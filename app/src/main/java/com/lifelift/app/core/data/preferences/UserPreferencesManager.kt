package com.lifelift.app.core.data.preferences

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    AMOLED
}

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    RUSSIAN("ru", "Русский")
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val WEIGHT_KEY = stringPreferencesKey("weight_kg") // Default 70.0
    }
    
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(themeName)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }
    
    val language: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val langCode = preferences[LANGUAGE_KEY] ?: AppLanguage.ENGLISH.code
        AppLanguage.values().find { it.code == langCode } ?: AppLanguage.ENGLISH
    }

    val weight: Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[WEIGHT_KEY]?.toDoubleOrNull() ?: 70.0
    }
    
    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }
    
    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.code
        }
        
        // Apply language change immediately
        applyLanguage(language)
    }

    suspend fun setWeight(weightKg: Double) {
        context.dataStore.edit { preferences ->
            preferences[WEIGHT_KEY] = weightKg.toString()
        }
    }
    
    private fun applyLanguage(language: AppLanguage) {
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
    
    fun initializeLanguage(language: AppLanguage) {
        applyLanguage(language)
    }
}
