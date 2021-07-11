package cz.wrzecond.restsys.task

import androidx.appcompat.app.AppCompatActivity
import cz.wrzecond.restsys.R

data class HttpResponse (
    val status: HttpStatus,
    val locationHeader: String?,
    val responseBody: String?
) {
    fun getToastString (activity: AppCompatActivity)
    = if (status.isSuccess) activity.getString(R.string.delete_success)
      else activity.getString(R.string.delete_failure, status.name)
}
