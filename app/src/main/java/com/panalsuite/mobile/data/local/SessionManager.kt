package com.panalsuite.mobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    val userId: Flow<Long?> = context.sessionDataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    suspend fun saveSession(userId: Long) {
        context.sessionDataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }

    private companion object {
        val USER_ID_KEY = longPreferencesKey("user_id")
    }
}