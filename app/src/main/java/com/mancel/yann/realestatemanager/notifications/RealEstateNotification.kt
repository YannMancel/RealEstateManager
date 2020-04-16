package com.mancel.yann.realestatemanager.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.activities.MainActivity

/**
 * Created by Yann MANCEL on 16/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.notifications
 */
object RealEstateNotification {

    // FIELDS --------------------------------------------------------------------------------------

    private const val NOTIFICATION_ID = 2020
    private const val NOTIFICATION_CHANNEL =
        "com.mancel.yann.realestatemanager.notifications.RealEstateNotification"

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Sends a notification with the message in argument
     * @param context       a [Context]
     * @param messageBody   a [String] that contains the message
     */
    fun sendVisualNotification(
        context: Context,
        messageBody: String
    ) {
        // Intent & PendingIntent
        val intent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Style for the Notification
        val notificationStyle = NotificationCompat.BigTextStyle().bigText(messageBody)

        // Notification Compat
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL
        )
        .setSmallIcon(R.drawable.ic_home)
        .setContentTitle(context.getString(R.string.notification_title))
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setStyle(notificationStyle)

        // API level >= API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }

            // Notification Manager
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(notificationChannel)
        }

        // Notification Manager Compat and show it
        NotificationManagerCompat
            .from(context)
            .notify(
                NOTIFICATION_ID,
                builder.build()
            )
    }
}