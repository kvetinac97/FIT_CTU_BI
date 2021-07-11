package cz.cvut.fit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class FragmentB : Fragment() {

    private lateinit var headlineTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_b, container, false)

        if (arguments != null) {
            val headline = requireNotNull(arguments).getString(ARG_HEADLINE)

            headlineTextView = view.findViewById(R.id.headline)
            headlineTextView.text = headline
        }

        return view
    }

    fun changeHeadline(headline: String) {
        headlineTextView.text = headline
    }

    companion object {
        private const val ARG_HEADLINE = "headline"

        fun newInstance(headline: String): FragmentB {
            // [4] vlozte headline jako argument fragmentu
            val fragment = FragmentB()
            val bundle = Bundle()
            bundle.putString(ARG_HEADLINE, headline)
            fragment.arguments = bundle
            return fragment
        }
    }

}