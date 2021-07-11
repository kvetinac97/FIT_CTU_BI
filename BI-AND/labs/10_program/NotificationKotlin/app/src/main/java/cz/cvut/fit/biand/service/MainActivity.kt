package cz.cvut.fit.biand.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val connection = LocalServiceConnection()
    private var stopwatchService: StopwatchService? = null

    internal inner class LocalServiceConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            setupService((service as StopwatchService.LocalBinder).service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            stopwatchService = null
        }
    }

    private fun setupService(service: StopwatchService) {
        service.observeTime()
            .observe(this, Observer { aLong -> txt_time.text = String.format("%d s", aLong) })
        this.stopwatchService = service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.setOnClickListener { sendActionToService(StopwatchService.ACTION_START) }

        btn_pause.setOnClickListener { sendActionToService(StopwatchService.ACTION_PAUSE) }

        btn_stop.setOnClickListener { sendActionToService(StopwatchService.ACTION_STOP) }
    }

    private fun sendActionToService(actionStart: String) {
        val startTimerIntent = Intent(this@MainActivity, StopwatchService::class.java)
        startTimerIntent.action = actionStart
        startService(startTimerIntent)
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, StopwatchService::class.java), connection, Service.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (stopwatchService?.runningState == StopwatchService.RunningState.STOPPED || stopwatchService?.runningState == StopwatchService.RunningState.INIT) {
            stopService(Intent(this, StopwatchService::class.java))
        }
    }
}
