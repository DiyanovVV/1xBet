package com.bookmaker.onexbetapp.common.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import com.bookmaker.onexbetapp.view.MainActivity
import com.bookmaker.onexbetapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageReceiver : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            remoteMessage.notification!!.title?.let {
                remoteMessage.notification!!.body?.let { it1 ->
                    showNotification(
                        it,
                        it1
                    )
                }
            }
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            ApplicationProvider.getApplicationContext<Context>().packageName,
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.mipmap.ic_launcher
        )
        return remoteViews
    }

    // Method to display the notifications
    private fun showNotification(
        title: String,
        message: String
    ) {
        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, MainActivity::class.java)
        // Assign channel ID
        val channelId = "notification_channel"
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            ApplicationProvider.getApplicationContext(),
            channelId
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        builder = builder.setContent(
            getCustomDesign(title, message)
        )
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channelId, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager?.notify(0, builder.build())
    }
}