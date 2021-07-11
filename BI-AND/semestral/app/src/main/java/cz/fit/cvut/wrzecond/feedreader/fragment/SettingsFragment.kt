package cz.fit.cvut.wrzecond.feedreader.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.fit.cvut.wrzecond.feedreader.R
import cz.fit.cvut.wrzecond.feedreader.adapter.FeedAdapter
import cz.fit.cvut.wrzecond.feedreader.room.entity.Feed
import cz.fit.cvut.wrzecond.feedreader.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var feedAdapter: FeedAdapter

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View
        = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedAdapter = FeedAdapter(LayoutInflater.from(activity)) { showDeleteFeedDialog(it) }

        recyclerView = view.findViewById(R.id.feed_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = feedAdapter

        settingsViewModel.feed.observe(viewLifecycleOwner) { feedList ->
            feedAdapter.feedList = feedList
            view.findViewById<TextView>(R.id.feed_list_empty).text = if (feedList.isEmpty()) getString(R.string.feed_list_empty) else null
        }
    }

    override fun onCreateOptionsMenu (menu: Menu, inflater: MenuInflater)
        = inflater.inflate(R.menu.menu_settings, menu)

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected (item: MenuItem)
        = when (item.itemId) {
            R.id.action_add -> showCreateFeedDialog()
            else -> super.onOptionsItemSelected(item)
        }

    private fun showCreateFeedDialog () : Boolean {
        SettingsFragmentDialog.show(R.string.create_feed, null, R.layout.dialog_feed_create,
            null, childFragmentManager)
        return true
    }

    private fun showDeleteFeedDialog (feed: Feed)
    = SettingsFragmentDialog.show(R.string.delete_feed, R.string.delete_feed_message, null,
        feed.id, parentFragmentManager)

    companion object {
        fun newInstance () = SettingsFragment()
    }

}