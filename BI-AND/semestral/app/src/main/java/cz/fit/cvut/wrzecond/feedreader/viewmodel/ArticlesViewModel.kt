package cz.fit.cvut.wrzecond.feedreader.viewmodel

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.util.Log
import androidx.lifecycle.*
import cz.fit.cvut.wrzecond.feedreader.App
import cz.fit.cvut.wrzecond.feedreader.room.AppRepository
import cz.fit.cvut.wrzecond.feedreader.service.FeedDownloader
import java.util.concurrent.TimeUnit

// === 3. kontrolni bod (upravy)
class ArticlesViewModel (val app: Application) : AndroidViewModel(app) {

    private val repository = App.get(app).appRepository
    val articles = repository.articleList

    val downloadPhase: LiveData<AppRepository.PHASE>
    get() = repository.phase

    override fun onCleared() = cancelDownload()

    fun startDownload () {
        Log.d("FeedReader", "Starting manual download")
        val job = JobInfo.Builder(DOWNLOAD_REFRESH_JOB, ComponentName(app, FeedDownloader::class.java))
            .setBackoffCriteria(TimeUnit.SECONDS.toMillis(1), JobInfo.BACKOFF_POLICY_EXPONENTIAL)
            .build()
        val scheduler = app.getSystemService(Application.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(job)
    }

    private fun cancelDownload () {
        Log.d("FeedReader", "Cancelling manual download")
        val scheduler = app.getSystemService(Application.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(DOWNLOAD_REFRESH_JOB)
    }

    companion object {
        private const val DOWNLOAD_REFRESH_JOB = 256
    }

}
// === 3. kontrolni bod