package cz.cvut.fit.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class FragmentA : Fragment() {

    private var mListener: FragmentAListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_a, container, false)

        view.findViewById<View>(R.id.fill).setOnClickListener { mListener?.fillHeadline() }
        view.findViewById<View>(R.id.clear).setOnClickListener { mListener?.clearHeadline() }
        view.findViewById<View>(R.id.remove_b).setOnClickListener {
            mListener?.removeB()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as FragmentAListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FragmentAListener")
        }

    }

    override fun onDetach() {
        super.onDetach()

        // [2] uvolnete referenci na FragmentAListener
        mListener = null
    }

    interface FragmentAListener {
        fun fillHeadline()

        fun clearHeadline()

        fun removeB()
    }

    companion object {

        fun newInstance(): FragmentA {
            return FragmentA()
        }
    }

}