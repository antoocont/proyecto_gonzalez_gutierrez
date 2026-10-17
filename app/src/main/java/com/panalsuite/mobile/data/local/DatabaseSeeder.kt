package com.panalsuite.mobile.data.local

import androidx.room.withTransaction

class DatabaseSeeder(private val database: PanalSuiteDatabase) {

    suspend fun seedIfEmpty() {
        if (database.userDao().count() > 0) return

        val now = System.currentTimeMillis()
        database.withTransaction {
            database.userDao().insertAll(SeedData.users())
            database.ticketDao().insertAll(SeedData.tickets(now))
            database.commentDao().insertAll(SeedData.comments(now))
            database.historyDao().insertAll(SeedData.history(now))
        }
    }
}