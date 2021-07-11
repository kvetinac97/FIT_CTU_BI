package cz.cvut.fit.toolbar

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager)
        mViewPager.adapter = mSectionsPagerAdapter

        // Pairing tabs with ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val item = Objects.requireNonNull<Menu>(menu).findItem(R.id.action_share)

        // Fetch and store ShareActionProvider
        val shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        val yourShareText = "Právě sedím na hodině BI-AND a tohle sdílím pomocí mého prvního ShareIntentu!"

        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText(yourShareText).intent
        shareActionProvider.setShareIntent(shareIntent)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this, "Nastavení!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_help -> {
                Toast.makeText(this, "Nápověda!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_add -> {
                Toast.makeText(this, "Přidání!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int) = PlaceholderFragment.newInstance(position + 1)

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence {
            val l = Locale.getDefault()
            when (position) {
                0 -> return getString(R.string.title_section1).toUpperCase(l)
                1 -> return getString(R.string.title_section2).toUpperCase(l)
                2 -> return getString(R.string.title_section3).toUpperCase(l)
            }
            return ""
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            val textView = rootView.findViewById<TextView>(R.id.section_label)
            textView.text = Objects.requireNonNull<Bundle>(arguments).getInt(ARG_SECTION_NUMBER).toString()
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

}
