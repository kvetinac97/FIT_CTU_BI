package cz.cvut.fit.viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private var mPager: ViewPager? = null

    private var pagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [2] vytvořte pager adapter a nastavte jej pageru
        mPager = findViewById(R.id.pager)
        pagerAdapter = StoryPagerAdapter(supportFragmentManager)
        mPager?.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        // [3] zařiďte, aby tlačítko back vracelo uživatele o jeden slide zpět
        val pager = mPager
        if (pager == null || pager.currentItem == 0) {
            super.onBackPressed()
            return
        }
        --pager.currentItem
    }

    private inner class StoryPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem (position: Int) = StoryFragment.newInstance()
        override fun getCount () = NUM_PAGES
    }

    companion object {
        private const val NUM_PAGES = 5
    }
}
