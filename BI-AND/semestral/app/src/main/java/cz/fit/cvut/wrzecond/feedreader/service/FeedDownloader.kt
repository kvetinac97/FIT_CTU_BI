package cz.fit.cvut.wrzecond.feedreader.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput
import cz.fit.cvut.wrzecond.feedreader.App
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.room.AppRepository
import kotlinx.coroutines.*
import java.io.InputStreamReader
import java.net.URL

// === 3. kontrolni bod ===
class FeedDownloader : JobService() {

    private lateinit var repository: AppRepository
    private var downloader: Job? = null

    override fun onCreate () {
        super.onCreate()

        val app = App.get(applicationContext)
        repository = app.appRepository
    }

    override fun onStartJob (params: JobParameters?) : Boolean {
        // Nothing to do
        if (downloader != null) return false

        Log.d("FeedReader", "DOWNLOAD_JOB executing, ID ${params?.jobId}")
        repository.feedUrlList.observeForever (object: Observer<List<String>> {
            override fun onChanged (urls: List<String>) {
                if (urls.isEmpty() && params?.jobId == App.DOWNLOAD_JOB_ID) return
                repository.feedUrlList.removeObserver(this)
                repository.phase.value = AppRepository.PHASE.DOWNLOADING
                downloader = download(urls, params)
            }
        })
        return true
    }

    override fun onStopJob  (params: JobParameters?) : Boolean {
        val downloader = downloader ?: return true
        downloader.cancel()

        Log.d("FeedReader", "DOWNLOAD_JOB stopping, ID ${params?.jobId}")
        this.downloader = null
        return true
    }

    fun download (urls: List<String>, params: JobParameters?) = App.get(applicationContext).applicationScope.launch {
        withContext(Dispatchers.Default) {
            Log.d("FeedReader", "Performing download")
            SystemClock.sleep(2000)
            var error = false
            urls.forEach { url ->
                if ( !isActive ) {
                    jobFinished(params, true)
                    return@withContext
                }

                val input = SyndFeedInput()
                try {
                    val feed = input.build(InputStreamReader(URL(url).openStream()))
                    repository.insertSyndFeed(url, feed)
                }
                catch ( xc: Exception ) { error = true; xc.printStackTrace() }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, if (error) R.string.download_failure else R.string.download_success, Toast.LENGTH_SHORT).show()
                repository.phase.value = if (error) AppRepository.PHASE.FAILURE else AppRepository.PHASE.SUCCESS
                Log.d("FeedReader", "Download ended")
            }
            downloader = null
            jobFinished(params, true)
        }
    }

}
// === 3. kontrolni bod