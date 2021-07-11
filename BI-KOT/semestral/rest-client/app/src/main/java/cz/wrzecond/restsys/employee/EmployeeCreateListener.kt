package cz.wrzecond.restsys.employee

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import cz.wrzecond.restsys.CreateListener
import cz.wrzecond.restsys.R
import wrzecond.TjvEmployeeReadDTO
import wrzecond.TjvEmployeeUpdateDTO

class EmployeeCreateListener (activity: EmployeeActivity, dialog: Dialog, dto: TjvEmployeeReadDTO?)
: CreateListener<TjvEmployeeReadDTO> (activity, dialog, dto) {

    private var adminValue: Boolean? = null

    override fun onShow (di: DialogInterface) {
        // Init dialog
        val username = dialog.findViewById<EditText>(R.id.create_employee_username)
        val password = dialog.findViewById<EditText>(R.id.create_employee_password)

        val firstName = dialog.findViewById<EditText>(R.id.create_employee_first_name)
        val lastName = dialog.findViewById<EditText>(R.id.create_employee_last_name)
        val admin: SwitchCompat = dialog.findViewById(R.id.create_employee_admin)
        admin.setOnCheckedChangeListener { _, checked -> adminValue = checked }

        if (dto != null) {
            username.setText(dto.username)
            firstName.setText(dto.firstName)
            lastName.setText(dto.lastName)
            admin.isChecked = dto.admin
            val button = dialog.findViewById<Button>(R.id.create_button)
            button.setText(R.string.button_edit)
        }
        else
            adminValue = false // pre-fill

        // Perform
        dialog.findViewById<View>(R.id.create_button).setOnClickListener {
            // Create DTO
            val dto = TjvEmployeeUpdateDTO (
                username.text.toString(),
                if (password.text.toString().isEmpty()) null else password.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                adminValue
            )
            scheduleRequest(dto, "/employees/")
        }
    }

}