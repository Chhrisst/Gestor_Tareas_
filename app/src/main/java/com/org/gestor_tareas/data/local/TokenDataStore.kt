package com.org.gestor_tareas.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TokenDataStore(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val ROL_KEY = stringPreferencesKey("user_rol")
        private val NOMBRE_KEY = stringPreferencesKey("user_nombre")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val rol: Flow<String?> = context.dataStore.data.map { it[ROL_KEY] }
    val nombre: Flow<String?> = context.dataStore.data.map { it[NOMBRE_KEY] }
    val email: Flow<String?> = context.dataStore.data.map { it[EMAIL_KEY] }

    suspend fun saveAuthData(token: String, rol: String, nombre: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[ROL_KEY] = rol
            preferences[NOMBRE_KEY] = nombre
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(ROL_KEY)
            preferences.remove(NOMBRE_KEY)
            preferences.remove(EMAIL_KEY)
        }
    }
}
