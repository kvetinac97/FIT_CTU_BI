package cz.wrzecond.restsys.table

import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.databinding.ItemTableBinding
import cz.wrzecond.restsys.food.FoodActivity
import cz.wrzecond.restsys.order.OrderFoodActivity
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvTableReadDTO
import wrzecond.TjvTableType

class TableAdapter (activity: TableActivity, dtos: List<TjvTableReadDTO>? = null, errorResId: Int? = null)
: Adapter<TjvTableReadDTO>(activity, dtos, errorResId)  {

    override fun createViewHolder (parent: ViewGroup)
        = ViewHolder(ItemTableBinding.inflate(activity.layoutInflater, parent, false))

    inner class ViewHolder (private val binding: ItemTableBinding) : Adapter<TjvTableReadDTO>.ViewHolder(binding.root) {
        override fun update (dto: TjvTableReadDTO) {
            // Set image
            binding.tableType.setImageResource( when (dto.type to dto.empty) {
                TjvTableType.BAR     to true  -> R.drawable.ic_bar_table_empty
                TjvTableType.BAR     to false -> R.drawable.ic_bar_table
                TjvTableType.INSIDE  to true  -> R.drawable.ic_inside_table_empty
                TjvTableType.INSIDE  to false -> R.drawable.ic_inside_table
                TjvTableType.OUTSIDE to true  -> R.drawable.ic_outside_table_empty
                TjvTableType.OUTSIDE to false -> R.drawable.ic_outside_table
                else -> -1 // invalid
            })

            // Set ID text
            binding.tableId.text = activity.getString(R.string.table_id, dto.id)
            if (dto.empty)
                binding.tableId.setTextColor(Color.rgb(153, 153, 153))

            // Open table detail
            binding.root.setOnClickListener {
                // Create new order
                if (dto.empty) {
                    val intent = Intent(activity, FoodActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        .putExtra(FoodActivity.EXTRA_SELECTION_TABLE, dto.id)
                    activity.startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE)
                    return@setOnClickListener
                }

                // Show existing order
                val intent = Intent(activity, OrderFoodActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(OrderFoodActivity.EXTRA_TABLE_ID, dto.id)
                activity.startActivity(intent)
            }

            // Edit - delete table (admin only)
            if (!activity.service.isAdmin)
                return

            binding.root.setOnLongClickListener {
                AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.table_id, dto.id))
                    .setMessage(R.string.table_choose_activity)
                    // Show create table dialog
                    .setPositiveButton(R.string.button_edit) { _,_ ->
                        AlertDialog.Builder(activity)
                            .setView(R.layout.create_table)
                            .create()
                            .apply { setOnShowListener(activity.createListener(this, dto)) }
                            .show()
                    }
                    // Perform delete
                    .setNegativeButton(R.string.button_delete) { di,_ ->
                        val request = HttpRequest("/tables/${dto.id}", HttpMethod.DELETE)
                        activity.service.scheduleTask(request) { response ->
                            // Show message and close dialog
                            Toast.makeText(activity, if (response.status.isSuccess) activity.getString(R.string.delete_success)
                                    else activity.getString(R.string.delete_failure, response.status.name), Toast.LENGTH_SHORT).show()

                            // Hide and reload list
                            di.dismiss()
                            if (response.status.isSuccess)
                                activity.loadList()
                        }
                    }
                    // Just hide
                    .setNeutralButton(R.string.button_dismiss) { di,_ -> di.dismiss() }
                    .show()
                true
            }
        }
    }

}