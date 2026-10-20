package com.panalsuite.mobile.data.repository

import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.TicketCategory

data class NewTicket(
    val title: String,
    val description: String,
    val branch: String,
    val category: TicketCategory,
    val priority: Priority
) {
    val team: String
        get() = when (category) {
            TicketCategory.INFRASTRUCTURE,
            TicketCategory.PHARMACY -> "Infraestructura"
            TicketCategory.STORE,
            TicketCategory.SUPPLIER,
            TicketCategory.INTERNAL_SUPPORT -> "Soporte TI"
        }
}