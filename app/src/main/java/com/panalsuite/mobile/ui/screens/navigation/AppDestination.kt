package com.panalsuite.mobile.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.panalsuite.mobile.R
import com.panalsuite.mobile.domain.model.Role

enum class AppDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val allowedRoles: Set<Role>
) {
    MY_TICKETS(
        route = "my_tickets",
        labelRes = R.string.nav_my_tickets,
        icon = Icons.Outlined.ConfirmationNumber,
        allowedRoles = setOf(Role.REQUESTER)
    ),
    NEW_TICKET(
        route = "new_ticket",
        labelRes = R.string.nav_new_ticket,
        icon = Icons.Outlined.AddCircleOutline,
        allowedRoles = setOf(Role.REQUESTER)
    ),
    QUEUE(
        route = "queue",
        labelRes = R.string.nav_queue,
        icon = Icons.Outlined.Inbox,
        allowedRoles = setOf(Role.RESOLVER)
    ),
    SUMMARY(
        route = "summary",
        labelRes = R.string.nav_summary,
        icon = Icons.Outlined.Dashboard,
        allowedRoles = setOf(Role.SUPERVISOR)
    ),
    ALL_TICKETS(
        route = "all_tickets",
        labelRes = R.string.nav_all_tickets,
        icon = Icons.Outlined.ConfirmationNumber,
        allowedRoles = setOf(Role.SUPERVISOR)
    ),
    PROFILE(
        route = "profile",
        labelRes = R.string.nav_profile,
        icon = Icons.Outlined.Person,
        allowedRoles = Role.entries.toSet()
    );

    companion object {
        fun tabsFor(role: Role): List<AppDestination> =
            entries.filter { role in it.allowedRoles }
    }
}

object Routes {
    const val TICKET_ID_ARG = "ticketId"
    const val TICKET_DETAIL = "ticket/{$TICKET_ID_ARG}"

    fun ticketDetail(ticketId: Long): String = "ticket/$ticketId"
}