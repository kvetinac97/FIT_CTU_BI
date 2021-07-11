package cz.cvut.fit.recyclerview

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import cz.cvut.fit.recyclerview.dummy.DummyContent

class ActorListFragment : Fragment() {

    private val handler = Handler()

    private lateinit var tracker: SelectionTracker<Long>

    private var actionMode: ActionMode? = null

    private var adapter: ActorAdapter? = null

    private val actionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.actors_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menu_delete -> {
                    // [3] pomocí hotové metody adapteru smažte označené položky, ukončete ActionMode a zrušte označení položek
                    adapter?.deleteSelectedItems(tracker)
                    mode.finish()
                    tracker.clearSelection()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_actor_list, container, false)

        adapter = ActorAdapter(DummyContent.ACTOR_ENTITIES) { _: Int, item: DummyContent.ActorEntity ->
            // [6] reagujte na klik pouze, poukud není contextual action menu aktivní
            if (actionMode == null)
                Toast.makeText(activity, item.toString(), Toast.LENGTH_SHORT).show()
        }

        // [4] zobrazte místo seznamu mřížku o dvou sloupcích
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.adapter = adapter

        tracker = SelectionTracker.Builder(
                "actorSelection",
                recyclerView,
                CustomItemKeyProvider(recyclerView, adapter!!),
                ActorItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build()

        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                val activity = activity
                val selectedCount = tracker.selection.size()

                if (selectedCount > 0) {
                    if (actionMode == null && activity != null) {
                        // [5] spusťte contextual action mode
                        actionMode = activity.startActionMode(actionModeCallback)
                    }
                } else {
                    if (actionMode != null) {
                        actionMode!!.finish()
                    }
                }
            }
        })

        adapter!!.setSelectionTracker(tracker)

        val swipeLayout = view.findViewById<View>(R.id.swipe_to_refresh) as SwipeRefreshLayout
        swipeLayout.setOnRefreshListener {
            // Your code to refresh the list should be here.
            // dummy async operation
            handler.postDelayed({ swipeLayout.isRefreshing = false }, 1500)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        tracker.onRestoreInstanceState(savedInstanceState)
    }

    companion object {
        fun newInstance(): ActorListFragment {
            return ActorListFragment()
        }
    }
}
