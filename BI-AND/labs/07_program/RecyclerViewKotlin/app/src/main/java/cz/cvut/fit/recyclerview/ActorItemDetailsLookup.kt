package cz.cvut.fit.recyclerview

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ActorItemDetailsLookup (private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    override fun getItemDetails (event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        return if (view != null) {
            (recyclerView.getChildViewHolder(view) as ActorAdapter.ViewHolder)
                    .itemDetails
        } else null
    }

}
