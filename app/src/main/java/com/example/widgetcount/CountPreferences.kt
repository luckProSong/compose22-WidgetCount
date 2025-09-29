package com.example.widgetcount

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CountPreferences {
    val COUNT_KEY = intPreferencesKey("count_key")

    private val Context.dataStore by preferencesDataStore("count_prefs")

    fun countFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[COUNT_KEY] ?: 0 }

    suspend fun setCount(context: Context, value: Int) {
        context.dataStore.edit { it[COUNT_KEY] = value }
    }
}