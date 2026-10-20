package com.panalsuite.mobile.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.data.repository.TicketRepository
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.Role
import com.panalsuite.mobile.domain.model.TicketStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class WorkloadItem(
    val name: String,
    val team: String,
    val active: Int
)

data class SummaryUiState(
    val isLoading: Boolean = true,
    val total: Int = 0,
    val unassigned: Int = 0,
    val criticalActive: Int = 0,
    val statusCounts: Map<TicketStatus, Int> = emptyMap(),
    val priorityCounts: Map<Priority, Int> = emptyMap(),
    val workload: List<WorkloadItem> = emptyList()
)

class SummaryViewModel(
    repository: TicketRepository,
    user: UserEntity
) : ViewModel() {

    val uiState: StateFlow<SummaryUiState> = combine(
        repository.observeTicketsFor(user),
        repository.observeUsers()
    ) { tickets, users ->
        buildSummary(tickets, users)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SummaryUiState()
    )

    companion object {
        fun factory(repository: TicketRepository, user: UserEntity): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { SummaryViewModel(repository, user) }
            }
    }
}

private fun buildSummary(tickets: List<TicketEntity>, users: List<UserEntity>): SummaryUiState {
    val active = tickets.filter {
        it.status != TicketStatus.RESOLVED && it.status != TicketStatus.CLOSED
    }
    return SummaryUiState(
        isLoading = false,
        total = tickets.size,
        unassigned = active.count { it.assigneeId == null },
        criticalActive = active.count { it.priority == Priority.CRITICAL },
        statusCounts = TicketStatus.entries.associateWith { status ->
            tickets.count { it.status == status }
        },
        priorityCounts = Priority.entries.associateWith { priority ->
            tickets.count { it.priority == priority }
        },
        workload = users
            .filter { it.role == Role.RESOLVER }
            .map { resolver ->
                WorkloadItem(
                    name = resolver.fullName,
                    team = resolver.team,
                    active = active.count { it.assigneeId == resolver.id }
                )
            }
    )
}