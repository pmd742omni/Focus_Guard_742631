package com.example.focusguard.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val COLOR_INDEX = intPreferencesKey("color_index")
        val ADMIN_PIN = stringPreferencesKey("admin_pin")
        val STUDY_MODE_ACTIVE = booleanPreferencesKey("study_mode_active")
    }

    val colorIndex: Flow<Int> = context.dataStore.data.map { it[COLOR_INDEX] ?: 0 }
    val adminPin: Flow<String?> = context.dataStore.data.map { it[ADMIN_PIN] }
    val studyModeActive: Flow<Boolean> = context.dataStore.data.map { it[STUDY_MODE_ACTIVE] ?: false }

    suspend fun setColorIndex(index: Int) {
        context.dataStore.edit { it[COLOR_INDEX] = index }
    }

    suspend fun setAdminPin(pin: String) {
        context.dataStore.edit { it[ADMIN_PIN] = pin }
    }

    suspend fun setStudyMode(active: Boolean) {
        context.dataStore.edit { it[STUDY_MODE_ACTIVE] = active }
    }
}
