package com.panalsuite.mobile.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.data.repository.TicketRepository
import com.panalsuite.mobile.domain.model.TicketStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class TicketListUiState(
    val tickets: List<TicketEntity> = emptyList(),
    val statusFilter: TicketStatus? = null
)

class TicketListViewModel(
    repository: TicketRepository,
    user: UserEntity
) : ViewModel() {

    private val statusFilter = MutableStateFlow<TicketStatus?>(null)

    val uiState: StateFlow<TicketListUiState> = combine(
        repository.observeTicketsFor(user),
        statusFilter
    ) { tickets, filter ->
        TicketListUiState(
            tickets = if (filter == null) tickets else tickets.filter { it.status == filter },
            statusFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TicketListUiState()
    )

    fun onStatusFilterChange(status: TicketStatus?) {
        statusFilter.value = status
    }

    companion object {
        fun factory(repository: TicketRepository, user: UserEntity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { TicketListViewModel(repository, user) }
            }
    }
}