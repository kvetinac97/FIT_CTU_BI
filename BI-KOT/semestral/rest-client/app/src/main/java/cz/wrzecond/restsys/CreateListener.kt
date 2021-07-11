package cz.wrzecond.restsys

import android.app.Dialog
import android.content.DialogInterface
import android.widget.Toast
import com.google.gson.Gson
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.IReadDTO
import wrzecond.IUpdateDTO

abstract class CreateListener<T : IReadDTO> (protected val activity: Activity<T>,
protected val dialog: Dialog, protected val dto: T?) : DialogInterface.OnShowListener {
    protected fun scheduleRequest (updateDTO: IUpdateDTO, path: String) {
        // Init request
        val request = if (dto == null) HttpRequest(path, HttpMethod.POST, body = Gson().toJson(updateDTO))
        else HttpRequest("$path${dto.id}", HttpMethod.PATCH, body = Gson().toJson(updateDTO))

        // Fire request
        activity.service.scheduleTask(request) { response ->
            // Init message
            val message = when {
                response.status.isSuccess && response.locationHeader != null ->
                    activity.getString(R.string.create_success, response.locationHeader.replace(path, ""))
                response.status.isSuccess -> activity.getString(R.string.edit_success)
                else -> activity.getString(R.string.create_edit_failure, response.status.name)
            }
            // Show message and dismiss
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            dialog.dismiss()

            // Reload on success
            if (response.status.isSuccess)
                activity.loadList()
        }
    }
}