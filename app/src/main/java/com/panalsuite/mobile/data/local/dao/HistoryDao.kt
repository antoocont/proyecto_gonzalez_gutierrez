package com.panalsuite.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.panalsuite.mobile.data.local.entity.TicketHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(entry: TicketHistoryEntity): Long

    @Insert
    suspend fun insertAll(entries: List<TicketHistoryEntity>)

    @Query("SELECT * FROM ticket_history WHERE ticketId = :ticketId ORDER BY createdAt DESC")
    fun observeByTicket(ticketId: Long): Flow<List<TicketHistoryEntity>>
}