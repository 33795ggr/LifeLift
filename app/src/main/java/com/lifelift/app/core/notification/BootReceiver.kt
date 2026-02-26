package com.lifelift.app.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lifelift.app.core.data.repository.VitaminRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var vitaminRepository: VitaminRepository

    @Inject
    lateinit var startNotificationScheduler: VitaminNotificationScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                vitaminRepository.getAllVitamins().first().forEach { vitamin ->
                    startNotificationScheduler.scheduleNotification(vitamin)
                }
            }
        }
    }
}
