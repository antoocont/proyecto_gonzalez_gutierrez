package com.panalsuite.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.panalsuite.mobile.data.local.StatusCount
import com.panalsuite.mobile.data.local.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert
    suspend fun insert(ticket: TicketEntity): Long

    @Insert
    suspend fun insertAll(tickets: List<TicketEntity>)

    @Update
    suspend fun update(ticket: TicketEntity)

    @Query("SELECT * FROM tickets ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE requesterId = :userId ORDER BY updatedAt DESC")
    fun observeByRequester(userId: Long): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE team = :team ORDER BY updatedAt DESC")
    fun observeByTeam(team: String): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE id = :ticketId")
    fun observeById(ticketId: Long): Flow<TicketEntity?>

    @Query("SELECT status, COUNT(*) AS total FROM tickets GROUP BY status")
    fun observeStatusCounts(): Flow<List<StatusCount>>


    @Query("SELECT * FROM tickets WHERE id = :ticketId")
    suspend fun findById(ticketId: Long): TicketEntity?
}