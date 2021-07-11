package cz.cvut.fit.recyclerview

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class CustomItemKeyProvider<VH : RecyclerView.ViewHolder>(internal var recycler: RecyclerView, internal var adapter: RecyclerView.Adapter<VH>) : ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {

    override fun getKey(position: Int): Long? {
        return adapter.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val viewHolder = recycler.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
    }
}