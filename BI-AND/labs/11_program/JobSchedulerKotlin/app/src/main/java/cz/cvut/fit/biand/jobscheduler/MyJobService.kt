package cz.cvut.fit.biand.jobscheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

/**
 * Job service that manages saving to shared preferences
 */
class MyJobService : JobService() {

    companion object {
        private const val TAG = "MyJobService"
    }

    override fun onStartJob (params: JobParameters) : Boolean {
        Log.d(TAG, "onStartJob() called with: params = [$params]")
        // [3] implementujte ulozeni timestampu skrz SharedPreferencesHelper.
        SharedPreferencesHelper(this).setTimestamp(System.currentTimeMillis())
        return false
    }

    override fun onStopJob (params: JobParameters) = false
}
