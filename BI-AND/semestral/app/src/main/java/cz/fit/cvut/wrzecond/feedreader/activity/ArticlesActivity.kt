package cz.fit.cvut.wrzecond.feedreader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cz.fit.cvut.wrzecond.feedreader.R

class ArticlesActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)
    }

    override fun onCreateOptionsMenu (menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected (item: MenuItem)
        = when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.action_sync_settings -> {
                val syncSettingsIntent = Intent(this, SynchronizationSettingsActivity::class.java)
                startActivity(syncSettingsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}