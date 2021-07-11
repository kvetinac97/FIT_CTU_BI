package cz.cvut.fit.biand.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var stopwatchService: StopwatchService? = null

    private fun setupService(service: StopwatchService) {
        Log.d(StopwatchService.TAG, "Setup service $service")
        service.observeTime()
            .observe(this, { time -> txt_time.text = String.format("%d s", time) })
        this.stopwatchService = service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.setOnClickListener {
            // 4 poslete service prikaz k startu. (hint: vyuzijte konstant ACTION_XXX z StopwatchService
            startService(Intent(this, StopwatchService::class.java).setAction(StopwatchService.ACTION_START))
        }

        btn_pause.setOnClickListener {
            // 5 poslete service prikaz k pause. (hint: vyuzijte konstant ACTION_XXX z StopwatchService
            startService(Intent(this, StopwatchService::class.java).setAction(StopwatchService.ACTION_PAUSE))
        }

        btn_stop.setOnClickListener {
            // 6 poslete service prikaz k stopu. (hint: vyuzijte konstant ACTION_XXX z StopwatchService
            startService(Intent(this, StopwatchService::class.java).setAction(StopwatchService.ACTION_STOP))
        }
    }

    // 2 implementujte bind k StopwatchService. Je treba vytvorit tridu dedici od ServiceConnection
    // po pripojeni service muzete vyuzit metodu setupService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            setupService((service as StopwatchService.StopwatchBinder).service)
        }

        override fun onServiceDisconnected (name: ComponentName?) {
            stopwatchService = null
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, StopwatchService::class.java), connection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        // 3 implementujte unbind service
    }

    override fun onDestroy() {
        super.onDestroy()
        // 10 pokud service neni ve stavu STARTED, stopnete ji. Vyuzijte na Service property runningState
        if (stopwatchService?.runningState != StopwatchService.RunningState.STARTED)
            stopService(Intent(this, StopwatchService::class.java))
    }
}
