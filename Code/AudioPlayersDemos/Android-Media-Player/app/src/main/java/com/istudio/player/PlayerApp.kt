package com.istudio.player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.istudio.player.Constants.NOTIFICATION_CHANNEL_ID
import com.istudio.player.Constants.NOTIFICATION_CHANNEL_NAME

const val APP_TAG = "Pokemon"
class PlayerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= (Build.VERSION_CODES.O)){

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME ,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

}