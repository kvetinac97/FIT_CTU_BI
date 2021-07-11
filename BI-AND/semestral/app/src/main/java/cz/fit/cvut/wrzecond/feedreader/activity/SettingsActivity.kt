package cz.fit.cvut.wrzecond.feedreader.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.fragment.SettingsFragment
import cz.fit.cvut.wrzecond.feedreader.fragment.SettingsFragmentDialogListener
import cz.fit.cvut.wrzecond.feedreader.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity(), SettingsFragmentDialogListener {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.action_settings)

        if (savedInstanceState != null)
            return

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment.newInstance())
            .commitNow()
    }

    override fun onOptionsItemSelected (item: MenuItem) = when(item.itemId) {
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun createFeedDialog (view: View) {
        val feedUrl = view.findViewById<EditText>(R.id.feed_url)
        val url = feedUrl.text.toString()
        if (url.isNotEmpty()) {
            settingsViewModel.createFeed(url)
            Toast.makeText(this, R.string.create_feed_success, Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(this, R.string.empty_url, Toast.LENGTH_SHORT).show()
    }

    override fun deleteFeedDialog (feedId: Long) {
        settingsViewModel.deleteFeed(feedId)
        Toast.makeText(this, R.string.delete_feed_success, Toast.LENGTH_SHORT).show()
    }

}