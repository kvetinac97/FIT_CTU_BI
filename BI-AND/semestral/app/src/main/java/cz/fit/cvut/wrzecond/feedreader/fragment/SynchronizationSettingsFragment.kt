package cz.fit.cvut.wrzecond.feedreader.fragment

import android.app.Application
import android.app.job.JobInfo.*
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import cz.fit.cvut.wrzecond.feedreader.App
import cz.fit.cvut.wrzecond.feedreader.App.Companion.DOWNLOAD_INTERVAL_PREFERENCE_KEY
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.service.FeedDownloader

class SynchronizationSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences (savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        // Reschedule job
        if (key == DOWNLOAD_INTERVAL_PREFERENCE_KEY) {
            val scheduler = requireContext().getSystemService(Application.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(App.DOWNLOAD_JOB_ID)

            val job = Builder(App.DOWNLOAD_JOB_ID, ComponentName(requireContext(), FeedDownloader::class.java))
                .setPeriodic(App.DOWNLOAD_INTERVAL)
                .setRequiredNetworkType(if (sharedPreferences.getBoolean(key, false)) NETWORK_TYPE_UNMETERED else NETWORK_TYPE_ANY)
                .setPersisted(true) // important !
                .build()
            scheduler.schedule(job)
        }
    }

}
