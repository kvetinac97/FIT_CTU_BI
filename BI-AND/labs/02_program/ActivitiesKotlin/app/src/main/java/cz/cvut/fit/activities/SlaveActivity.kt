package cz.cvut.fit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SlaveActivity : AppCompatActivity() {

    private lateinit var numberTextView: TextView
    private lateinit var likeCheckBox: CheckBox

    private var number: Int = 0

    private var handler: Handler? = null

    private val counter = object : Runnable {
        override fun run() {
            showNumber(++number)
            handler?.postDelayed(this, COUNTER_DELAY.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_slave)

        number = intent.getIntExtra(EXTRA_NUMBER, 0)

        numberTextView = findViewById(R.id.number)
        likeCheckBox = findViewById(R.id.like_button)
        showNumber(number)

        val onClickListener = View.OnClickListener { v ->
            when (v.id) {
                R.id.use -> {
                    val resultIntent = Intent().putExtra(EXTRA_NUMBER, number)

                    // [2] nastavte výsledek pro volající aktivitu
                    setResult(RESULT_OK, resultIntent)

                    finish()
                }
                R.id.number -> showNumber(++number)
            }
        }

        findViewById<View>(R.id.use).setOnClickListener(onClickListener)
        findViewById<View>(R.id.number).setOnClickListener(onClickListener)
        likeCheckBox.isChecked = loadLike()
    }

    override fun onResume() {
        super.onResume()
        // spustíme counter
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(counter, COUNTER_DELAY.toLong())
    }

    override fun onPause() {
        super.onPause()
        // zastavíme counter
        handler = null
        saveLike(likeCheckBox.isChecked)
    }

    private fun showNumber(number: Int) {
        numberTextView.text = number.toString()
    }

    private fun saveLike(like: Boolean) {
        getPreferences(MODE_PRIVATE)
            .edit()
            .putBoolean(LIKE_PREF, like)
            .apply()
    }

    private fun loadLike(): Boolean {
        return getPreferences(MODE_PRIVATE)
            .getBoolean(LIKE_PREF, false)
    }

    companion object {

        const val COUNTER_DELAY = 2000

        const val EXTRA_NUMBER = "number"

        private const val LIKE_PREF = "like"

        fun start(act: AppCompatActivity, number: Int) {
            val intent = Intent(act, SlaveActivity::class.java)
            intent.putExtra(EXTRA_NUMBER, number)
            act.startActivity(intent)
        }

        fun startForResult(act: AppCompatActivity, number: Int, resultLauncher: ActivityResultLauncher<Intent>) {
            val intent = Intent(act, SlaveActivity::class.java)
            intent.putExtra(EXTRA_NUMBER, number)

            // [2] nastartujte slave aktivitu tak, abychom od ní zpět dostali upravené číslo
            resultLauncher.launch(intent)
        }
    }
}

