package cz.cvut.fit.biand.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * Application class
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
            })
        }
    }
}