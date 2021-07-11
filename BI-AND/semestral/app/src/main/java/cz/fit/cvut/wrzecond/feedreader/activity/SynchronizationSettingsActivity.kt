package cz.fit.cvut.wrzecond.feedreader.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.fragment.SynchronizationSettingsFragment

/**
 * Helper activity for synchronization settings
 */
class SynchronizationSettingsActivity : AppCompatActivity() {
    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SynchronizationSettingsFragment())
            .commit()
    }
}