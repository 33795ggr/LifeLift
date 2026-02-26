package com.lifelift.app.core.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.lifelift.app.core.data.local.entity.VitaminEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitaminNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(vitamin: VitaminEntity) {
        val intent = Intent(context, VitaminNotificationReceiver::class.java).apply {
            putExtra("vitamin_id", vitamin.id)
            putExtra("vitamin_name", vitamin.name)
            putExtra("vitamin_dosage", vitamin.dosage)
            putExtra("vitamin_time", vitamin.timeOfDay)
        }

        // Parse time string "HH:mm"
        val timeParts = vitamin.timeOfDay.split(":")
        if (timeParts.size != 2) return
        
        val hour = timeParts[0].toIntOrNull() ?: 9
        val minute = timeParts[1].toIntOrNull() ?: 0

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // If time has passed today, schedule for tomorrow
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            vitamin.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("VitaminScheduler", "Scheduled notification for ${vitamin.name} at ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("VitaminScheduler", "Failed to schedule alarm: permission denied", e)
        }
    }

    fun cancelNotification(vitamin: VitaminEntity) {
        val intent = Intent(context, VitaminNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            vitamin.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Vitamin Reminders"
            val descriptionText = "Notifications to take your vitamins"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("vitamin_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
