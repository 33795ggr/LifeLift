package com.lifelift.app.core.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.lifelift.app.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VitaminNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationScheduler: VitaminNotificationScheduler

    override fun onReceive(context: Context, intent: Intent) {
        val vitaminId = intent.getLongExtra("vitamin_id", -1)
        val vitaminName = intent.getStringExtra("vitamin_name") ?: "Vitamin"
        val vitaminDosage = intent.getStringExtra("vitamin_dosage") ?: ""
        val vitaminTime = intent.getStringExtra("vitamin_time") ?: "09:00"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, "vitamin_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Using default launcher icon for now
            .setContentTitle(context.getString(R.string.msg_notification_title, vitaminName))
            .setContentText(context.getString(R.string.msg_notification_text, vitaminDosage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(vitaminId.toInt(), builder.build())
        
        // Reschedule for next occurrence
        // We construct a temporary entity to pass to the scheduler. 
        // Note: Ideally, we should fetch from DB to get the latest state (notes, frequency change), 
        // but for reliability of the loop, using the intent data is a safe fallback.
        // However, if the user deleted the vitamin, we might be rescheduling a phantom?
        // But cancelNotification cancels the PendingIntent, so this receiver wouldn't fire if cancelled properly.
        // So it is safe to reschedule.
        
        // We need VitaminEntity. We'll use default values for missing fields as they don't affect scheduling logic (time is key).
        val vitamin = com.lifelift.app.core.data.local.entity.VitaminEntity(
            id = vitaminId,
            name = vitaminName,
            dosage = vitaminDosage,
            timeOfDay = vitaminTime,
            frequency = com.lifelift.app.core.data.local.entity.VitaminFrequency.DAILY,
            notes = ""
        )
        
        notificationScheduler.scheduleNotification(vitamin)
    }
}
