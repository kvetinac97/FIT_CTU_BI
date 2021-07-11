package cz.wrzecond.restsys

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wrzecond.IReadDTO

abstract class Adapter<T: IReadDTO> (protected val activity: Activity<T>,
    private val dtos: List<T>?, private val errorResId: Int?) : RecyclerView.Adapter<Adapter<T>.ViewHolder>() {

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int) : ViewHolder {
        if (errorResId != null) {
            val errorView = TextView(activity).apply { 
                layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setText(errorResId)
                textSize = 25f
                setPaddingRelative(0, 25, 0, 0)
                setTextColor(Color.rgb(255, 0, 0))
            }
            return ErrorViewHolder(errorView)
        }
        return createViewHolder(parent)
    }

    override fun onBindViewHolder (holder: ViewHolder, position: Int) {
        if (dtos != null)
            holder.update(dtos[position])
    }

    override fun getItemViewType (position: Int) = position
    override fun getItemCount() = dtos?.size ?: 1
    
    protected abstract fun createViewHolder (parent: ViewGroup) : ViewHolder

    // ViewHolder for regular data
    abstract inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        abstract fun update (dto: T)
    }

    // ViewHolder for the special error view
    private inner class ErrorViewHolder (view: View) : ViewHolder(view) {
        override fun update(dto: T) {} // this function will never actually get called
    }

}
