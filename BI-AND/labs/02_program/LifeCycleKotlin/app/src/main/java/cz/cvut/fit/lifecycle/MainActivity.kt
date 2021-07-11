package cz.cvut.fit.lifecycle

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var ctx: Context
    private lateinit var act: Activity

    /*
        1.

        2021-02-24 18:08:10.794 4820-4820/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:08:10.794 4820-4820/cz.cvut.fit.lifecycle V/Main: onStop
        2021-02-24 18:08:10.796 4820-4820/cz.cvut.fit.lifecycle V/Main: onDestroy
        2021-02-24 18:08:10.938 4820-4820/cz.cvut.fit.lifecycle V/Main: onCreate
        2021-02-24 18:08:10.944 4820-4820/cz.cvut.fit.lifecycle V/Main: onStart
        2021-02-24 18:08:10.955 4820-4820/cz.cvut.fit.lifecycle V/Main: onResume

        NE

        2.

        2021-02-24 18:09:39.198 4980-4980/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:09:39.470 4980-4980/cz.cvut.fit.lifecycle V/Another: onCreate
        2021-02-24 18:09:39.480 4980-4980/cz.cvut.fit.lifecycle V/Another: onStart
        2021-02-24 18:09:39.490 4980-4980/cz.cvut.fit.lifecycle V/Another: onResume
        2021-02-24 18:09:40.132 4980-4980/cz.cvut.fit.lifecycle V/Main: onStop

        2.b

        2021-02-24 18:11:43.910 5620-5620/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:11:44.053 5620-5620/cz.cvut.fit.lifecycle V/Another: onCreate
        2021-02-24 18:11:44.068 5620-5620/cz.cvut.fit.lifecycle V/Another: onStart
        2021-02-24 18:11:44.073 5620-5620/cz.cvut.fit.lifecycle V/Another: onResume
        2021-02-24 18:11:44.645 5620-5620/cz.cvut.fit.lifecycle V/Main: onStop
        2021-02-24 18:11:44.660 5620-5620/cz.cvut.fit.lifecycle V/Main: onDestroy

        3.

        2021-02-24 18:12:37.586 5932-5932/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:12:37.592 5932-5932/cz.cvut.fit.lifecycle V/Main: onStop

        2021-02-24 18:12:42.324 5932-5932/cz.cvut.fit.lifecycle V/Main: onRestart
        2021-02-24 18:12:42.328 5932-5932/cz.cvut.fit.lifecycle V/Main: onStart
        2021-02-24 18:12:42.329 5932-5932/cz.cvut.fit.lifecycle V/Main: onResume

        4. Home

        2021-02-24 18:13:36.226 6057-6057/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:13:37.209 6057-6057/cz.cvut.fit.lifecycle V/Main: onStop


        Back

        2021-02-24 18:13:49.408 6057-6057/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:13:50.099 6057-6057/cz.cvut.fit.lifecycle V/Main: onStop
        2021-02-24 18:13:50.099 6057-6057/cz.cvut.fit.lifecycle V/Main: onDestroy


        Home (destroy)

        2021-02-24 18:15:09.042 6057-6057/cz.cvut.fit.lifecycle V/Main: onPause
        2021-02-24 18:15:09.963 6057-6057/cz.cvut.fit.lifecycle V/Main: onStop
        2021-02-24 18:15:10.003 6057-6057/cz.cvut.fit.lifecycle V/Main: onDestroy

        5. Je to náš Dialog (ne jiný)
     */

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
            val dialog = AlertDialog.Builder(this@MainActivity).create()
            dialog.setMessage("Dialog button")
            dialog.setTitle("Dialog!")
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dia, _ -> dia.dismiss() }
            dialog.setCancelable(true)
            dialog.show()
        }

        button = findViewById(R.id.finishButton)
        button.setOnClickListener { act.finish() }

        button = findViewById(R.id.startActivityButton)
        button.setOnClickListener {
            val i = Intent(ctx, AnotherActivity::class.java)
            startActivity(i)
        }

    }

    companion object {
        private const val TAG = "Main"
    }

}