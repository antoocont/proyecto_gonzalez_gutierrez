package com.panalsuite.mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.panalsuite.mobile.data.local.dao.CommentDao
import com.panalsuite.mobile.data.local.dao.HistoryDao
import com.panalsuite.mobile.data.local.dao.TicketDao
import com.panalsuite.mobile.data.local.dao.UserDao
import com.panalsuite.mobile.data.local.entity.CommentEntity
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.data.local.entity.TicketHistoryEntity
import com.panalsuite.mobile.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TicketEntity::class,
        CommentEntity::class,
        TicketHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PanalSuiteDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun ticketDao(): TicketDao
    abstract fun commentDao(): CommentDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: PanalSuiteDatabase? = null

        fun getInstance(context: Context): PanalSuiteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PanalSuiteDatabase::class.java,
                    "panalsuite.db"
                ).build().also { instance = it }
            }
    }
}