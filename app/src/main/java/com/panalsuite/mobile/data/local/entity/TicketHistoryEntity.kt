package com.panalsuite.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ticket_history",
    foreignKeys = [
        ForeignKey(
            entity = TicketEntity::class,
            parentColumns = ["id"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ticketId"), Index("authorId")]
)
data class TicketHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticketId: Long,
    val authorId: Long,
    val description: String,
    val createdAt: Long
)