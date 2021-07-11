package cz.cvut.fit.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), FragmentA.FragmentAListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [1] osetri aby se fragmenty vkladaly pouze pokud nejsou jiz ulozeny v bundlu
        val fragmentBHeadline = getString(R.string.fragment_b_headline)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container_a, FragmentA.newInstance())
                .add(R.id.container_b, FragmentB.newInstance(fragmentBHeadline))
                .commit()
        }
    }

    override fun fillHeadline() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container_b) as FragmentB?
        fragment?.changeHeadline(getString(R.string.fragment_b_headline))
    }

    override fun clearHeadline() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container_b) as FragmentB?
        fragment?.changeHeadline("")
    }

    override fun removeB() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container_b) as FragmentB?
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }
}
