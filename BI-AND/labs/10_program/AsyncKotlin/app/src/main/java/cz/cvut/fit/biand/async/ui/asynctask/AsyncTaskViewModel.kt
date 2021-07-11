package cz.cvut.fit.biand.async.ui.asynctask

import android.annotation.SuppressLint
import android.os.AsyncTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.async.model.FibonacciComputer

/**
 * ViewModel of an AsyncTask screen
 */
class AsyncTaskViewModel : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = progressLiveData

    private val resultLiveData = MutableLiveData<Int>()
    val result: LiveData<Int> = resultLiveData

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    fun computeFor (number: Int) {
        // 1 implementujte async task, ktery pred provedenim vypoctu zobrazi progress,
        // a po skonceni vypoctu ho zase skryje.
        FibonacciAsyncTask().execute(number)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    private inner class FibonacciAsyncTask : AsyncTask<Int, Void, Int>() {
        override fun onPreExecute () {
            progressLiveData.value = true
        }
        override fun doInBackground (vararg params: Int?) = FibonacciComputer().forNumber(params.first() ?: 0)
        override fun onPostExecute (result: Int?) {
            resultLiveData.value = result
            progressLiveData.value = false
        }
    }

}
