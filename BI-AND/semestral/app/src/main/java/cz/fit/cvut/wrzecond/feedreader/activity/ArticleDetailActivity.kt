package cz.fit.cvut.wrzecond.feedreader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.fragment.ArticleDetailFragment

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null)
            return

        val adf = ArticleDetailFragment.newInstance(intent.getLongExtra(ArticleDetailFragment.ARG_ARTICLE_ID, -1))
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, adf)
            .commitNow()
    }

    override fun onOptionsItemSelected (item: MenuItem) = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}