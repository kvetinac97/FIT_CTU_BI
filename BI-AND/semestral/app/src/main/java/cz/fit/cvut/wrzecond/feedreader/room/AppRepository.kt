package cz.fit.cvut.wrzecond.feedreader.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed
import cz.fit.cvut.wrzecond.feedreader.room.dao.ArticleDAO
import cz.fit.cvut.wrzecond.feedreader.room.dao.FeedDAO
import cz.fit.cvut.wrzecond.feedreader.room.entity.Article
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed
import java.util.*

class AppRepository (private val feedDAO: FeedDAO, private val articleDAO: ArticleDAO) {

    // Feed
    val feedList: LiveData<List<Feed>> = feedDAO.findAll()
    val feedUrlList: LiveData<List<String>> = Transformations.map(feedList) { list ->
        list.map { it.url }
    }

    // Download status
    val phase = MutableLiveData(PHASE.IDLE)

    suspend fun deleteFeed (id: Long)   = feedDAO.delete(id)
    suspend fun insertFeed (feed: Feed) = feedDAO.insert(feed)

    // Article
    val articleList: LiveData<List<Article>> = articleDAO.findAll()
    fun getArticleByID (id: Long) = articleDAO.getByID(id)

    // Common

    suspend fun insertSyndFeed (url: String, syndFeed: SyndFeed) {
        val id = feedDAO.findByUrl(url)
        val feed = Feed(id, url, syndFeed.title ?: "", Date())
        val feedId = when (id) {
            null -> feedDAO.insert(feed)
            else -> {
                feedDAO.update(feed)
                id
            }
        }
        syndFeed.entries.forEach { entryRaw ->
            val entry = entryRaw as? SyndEntry ?: return@forEach
            val articleId = articleDAO.getByLink(entry.link)
            val text = (entry.contents?.getOrNull(0) as? SyndContent)?.value ?: entry.description?.value ?: ""

            val article = Article(articleId, feedId, entry.link ?: "", entry.author ?: "",
                entry.title ?: "", text, entry.updatedDate ?: entry.publishedDate ?: Date())
            if (articleId == null) articleDAO.insert(article) else articleDAO.update(article)
        }
    }

    enum class PHASE { IDLE, DOWNLOADING, SUCCESS, FAILURE }

}