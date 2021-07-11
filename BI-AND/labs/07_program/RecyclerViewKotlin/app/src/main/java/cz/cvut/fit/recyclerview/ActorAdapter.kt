package cz.cvut.fit.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import cz.cvut.fit.recyclerview.dummy.DummyContent.ActorEntity

class ActorAdapter(private val mValues: MutableList<ActorEntity>, private val mListener: (position: Int, item: ActorEntity) -> Unit) : RecyclerView.Adapter<ActorAdapter.ViewHolder>() {

    private var selectionTracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.actor_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actor = mValues[position]
        val isSelected = selectionTracker!!.isSelected(actor.id)

        holder.mItem = actor
        holder.mName.text = actor.name + " " + actor.surname
        holder.mDescription.text = actor.description

        // [1] doplňte obsluhu kliku volající mListner
        holder.mView.setOnClickListener {
            val item = holder.mItem
            if (item != null)
                mListener(holder.adapterPosition, item)
        }

        holder.mView.isActivated = isSelected
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun getItemId(position: Int): Long {
        return mValues[position].id
    }

    fun setSelectionTracker(selectionTracker: SelectionTracker<Long>) {
        this.selectionTracker = selectionTracker
    }

    fun deleteSelectedItems(tracker: SelectionTracker<Long>) {
        val selection = tracker.selection

        var index = 0
        val i = mValues.iterator()
        while (i.hasNext()) {
            val actor = i.next()
            if (selection.contains(actor.id)) {
                i.remove()
            }

            notifyDataSetChanged()
            index++
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mName: TextView = mView.findViewById(R.id.name)
        // [2] init description
        val mDescription: TextView = mView.findViewById(R.id.description)
        var mItem: ActorEntity? = null

        val itemDetails: ItemDetailsLookup.ItemDetails<Long>
            get() = object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition() = adapterPosition
                override fun getSelectionKey() = itemId
            }

        override fun toString() = super.toString() + " '" + mName.text + "'"
    }

}
