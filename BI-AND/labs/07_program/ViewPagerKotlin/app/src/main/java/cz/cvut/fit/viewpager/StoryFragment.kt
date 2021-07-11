package cz.cvut.fit.viewpager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.thedeanda.lorem.LoremIpsum

// [1] vytvořte fragment, zobrazující dummy text v R.layout.fragment_story

class StoryFragment : Fragment() {

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?
        = inflater.inflate(R.layout.fragment_story, container, false).apply {
            findViewById<TextView>(R.id.text).text = LoremIpsum.getInstance().getParagraphs(2, 10)
        }

    companion object {
        fun newInstance () = StoryFragment()
    }

}
