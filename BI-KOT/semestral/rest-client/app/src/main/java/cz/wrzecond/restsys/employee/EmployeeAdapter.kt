package cz.wrzecond.restsys.employee

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.databinding.ItemEmployeeBinding
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvEmployeeReadDTO
import java.util.*

class EmployeeAdapter (activity: EmployeeActivity, dtos: List<TjvEmployeeReadDTO>? = null, errorResId: Int? = null)
: Adapter<TjvEmployeeReadDTO>(activity, dtos, errorResId) {

    override fun createViewHolder (parent: ViewGroup)
        = ViewHolder(ItemEmployeeBinding.inflate(activity.layoutInflater, parent, false))

    inner class ViewHolder (private val binding: ItemEmployeeBinding) : Adapter<TjvEmployeeReadDTO>.ViewHolder(binding.root) {
        override fun update (dto: TjvEmployeeReadDTO) {
            // Show name
            binding.employeeName.text = String.format(Locale.getDefault(), "%s %s", dto.firstName, dto.lastName)

            // Only for admin
            if (!activity.service.isAdmin) {
                binding.employeeEdit.visibility = View.GONE
                binding.employeeDelete.visibility = View.GONE
            }

            // Edit employee
            binding.employeeEdit.setOnClickListener {
                AlertDialog.Builder(activity)
                    .setView(R.layout.create_employee)
                    .create()
                    .apply { setOnShowListener(activity.createListener(this, dto)) }
                    .show()
            }

            // No self-delete
            if (dto.id == activity.service.loggedUserID) {
                binding.employeeDelete.visibility = View.INVISIBLE
                return
            }

            // Delete employee
            binding.employeeDelete.setOnClickListener {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.employee_delete)
                    .setMessage(activity.getString(R.string.employee_delete_confirm, dto.firstName, dto.lastName))
                    .setNegativeButton(R.string.button_dismiss, null)
                    .setPositiveButton(R.string.button_delete) { di,_ ->
                        val request = HttpRequest("/employees/${dto.id}", HttpMethod.DELETE)
                        activity.service.scheduleTask(request) { response ->
                            Toast.makeText(activity, response.getToastString(activity), Toast.LENGTH_SHORT).show()
                            di.dismiss()
                            if (response.status.isSuccess)
                                activity.loadList()
                        }
                    }
                    .create()
                    .show()
            }
        }
    }

}