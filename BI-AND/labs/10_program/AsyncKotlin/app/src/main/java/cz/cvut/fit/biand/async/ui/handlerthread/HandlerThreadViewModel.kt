package cz.cvut.fit.biand.async.ui.handlerthread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fit.biand.async.model.FibonacciComputer

/**
 * ViewModel of an HandlerThread screen
 */
class HandlerThreadViewModel : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = progressLiveData

    private val resultLiveData = MutableLiveData<Int>()
    val result: LiveData<Int> = resultLiveData

    private val uiHandler = Handler(Looper.getMainLooper()) {
        // 3 zpracujte zpravy pro hlavni vlakno a pres LiveData predejte data aktivite
        when (it.what) {
            PROGRESS_KEY -> progressLiveData.value = (it.arg1 == 1)
            RESULT_KEY -> resultLiveData.value = it.arg1
        }
        true
    }

    // 1 inicializujte handlerThread a computingHandler. Nezapomente nastartovat Handler vlakno.
    private val handlerThread: HandlerThread = HandlerThread(THREAD_NAME).apply { start() }
    private val computingHandler: Handler = Handler(handlerThread.looper) {
        if (it.what != COMPUTE_KEY) return@Handler false
        uiHandler.sendMessage(uiHandler.obtainMessage(PROGRESS_KEY, 1, 42))
        val result = FibonacciComputer().forNumber(it.arg1)
        uiHandler.sendMessage(uiHandler.obtainMessage(RESULT_KEY, result, 42))
        uiHandler.sendMessage(uiHandler.obtainMessage(PROGRESS_KEY, 0, 42))
        true
    }

    fun computeFor(number: Int) {
        // 2 poslete compute handleru zpravu, ze ma zacit pocitat. Cislo mu muzete poslat pres arg1 zpravy
        computingHandler.sendMessage(computingHandler.obtainMessage(COMPUTE_KEY, number, 42))
    }

    companion object {
        // helper thread name
        private const val THREAD_NAME = "GARGAMEL"

        // ui handler constants
        private const val PROGRESS_KEY = 1
        private const val RESULT_KEY = 2

        // compute handler constants
        private const val COMPUTE_KEY = 3
    }

}
