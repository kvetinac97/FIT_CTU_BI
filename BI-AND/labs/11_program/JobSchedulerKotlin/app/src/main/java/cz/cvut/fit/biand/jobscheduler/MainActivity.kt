package cz.cvut.fit.biand.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val JOB_ID = 1
    }

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtTime = findViewById<TextView>(R.id.txt_time)

        val helper = SharedPreferencesHelper(this)
        helper.observeData()
            .observe(this, { time ->
                txtTime.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
                    .format(Date(time!!))
            })

        findViewById<View>(R.id.btn_schedule)
            .setOnClickListener {
                // [1] naimplementujte naplanovani Jobu, ktery se spusti jen pri pripojene nabijecce a
                //  kdyz je zarizeni na wifi
                val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val job = JobInfo.Builder(JOB_ID, ComponentName(this@MainActivity, MyJobService::class.java))
                        .setRequiresCharging(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                        .build()
                jobScheduler.schedule(job)
            }
    }

}
