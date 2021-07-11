package cz.cvut.fit.viewmodellivedata.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.cvut.fit.viewmodellivedata.R
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import cz.cvut.fit.viewmodellivedata.databinding.IssueItemBinding
import cz.cvut.fit.viewmodellivedata.networking.entity.IssueEntity

internal class IssueAdapter(private val mIssueClickedListener: (IssueEntity) -> Unit) : RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {

    internal var mIssueList: List<IssueEntity>? = null

    init {
        setHasStableIds(true)
    }

    fun setCarList(issueList: List<IssueEntity>) {
        if (mIssueList == null) {
            mIssueList = issueList
            notifyItemRangeInserted(0, issueList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mIssueList!!.size
                }

                override fun getNewListSize(): Int {
                    return issueList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mIssueList!![oldItemPosition].id == issueList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newIssue = issueList[newItemPosition]
                    val oldIssue = mIssueList!![oldItemPosition]
                    return newIssue.id == oldIssue.id
                }
            })
            mIssueList = issueList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val binding = DataBindingUtil
                .inflate<IssueItemBinding>(LayoutInflater.from(parent.context), R.layout.issue_item,
                        parent, false)
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val issue = mIssueList!![position]

        holder.binding.issue = issue
        holder.binding.executePendingBindings()

        holder.itemView.setOnClickListener {
            mIssueClickedListener(issue)
        }
    }

    override fun getItemCount(): Int {
        return if (mIssueList == null) 0 else mIssueList!!.size
    }

    override fun getItemId(position: Int): Long {
        return mIssueList!![position].id
    }

    fun getItem(position: Int): IssueEntity {
        return mIssueList!![position]
    }

    internal class IssueViewHolder(val binding: IssueItemBinding) : RecyclerView.ViewHolder(binding.root)

}
