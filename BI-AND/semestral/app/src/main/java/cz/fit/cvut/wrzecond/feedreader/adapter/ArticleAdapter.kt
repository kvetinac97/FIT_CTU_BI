package cz.fit.cvut.wrzecond.feedreader.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.room.entity.Article

class ArticleAdapter (private val inflater: LayoutInflater, val listener: (Article) -> Unit) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    // Article list property
    var articleList = listOf<Article>()
    set (list) {
        field = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int)
        = ViewHolder(inflater.inflate(R.layout.item_article, parent, false))

    override fun onBindViewHolder (holder: ViewHolder, position: Int)
        = holder.update(articleList[position])

    override fun getItemCount() = articleList.size

    inner class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.article_title)
        private val text  = view.findViewById<TextView>(R.id.article_text)

        fun update (article: Article) {
            title.text = @Suppress("deprecation") Html.fromHtml(article.title)
            text.text  = article.short
            view.setOnClickListener { listener(article) }
        }
    }

}