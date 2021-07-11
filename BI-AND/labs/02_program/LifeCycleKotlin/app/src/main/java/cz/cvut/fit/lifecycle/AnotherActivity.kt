package cz.cvut.fit.lifecycle

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AnotherActivity : Activity() {

    private lateinit var ctx: Context
    private lateinit var act: Activity

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        Log.v(TAG, "onCreate")

        ctx = applicationContext
        act = this

        inicialization()
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }

    private fun inicialization() {
        val tv = findViewById<TextView>(R.id.activityName)
        tv.text = TAG

        var button = findViewById<Button>(R.id.dialogButton)
        button.setOnClickListener {
            val dialog = Dialog(act)
            dialog.setTitle("Dialog")
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }

        button = findViewById(R.id.finishButton)
        button.setOnClickListener { act.finish() }

        button = findViewById(R.id.startActivityButton)
        button.setOnClickListener { startActivity(Intent(act, AnotherActivity::class.java)) }
    }

    companion object {

        private val TAG = "Another"
    }

}