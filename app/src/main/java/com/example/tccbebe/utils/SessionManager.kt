package com.example.tccbebe.utils

import android.content.Context
import android.util.Log

object SessionManager {
    private const val PREFS_NAME = "app_prefs"

    // Chaves para SharedPreferences
    private const val KEY_USER_ID = "key_user_id"
    private const val KEY_RESPONSAVEL_ID = "key_responsavel_id"
    private const val KEY_AUTH_TOKEN = "key_auth_token"

    // ------------------- USER ID -------------------
    fun saveUserId(context: Context, userId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun clearUserId(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    // ------------------- RESPONSAVEL ID -------------------
    fun saveResponsavelId(context: Context, responsavelId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_RESPONSAVEL_ID, responsavelId).apply()
    }

    fun getResponsavelId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_RESPONSAVEL_ID, -1)
    }

    fun clearResponsavelId(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_RESPONSAVEL_ID).apply()
    }

    // ------------------- AUTH TOKEN -------------------
    fun saveAuthToken(context: Context, token: String) {
        Log.d("SESSION_MANAGER", "💾 Salvando token: $token")
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
        Log.d("SESSION_MANAGER", "✅ Token salvo com sucesso")
    }

    fun getAuthToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = prefs.getString(KEY_AUTH_TOKEN, null)
        Log.d("SESSION_MANAGER", "🔍 Recuperando token: $token")
        return token
    }

    fun clearAuthToken(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    fun getBearerToken(context: Context): String? {
        val token = getAuthToken(context)
        val bearerToken = if (token != null) "Bearer $token" else null
        Log.d("SESSION_MANAGER", "🎯 getBearerToken retornando: $bearerToken")
        return bearerToken
    }

    // ------------------- CLEAR ALL -------------------
    fun clearAll(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
