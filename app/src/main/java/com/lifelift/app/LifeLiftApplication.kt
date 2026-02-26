package com.lifelift.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class with Hilt DI initialization
 */
import com.lifelift.app.core.notification.VitaminNotificationScheduler
import javax.inject.Inject

@HiltAndroidApp
class LifeLiftApplication : Application() {
    
    @Inject
    lateinit var notificationScheduler: VitaminNotificationScheduler

    override fun onCreate() {
        super.onCreate()
        notificationScheduler.createNotificationChannel()
    }
}
