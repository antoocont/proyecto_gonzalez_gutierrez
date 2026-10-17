package com.panalsuite.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.domain.model.Role
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun findById(userId: Long): UserEntity?

    @Query("SELECT * FROM users ORDER BY fullName")
    fun observeAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY fullName")
    fun observeByRole(role: Role): Flow<List<UserEntity>>
}