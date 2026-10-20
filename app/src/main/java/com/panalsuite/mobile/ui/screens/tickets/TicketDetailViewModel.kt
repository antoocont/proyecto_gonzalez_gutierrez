package com.panalsuite.mobile.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.data.repository.TicketRepository
import com.panalsuite.mobile.domain.model.Role
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.domain.model.TicketWorkflow
import com.panalsuite.mobile.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CommentItem(
    val id: Long,
    val authorName: String,
    val authorRole: Role?,
    val message: String,
    val createdAt: Long
)

data class HistoryItem(
    val id: Long,
    val description: String,
    val authorName: String,
    val createdAt: Long
)

data class TicketDetailUiState(
    val isLoading: Boolean = true,
    val ticket: TicketEntity? = null,
    val requesterName: String = "",
    val assigneeName: String? = null,
    val canManage: Boolean = false,
    val canAssign: Boolean = false,
    val nextStatuses: List<TicketStatus> = emptyList(),
    val comments: List<CommentItem> = emptyList(),
    val history: List<HistoryItem> = emptyList()
)

data class CommentFormState(
    val text: String = "",
    val error: Int? = null
)

class TicketDetailViewModel(
    private val repository: TicketRepository,
    private val user: UserEntity,
    private val ticketId: Long
) : ViewModel() {

    private val canManage = user.role != Role.REQUESTER

    val uiState: StateFlow<TicketDetailUiState> = combine(
        repository.observeTicket(ticketId),
        repository.observeUsers(),
        repository.observeComments(ticketId),
        repository.observeHistory(ticketId)
    ) { ticket, users, comments, history ->
        val usersById = users.associateBy { it.id }
        TicketDetailUiState(
            isLoading = false,
            ticket = ticket,
            requesterName = ticket?.let { usersById[it.requesterId]?.fullName }.orEmpty(),
            assigneeName = ticket?.assigneeId?.let { usersById[it]?.fullName },
            canManage = canManage,
            canAssign = ticket != null &&
                    user.role == Role.RESOLVER &&
                    ticket.assigneeId != user.id &&
                    ticket.status != TicketStatus.CLOSED,
            nextStatuses = if (ticket != null && canManage) {
                TicketWorkflow.nextStatuses(ticket.status)
            } else {
                emptyList()
            },
            comments = comments.map { comment ->
                val author = usersById[comment.authorId]
                CommentItem(
                    id = comment.id,
                    authorName = author?.fullName.orEmpty(),
                    authorRole = author?.role,
                    message = comment.message,
                    createdAt = comment.createdAt
                )
            },
            history = history.map { entry ->
                HistoryItem(
                    id = entry.id,
                    description = entry.description,
                    authorName = usersById[entry.authorId]?.fullName.orEmpty(),
                    createdAt = entry.createdAt
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TicketDetailUiState()
    )

    private val _form = MutableStateFlow(CommentFormState())
    val form: StateFlow<CommentFormState> = _form.asStateFlow()

    fun onCommentChange(text: String) {
        _form.update { it.copy(text = text, error = null) }
    }

    fun onSendComment() {
        val message = _form.value.text
        val error = Validators.validateComment(message)
        if (error != null) {
            _form.update { it.copy(error = error) }
            return
        }
        viewModelScope.launch {
            repository.addComment(ticketId, user, message)
            _form.value = CommentFormState()
        }
    }

    fun onStatusChange(newStatus: TicketStatus) {
        viewModelScope.launch { repository.changeStatus(ticketId, newStatus, user) }
    }

    fun onAssignToMe() {
        viewModelScope.launch { repository.assignToMe(ticketId, user) }
    }

    companion object {
        fun factory(
            repository: TicketRepository,
            user: UserEntity,
            ticketId: Long
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { TicketDetailViewModel(repository, user, ticketId) }
        }
    }
}