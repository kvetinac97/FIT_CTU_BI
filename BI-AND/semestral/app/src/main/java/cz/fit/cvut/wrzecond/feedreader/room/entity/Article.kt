package cz.fit.cvut.wrzecond.feedreader.room.entity

import android.text.Html
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cz.fit.cvut.wrzecond.feedreader.room.dao.ArticleDAO
import java.util.*

@Entity (
    tableName = ArticleDAO.TABLE,
    foreignKeys = [ForeignKey(
        entity = Feed::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("feed"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("feed")]
)
data class Article (
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val feed: Long,
    val link: String,
    val author: String,
    val title: String,
    val text: String,
    val date: Date // updatedDate
) {
    @Suppress("deprecation")
    val short = Html.fromHtml (
        text
            .replace("(?s)<style[^>]*>.*?</style>".toRegex(), "")
            .replace("(?s)<img.*?/>".toRegex(), "")
    ).toString().run { substring(0, minOf(length, 150)) } + "..."
}
