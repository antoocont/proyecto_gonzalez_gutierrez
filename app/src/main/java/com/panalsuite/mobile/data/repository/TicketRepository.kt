package com.panalsuite.mobile.data.repository

import androidx.room.withTransaction
import com.panalsuite.mobile.data.local.PanalSuiteDatabase
import com.panalsuite.mobile.data.local.entity.CommentEntity
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.TicketHistoryEntity
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.domain.model.Role
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.domain.model.TicketWorkflow
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val database: PanalSuiteDatabase) {

    private val ticketDao = database.ticketDao()
    private val commentDao = database.commentDao()
    private val historyDao = database.historyDao()
    private val userDao = database.userDao()

    fun observeTicketsFor(user: UserEntity): Flow<List<TicketEntity>> = when (user.role) {
        Role.REQUESTER -> ticketDao.observeByRequester(user.id)
        Role.RESOLVER -> ticketDao.observeByTeam(user.team)
        Role.SUPERVISOR -> ticketDao.observeAll()
    }

    fun observeTicket(ticketId: Long): Flow<TicketEntity?> = ticketDao.observeById(ticketId)

    fun observeComments(ticketId: Long): Flow<List<CommentEntity>> =
        commentDao.observeByTicket(ticketId)

    fun observeHistory(ticketId: Long): Flow<List<TicketHistoryEntity>> =
        historyDao.observeByTicket(ticketId)

    fun observeUsers(): Flow<List<UserEntity>> = userDao.observeAll()

    suspend fun changeStatus(ticketId: Long, newStatus: TicketStatus, actor: UserEntity) {
        database.withTransaction { applyStatusChange(ticketId, newStatus, actor) }
    }

    suspend fun assignToMe(ticketId: Long, actor: UserEntity) {
        database.withTransaction { applyAssignment(ticketId, actor) }
    }

    suspend fun addComment(ticketId: Long, author: UserEntity, message: String) {
        database.withTransaction { insertComment(ticketId, author, message) }
    }

    private suspend fun applyStatusChange(
        ticketId: Long,
        newStatus: TicketStatus,
        actor: UserEntity
    ) {
        val ticket = ticketDao.findById(ticketId) ?: return
        if (newStatus !in TicketWorkflow.nextStatuses(ticket.status)) return
        val now = System.currentTimeMillis()
        ticketDao.update(ticket.copy(status = newStatus, updatedAt = now))
        historyDao.insert(
            TicketHistoryEntity(
                ticketId = ticketId,
                authorId = actor.id,
                description = "Estado cambiado de ${ticket.status.label} a ${newStatus.label}",
                createdAt = now
            )
        )
    }

    private suspend fun applyAssignment(ticketId: Long, actor: UserEntity) {
        val ticket = ticketDao.findById(ticketId) ?: return
        val now = System.currentTimeMillis()
        val newStatus =
            if (ticket.status == TicketStatus.OPEN) TicketStatus.IN_PROGRESS else ticket.status
        ticketDao.update(ticket.copy(assigneeId = actor.id, status = newStatus, updatedAt = now))
        historyDao.insert(
            TicketHistoryEntity(
                ticketId = ticketId,
                authorId = actor.id,
                description = "Responsable asignado: ${actor.fullName}",
                createdAt = now
            )
        )
        if (newStatus != ticket.status) {
            historyDao.insert(
                TicketHistoryEntity(
                    ticketId = ticketId,
                    authorId = actor.id,
                    description = "Estado cambiado de ${ticket.status.label} a ${newStatus.label}",
                    createdAt = now + 1
                )
            )
        }
    }

    private suspend fun insertComment(ticketId: Long, author: UserEntity, message: String) {
        val ticket = ticketDao.findById(ticketId) ?: return
        val now = System.currentTimeMillis()
        commentDao.insert(
            CommentEntity(
                ticketId = ticketId,
                authorId = author.id,
                message = message.trim(),
                createdAt = now
            )
        )
        ticketDao.update(ticket.copy(updatedAt = now))
    }

    suspend fun createTicket(requester: UserEntity, draft: NewTicket): Long =
        database.withTransaction { insertTicket(requester, draft) }

    private suspend fun insertTicket(requester: UserEntity, draft: NewTicket): Long {
        val now = System.currentTimeMillis()
        val ticketId = ticketDao.insert(
            TicketEntity(
                title = draft.title.trim(),
                description = draft.description.trim(),
                category = draft.category,
                priority = draft.priority,
                status = TicketStatus.OPEN,
                branch = draft.branch.trim(),
                requesterId = requester.id,
                assigneeId = null,
                team = draft.team,
                createdAt = now,
                updatedAt = now
            )
        )
        historyDao.insert(
            TicketHistoryEntity(
                ticketId = ticketId,
                authorId = requester.id,
                description = "Ticket creado",
                createdAt = now
            )
        )
        return ticketId
    }
}