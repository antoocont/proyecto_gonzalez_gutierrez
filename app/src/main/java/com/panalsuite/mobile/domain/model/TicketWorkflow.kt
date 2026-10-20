package com.panalsuite.mobile.domain.model

object TicketWorkflow {

    fun nextStatuses(current: TicketStatus): List<TicketStatus> = when (current) {
        TicketStatus.OPEN -> listOf(TicketStatus.IN_PROGRESS)
        TicketStatus.IN_PROGRESS -> listOf(TicketStatus.ON_HOLD, TicketStatus.RESOLVED)
        TicketStatus.ON_HOLD -> listOf(TicketStatus.IN_PROGRESS)
        TicketStatus.RESOLVED -> listOf(TicketStatus.CLOSED, TicketStatus.IN_PROGRESS)
        TicketStatus.CLOSED -> emptyList()
    }
}