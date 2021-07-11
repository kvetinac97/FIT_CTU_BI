package cz.cvut.fit.biand.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Service managing stop watch logic
 */
class StopwatchService : Service() {

    var runningState = RunningState.INIT
        private set

    private var counter: Long = 0
    private var timer: Timer? = Timer()

    private val timeLiveData = MutableLiveData<Long>()

    // 8 do metod onBind, onRebind a onUnbind doplnte kod nastavujici tuto property
    private var clientBound = false

    enum class RunningState {
        INIT, STARTED, STOPPED, PAUSED
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "on create")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "on start command ${intent.action}")
        // 7 podle Action intentu volejte spravne metody startStopwatch, stopStopwatch nebo pauseStopwatch
        // vyuzijte vyse zminene konstanty
        when (intent.action) {
            ACTION_START -> startStopwatch()
            ACTION_STOP -> stopStopwatch()
            ACTION_PAUSE -> pauseStopwatch()
        }
        return START_STICKY
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "on unbind")
        clientBound = false
        // treba vracet true, jinak se nevola rebind
        return true
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        clientBound = true
        Log.d(TAG, "on rebind")
    }

    inner class StopwatchBinder : Binder () {
        val service = this@StopwatchService
    }
    private val binder = StopwatchBinder()

    override fun onBind (intent: Intent) : IBinder {
        Log.d(TAG, "on bind")
        clientBound = true
        // 1 implementujte v Service Binder, aby se mohla Activity pripojit
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "on destroy ")
        timer?.cancel()
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
                // 9 do logu vypisujte hodnotu pouze tehdy, neni li zadny klient pripojen.
                // jinak hodnotu posilejte do LiveDat, aby ji mohl klienti cist
                Log.d(TAG, "Update counter")
                if (!clientBound)
                    Log.d(TAG, String.format("%d s", counter))
                else
                    timeLiveData.postValue(counter)
            }
        }

        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask, 0, TimeUnit.SECONDS.toMillis(1))
        runningState = RunningState.STARTED
    }

    private fun pauseStopwatch() {
        runningState = RunningState.PAUSED
        timer?.cancel()
    }

    private fun stopStopwatch() {
        runningState = RunningState.STOPPED
        timer?.cancel()
        counter = 0L
        timeLiveData.postValue(0L)
    }

    companion object {

        const val TAG = "StopWatch"

        const val ACTION_START = "action_start"
        const val ACTION_STOP = "action_stop"
        const val ACTION_PAUSE = "action_pause"
    }
}
