package com.panalsuite.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.panalsuite.mobile.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert
    suspend fun insert(comment: CommentEntity): Long

    @Insert
    suspend fun insertAll(comments: List<CommentEntity>)

    @Query("SELECT * FROM comments WHERE ticketId = :ticketId ORDER BY createdAt ASC")
    fun observeByTicket(ticketId: Long): Flow<List<CommentEntity>>
}