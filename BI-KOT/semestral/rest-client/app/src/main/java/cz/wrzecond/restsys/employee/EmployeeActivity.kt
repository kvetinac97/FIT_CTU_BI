package cz.wrzecond.restsys.employee

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.Activity
import cz.wrzecond.restsys.R
import wrzecond.TjvEmployeeReadDTO
import java.lang.reflect.Type

class EmployeeActivity : Activity<TjvEmployeeReadDTO>() {

    override fun onCreate (si: Bundle?) {
        super.onCreate(si)
        loadNavigation(R.id.navigation_employees, R.layout.create_employee)
    }

    override val endpoint : String
        get() = "/employees/"

    override val dtoListType: Type
        get() = object : TypeToken<List<TjvEmployeeReadDTO>>(){}.type

    override fun createAdapter (dtos: List<TjvEmployeeReadDTO>)
        = EmployeeAdapter (this, dtos = dtos)

    override fun createAdapter (errorResId: Int)
        = EmployeeAdapter (this, errorResId = errorResId)

    override fun createListener (dialog: AlertDialog, dto: TjvEmployeeReadDTO?)
        = EmployeeCreateListener (this, dialog, dto)
          .apply {
              dialog.setTitle(dto?.let { getString(R.string.employee_edit, it.firstName, it.lastName) } ?:
                getString(R.string.employee_create))
          }

}