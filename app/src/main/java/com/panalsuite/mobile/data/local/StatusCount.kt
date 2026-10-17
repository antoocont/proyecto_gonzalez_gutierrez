package com.panalsuite.mobile.data.local

import com.panalsuite.mobile.domain.model.TicketStatus

data class StatusCount(
    val status: TicketStatus,
    val total: Int
)