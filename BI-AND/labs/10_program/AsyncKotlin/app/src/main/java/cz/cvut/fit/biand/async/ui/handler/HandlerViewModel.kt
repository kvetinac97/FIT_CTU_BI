package cz.cvut.fit.biand.async.ui.handler

import android.os.Handler
import android.os.Looper
import android.os.Message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.async.model.FibonacciComputer

/**
 * ViewModel of an Handler screen
 */
class HandlerViewModel : ViewModel() {

    companion object {
        private const val PROGRESS_KEY = 1
        private const val RESULT_KEY = 2
    }

    private val progressLiveData = MutableLiveData<Boolean>()
    val progress : LiveData<Boolean> = progressLiveData

    private val resultLiveData = MutableLiveData<Int>()
    val result : LiveData<Int> = resultLiveData

    private val handler = Handler(Looper.getMainLooper()) {
        // 2 zpracujte zpravy a pres LiveData predejte aktivite data
        when (it.what) {
            PROGRESS_KEY -> progressLiveData.value = (it.arg1 == 1)
            RESULT_KEY -> resultLiveData.value = it.arg1
        }
        true
    }

    // 1 Za pomoci Threadu provedte vypocet fibonacciho cisla. Pro komunikaci
    // s ui vlaknem pouzijte Handler.
    // Uzitecne metody: Handler.obtainMesasge(what, arg1, arg2), Handler.sendMessage(message)
    fun computeFor (number: Int) = Thread {
        handler.sendMessage(handler.obtainMessage(PROGRESS_KEY, 1, 42))
        val result = FibonacciComputer().forNumber(number)
        handler.sendMessage(handler.obtainMessage(RESULT_KEY, result, 42))
        handler.sendMessage(handler.obtainMessage(PROGRESS_KEY, 0, 42))
    }.start()

}
