package cn.wufuqi.nbnotification.notification.bar

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cn.wufuqi.nbnotification.NBNotification
import cn.wufuqi.nbnotification.utils.ActivityUtils


object NotificationBar {

    @SuppressLint("UnspecifiedImmutableFlag")
    fun notify(nbBuilder: NBNotification.Builder) {

        val channelId = nbBuilder.groupId.toString() + "_channel"
        val channelName = nbBuilder.appName
        val id = nbBuilder.groupId

        val application = ActivityUtils.getApplication()!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            (application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(notificationChannel)
        }
        val builder = NotificationCompat.Builder(application)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId)
        }
        builder.setContentTitle(nbBuilder.title)
            .setContentText(nbBuilder.content)
            .setLargeIcon(BitmapFactory.decodeResource(application.resources, nbBuilder.resId))
            .setSmallIcon(nbBuilder.resId)
            .setAutoCancel(nbBuilder.autoCancel)
            .setWhen(System.currentTimeMillis())

        if (nbBuilder.intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setContentIntent(
                    PendingIntent.getActivity(
                        ActivityUtils.getContext(),
                        0,
                        nbBuilder.intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
            } else {
                builder.setContentIntent(
                    PendingIntent.getActivity(
                        ActivityUtils.getContext(),
                        0,
                        nbBuilder.intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                )
            }
        }

        NotificationManagerCompat.from(application).notify(
            channelName,
            id,
            builder.build()
        )
    }

}