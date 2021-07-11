package cz.fit.cvut.wrzecond.feedreader

import android.app.AlarmManager
import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.room.Room
import cz.fit.cvut.wrzecond.feedreader.room.AppDatabase
import cz.fit.cvut.wrzecond.feedreader.room.AppRepository
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed
import cz.fit.cvut.wrzecond.feedreader.service.FeedDownloader
import kotlinx.coroutines.*

class App : Application() {

    lateinit var appRepository: AppRepository
    val applicationScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    override fun onCreate () {
        super.onCreate()

        database = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
        appRepository = AppRepository(database.feedDao(), database.articleDao())

        val preferences = getSharedPreferences(REPOSITORY_PREFERENCES, MODE_PRIVATE)
        if ( preferences.getBoolean(REPOSITORY_LOADED, false) )
            return

        // === 3. kontrolni bod
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val networkType = if (sharedPreferences.getBoolean(DOWNLOAD_INTERVAL_PREFERENCE_KEY, false)) JobInfo.NETWORK_TYPE_UNMETERED else JobInfo.NETWORK_TYPE_ANY
        val job = JobInfo.Builder(DOWNLOAD_JOB_ID, ComponentName(this, FeedDownloader::class.java))
            .setPeriodic(DOWNLOAD_INTERVAL)
            .setRequiredNetworkType(networkType)
            .setPersisted(true) // important !
            .build()

        Log.d("FeedReader", "Scheduling DOWNLOAD_JOB with interval $DOWNLOAD_INTERVAL, network type $networkType")
        val scheduler = applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(job)
        // === 3. kontrolni bod

        preferences.edit().putBoolean(REPOSITORY_LOADED, true).apply()

        applicationScope.launch {
            withContext(Dispatchers.Default) {
                appRepository.insertFeed(Feed("http://android-developers.blogspot.com/atom.xml"))
                appRepository.insertFeed(Feed("https://novinky.cz/rss"))
            }
        }
    }

    companion object {
        const val REPOSITORY_PREFERENCES = "REPOSITORY_PREFERENCES"
        const val REPOSITORY_LOADED = "REPOSITORY_LOADED"

        const val DOWNLOAD_JOB_ID = 128
        const val DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES
        const val DOWNLOAD_INTERVAL_PREFERENCE_KEY = "synchronization_wifi"

        lateinit var database: AppDatabase
        private set

        fun get (context: Context) = context.applicationContext as App
    }

}