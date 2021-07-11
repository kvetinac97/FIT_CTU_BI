package cz.fit.cvut.wrzecond.feedreader.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import cz.fit.cvut.wrzecond.feedreader.R

/**
 * 3. kontrolni bod
 * (update = prezije otoceni obrazovky)
 */
class SettingsFragmentDialog : DialogFragment() {

    private var listener: SettingsFragmentDialogListener? = null

    override fun onAttach (context: Context) {
        super.onAttach(context)
        Log.d("FeedReader", "Attached to $context")
        listener = context as? SettingsFragmentDialogListener
        Log.d("FeedReader", "Listener is $listener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateDialog (savedInstanceState: Bundle?) : Dialog {
        val view = if (requireArguments().containsKey(BUNDLE_VIEW_ID)) layoutInflater
            .inflate(requireArguments().getInt(BUNDLE_VIEW_ID), null) else null
        val action = DialogInterface.OnClickListener { _,_ ->
            view?.let { listener?.createFeedDialog(it) } ?: listener?.deleteFeedDialog(requireArguments().getLong(BUNDLE_FEED_ID))
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(requireArguments().getInt(BUNDLE_TITLE))
            .apply { if (requireArguments().containsKey(BUNDLE_MESSAGE)) setMessage(requireArguments().getInt(BUNDLE_MESSAGE)) }
            .setPositiveButton(R.string.ok, action)
            .setNegativeButton(R.string.cancel, null)
            .apply { if (view != null) setView(view) }
            .create()
    }

    companion object {

        private const val SETTINGS_DIALOG = "SETTINGS_DIALOG"
        private const val BUNDLE_TITLE   = "SETTINGS_TITLE"
        private const val BUNDLE_MESSAGE = "SETTINGS_MESSAGE"
        private const val BUNDLE_VIEW_ID = "SETTINGS_VIEW_ID"
        private const val BUNDLE_FEED_ID = "BUNDLE_FEED_ID"

        fun show (title: Int, message: Int?, viewId: Int?, feedId: Long?, fragmentManager: FragmentManager) {
            val transaction = fragmentManager.beginTransaction()
            val existing = fragmentManager.findFragmentByTag(SETTINGS_DIALOG)
            if (existing != null)
                transaction.remove(existing)

            val bundle = Bundle()
            bundle.putInt(BUNDLE_TITLE, title)
            if (message != null)
                bundle.putInt(BUNDLE_MESSAGE, message)
            if (viewId != null)
                bundle.putInt(BUNDLE_VIEW_ID, viewId)
            if (feedId != null)
                bundle.putLong(BUNDLE_FEED_ID, feedId)

            val dialog = SettingsFragmentDialog()
            dialog.arguments = bundle
            dialog.show(transaction, SETTINGS_DIALOG)
        }

    }

}

interface SettingsFragmentDialogListener {
    fun createFeedDialog (view: View)
    fun deleteFeedDialog (feedId: Long)
}
