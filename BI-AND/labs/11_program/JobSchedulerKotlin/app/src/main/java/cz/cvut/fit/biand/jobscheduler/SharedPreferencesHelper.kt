package cz.cvut.fit.biand.jobscheduler

import android.content.Context
import android.content.SharedPreferences

import androidx.lifecycle.LiveData

/**
 * Helper for storing data to SharedPreferences
 */
class SharedPreferencesHelper(context: Context) {

    companion object {

        private const val SP_NAME = "prefs"
        private const val DATA_KEY = "data"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    private val data: Long
        get() = sharedPreferences.getLong(DATA_KEY, 0)

    fun setTimestamp(timestamp: Long) {
        this.sharedPreferences.edit().putLong(DATA_KEY, timestamp).apply()
    }

    fun observeData(): LiveData<Long> {
        return object : LiveData<Long>() {

            private var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

            override fun onActive() {
                super.onActive()
                value = this@SharedPreferencesHelper.data
                onSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    if (key == DATA_KEY) {
                        value = data
                    }
                }
                sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
            }

            override fun onInactive() {
                super.onInactive()
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
            }
        }
    }
}
