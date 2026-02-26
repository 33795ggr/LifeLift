package com.lifelift.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.lifelift.app.core.data.preferences.AppLanguage
import com.lifelift.app.core.data.preferences.ThemeMode
import com.lifelift.app.core.data.preferences.UserPreferencesManager
import kotlinx.coroutines.launch
import com.lifelift.app.core.ui.theme.LifeLiftTheme
import com.lifelift.app.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var preferencesManager: UserPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Enable edge-to-edge display
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode = 
                android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
        
        
        // Strictly Force Max Refresh Rate (120Hz fix)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val display = window.context.display
            val maxMode = display?.supportedModes?.maxByOrNull { it.refreshRate }
            if (maxMode != null) {
                val params = window.attributes
                params.preferredDisplayModeId = maxMode.modeId
                params.preferredRefreshRate = maxMode.refreshRate
                window.attributes = params
            }
        }
        
        
        // Observe language changes and apply them
        lifecycleScope.launch {
            preferencesManager.language.collect { language ->
                applyLanguage(language)
            }
        }
        
        
        setContent {
            val themeMode by preferencesManager.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            
            LifeLiftTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    @OptIn(ExperimentalFoundationApi::class)
                    CompositionLocalProvider(
                        LocalOverscrollConfiguration provides null
                    ) {
                        val navController = rememberNavController()
                        AppNavigation(navController = navController)
                    }
                }
            }
        }
    }
    
    private fun applyLanguage(language: AppLanguage) {
        val localeCode = when(language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.RUSSIAN -> "ru"
        }
        
        val appLocale = LocaleListCompat.forLanguageTags(localeCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}
