package cz.cvut.fit.biand.async.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.cvut.fit.biand.async.R
import cz.cvut.fit.biand.async.ui.asynctask.AsyncTaskActivity
import cz.cvut.fit.biand.async.ui.handler.HandlerActivity
import cz.cvut.fit.biand.async.ui.handlerthread.HandlerThreadActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * Activity with three buttons leading to different solution of the task
 */
class DashboardActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        btn_handler.setOnClickListener { startActivity(Intent(this@DashboardActivity, HandlerActivity::class.java)) }
        btn_async_task.setOnClickListener { startActivity(Intent(this@DashboardActivity, AsyncTaskActivity::class.java)) }
        btn_handler_thread.setOnClickListener { startActivity(Intent(this@DashboardActivity, HandlerThreadActivity::class.java)) }
    }

}
