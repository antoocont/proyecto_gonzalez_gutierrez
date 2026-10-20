package com.panalsuite.mobile.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.data.repository.NewTicket
import com.panalsuite.mobile.data.repository.TicketRepository
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.TicketCategory
import com.panalsuite.mobile.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateTicketUiState(
    val title: String = "",
    val description: String = "",
    val branch: String = "",
    val category: TicketCategory? = null,
    val priority: Priority = Priority.MEDIUM,
    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val branchError: Int? = null,
    val categoryError: Int? = null,
    val isSaving: Boolean = false,
    val createdTicketId: Long? = null
)

class CreateTicketViewModel(
    private val repository: TicketRepository,
    private val user: UserEntity
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onBranchChange(value: String) {
        _uiState.update { it.copy(branch = value, branchError = null) }
    }

    fun onCategorySelect(value: TicketCategory) {
        _uiState.update { it.copy(category = value, categoryError = null) }
    }

    fun onPrioritySelect(value: Priority) {
        _uiState.update { it.copy(priority = value) }
    }

    fun onCreatedHandled() {
        _uiState.update { it.copy(createdTicketId = null) }
    }

    fun onSubmit() {
        val current = _uiState.value
        if (current.isSaving) return

        val titleError = Validators.validateTicketTitle(current.title)
        val descriptionError = Validators.validateTicketDescription(current.description)
        val branchError = Validators.validateBranch(current.branch)
        val categoryError = Validators.validateCategory(current.category)
        val category = current.category

        if (titleError != null || descriptionError != null || branchError != null || category == null) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    descriptionError = descriptionError,
                    branchError = branchError,
                    categoryError = categoryError
                )
            }
            return
        }

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val ticketId = repository.createTicket(
                requester = user,
                draft = NewTicket(
                    title = current.title,
                    description = current.description,
                    branch = current.branch,
                    category = category,
                    priority = current.priority
                )
            )
            _uiState.value = CreateTicketUiState(createdTicketId = ticketId)
        }
    }

    companion object {
        fun factory(repository: TicketRepository, user: UserEntity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { CreateTicketViewModel(repository, user) }
            }
    }
}