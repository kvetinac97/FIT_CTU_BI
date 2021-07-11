package cz.cvut.fit.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var numberTextView: TextView
    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                number = result.data?.getIntExtra(SlaveActivity.EXTRA_NUMBER, 0) ?: 0
                showNumber(number)
            }
        }

        val onClickListener = View.OnClickListener { v ->
            when (v.id) {
                R.id.minus -> showNumber(--number)
                R.id.plus -> showNumber(++number)
                R.id.slave -> SlaveActivity.start(this@MainActivity, number)
                R.id.slave_for_result ->
                    // [2] chceme od SlaveActivity dostat zpět výsledek
                    SlaveActivity.startForResult(this@MainActivity, number, resultLauncher)
            }
        }

        findViewById<View>(R.id.minus).setOnClickListener(onClickListener)
        findViewById<View>(R.id.plus).setOnClickListener(onClickListener)
        findViewById<View>(R.id.slave).setOnClickListener(onClickListener)
        findViewById<View>(R.id.slave_for_result).setOnClickListener(onClickListener)

        numberTextView = findViewById(R.id.number)

        // [1] obnovte stav instanční proměnné number
        number = savedInstanceState?.getInt(INSTANCE_NUMBER) ?: 0
        showNumber(number)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // [1] uložte si hodnotu instanční proměnné number
        outState.putInt(INSTANCE_NUMBER, number)
    }

    private fun showNumber(number: Int) {
        numberTextView.text = number.toString()
    }

    companion object {
        private const val INSTANCE_NUMBER = "number"
    }
}
