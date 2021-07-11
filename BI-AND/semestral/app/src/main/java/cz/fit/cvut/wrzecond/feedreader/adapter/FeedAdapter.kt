package cz.fit.cvut.wrzecond.feedreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed

class FeedAdapter (private val inflater: LayoutInflater, val listener: (Feed) -> Unit) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    // Feed list property
    var feedList = listOf<Feed>()
    set (list) {
        field = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int)
        = ViewHolder(inflater.inflate(R.layout.item_article, parent, false))

    override fun onBindViewHolder (holder: ViewHolder, position: Int)
        = holder.update(feedList[position])

    override fun getItemCount() = feedList.size

    inner class ViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.article_title)
        private val text  = view.findViewById<TextView>(R.id.article_text)

        fun update (feed: Feed) {
            title.text = if (feed.title.isEmpty()) title.context.getString(R.string.feed_title_unknown) else feed.title
            text.text  = feed.url
            view.setOnClickListener { listener(feed) }
        }
    }

}