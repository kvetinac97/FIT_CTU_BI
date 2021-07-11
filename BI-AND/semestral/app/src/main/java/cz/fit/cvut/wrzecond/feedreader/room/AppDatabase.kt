package cz.fit.cvut.wrzecond.feedreader.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.fit.cvut.wrzecond.feedreader.room.dao.ArticleDAO
import cz.fit.cvut.wrzecond.feedreader.room.dao.FeedDAO
import cz.fit.cvut.wrzecond.feedreader.room.entity.Article
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed

@Database(entities = [Feed::class, Article::class], version = AppDatabase.DATABASE_VERSION, exportSchema = false)
@TypeConverters(AppConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun feedDao () : FeedDAO
    abstract fun articleDao () : ArticleDAO

    companion object {
        const val DATABASE_NAME = "FeedReader"
        const val DATABASE_VERSION = 1
    }

}
