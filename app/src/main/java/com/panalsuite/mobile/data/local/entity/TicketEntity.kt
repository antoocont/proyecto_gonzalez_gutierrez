package com.panalsuite.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.TicketCategory
import com.panalsuite.mobile.domain.model.TicketStatus

@Entity(
    tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["requesterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["assigneeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("requesterId"), Index("assigneeId")]
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: TicketCategory,
    val priority: Priority,
    val status: TicketStatus,
    val branch: String,
    val requesterId: Long,
    val assigneeId: Long? = null,
    val team: String,
    val createdAt: Long,
    val updatedAt: Long
)