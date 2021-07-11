package cz.cvut.fit.biand.async.ui.handler

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.cvut.fit.biand.async.R
import kotlinx.android.synthetic.main.activity_main.*

class HandlerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(HandlerViewModel::class.java)
        viewModel.progress.observe(this, { aBoolean ->
            progress.visibility = if (aBoolean) View.VISIBLE else View.GONE
            txt_result.visibility = if (aBoolean) View.GONE else View.VISIBLE
        })

        viewModel.result.observe(this, { integer -> txt_result.text = String.format("%d", integer) })

        // 3 zajistete, aby se vypocet provedl jen jednou pri prvnim nacteni obrazovky
        if (savedInstanceState == null)
            viewModel.computeFor(45)
    }

}
