package cz.cvut.fit.biand.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Service managing stop watch logic
 */
class StopwatchService : Service() {

    companion object {

        const val TAG = "StopWatch"

        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"
        const val ACTION_PAUSE = "action_pause"

        const val NOTIFICATION_ID = 12
    }

    var runningState = RunningState.INIT
        private set

    private val binder = LocalBinder()

    internal var counter: Long = 0
    private var timer: Timer? = Timer()

    private val timeLiveData = MutableLiveData<Long>()

    private var clientBound = false

    enum class RunningState {
        INIT, STARTED, STOPPED, PAUSED
    }

    inner class LocalBinder : Binder() {
        val service: StopwatchService
            get() = this@StopwatchService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "on create")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "on start command " + intent.action!!)
        when (intent.action) {
            ACTION_START -> startStopwatch()
            ACTION_PAUSE -> pauseStopwatch()
            ACTION_STOP -> stopStopwatch()
        }
        return START_STICKY
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "on unbind")
        clientBound = false
        return true
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.d(TAG, "on rebind")
        clientBound = true
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "on bind")
        clientBound = true
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "on destroy ")
        timer!!.cancel()
    }

    fun observeTime(): LiveData<Long> {
        return timeLiveData
    }

    private fun startStopwatch() {
        if (runningState == RunningState.INIT || runningState == RunningState.STOPPED) {
            counter = 0
        }
        val timerTask = object : TimerTask() {

            override fun run() {
                counter++
                timeLiveData.postValue(counter)
                if (!clientBound) {
                    showNotification(counter, runningState)
                } else {
                    hideNotification()
                }
            }
        }
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer()
        timer!!.scheduleAtFixedRate(timerTask, 0, TimeUnit.SECONDS.toMillis(1))
        runningState = RunningState.STARTED
    }

    private fun hideNotification() {
        stopForeground(true)
    }

    private fun showNotification(counter: Long, runningState: RunningState) {
        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(String.format("%d s", counter))
            .setContentText(runningState.name)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            .setSmallIcon(R.drawable.ic_notification)

        when (runningState) {
            RunningState.STARTED -> {
                builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_pause, getString(R.string.pause), PendingIntent.getService(
                    this, 0, Intent(this, StopwatchService::class.java).setAction(ACTION_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT)
                ).build()
                )
                builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_stop, getString(R.string.stop), PendingIntent.getService(
                    this, 0, Intent(this, StopwatchService::class.java).setAction(ACTION_STOP), PendingIntent.FLAG_UPDATE_CURRENT)
                ).build()
                )
            }
            RunningState.PAUSED -> builder.addAction(NotificationCompat.Action.Builder(R.drawable.ic_play, getString(R.string.start), PendingIntent.getService(
                this, 0, Intent(this, StopwatchService::class.java).setAction(ACTION_START), PendingIntent.FLAG_UPDATE_CURRENT)
            ).build())
        }

        startForeground(NOTIFICATION_ID, builder.build())
    }

    private fun pauseStopwatch() {
        runningState = RunningState.PAUSED
        timer!!.cancel()
        if (!clientBound) {
            showNotification(counter, runningState)
        }
    }

    private fun stopStopwatch() {
        runningState = RunningState.STOPPED
        timer!!.cancel()
        timeLiveData.postValue(0L)
        if (!clientBound) {
            hideNotification()
        }
    }
}
