package cz.wrzecond.restsys.employee;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.CreateListener;
import wrzecond.employee.TjvEmployeeCreateDTO;
import wrzecond.employee.TjvEmployeeReadDTO;

public class EmployeeCreateListener extends CreateListener<TjvEmployeeReadDTO> {

    private Boolean adminValue = null;

    public EmployeeCreateListener (EmployeeActivity activity, Dialog dialog, TjvEmployeeReadDTO dto) { super(activity, dialog, dto); }

    @Override
    public void onShow (DialogInterface di) {
        // Init dialog
        EditText username = dialog.findViewById(R.id.create_employee_username);
        EditText password = dialog.findViewById(R.id.create_employee_password);

        EditText firstName = dialog.findViewById(R.id.create_employee_first_name);
        EditText lastName = dialog.findViewById(R.id.create_employee_last_name);
        SwitchCompat admin = dialog.findViewById(R.id.create_employee_admin);
        admin.setOnCheckedChangeListener( (v, checked) -> adminValue = checked );

        if ( dto != null ) {
            username.setText(dto.getUsername());
            firstName.setText(dto.getFirstName());
            lastName.setText(dto.getLastName());
            admin.setChecked(dto.getAdmin());

            Button button = dialog.findViewById(R.id.create_button);
            button.setText(R.string.button_edit);
        }
        else
            adminValue = false; // pre-fill

        // Perform
        dialog.findViewById(R.id.create_button).setOnClickListener( view -> {
            // Create DTO
            TjvEmployeeCreateDTO dto = new TjvEmployeeCreateDTO(
                username.getText().toString(),
                password.getText().toString().isEmpty() ? null : password.getText().toString(),

                firstName.getText().toString(),
                lastName.getText().toString(),
                adminValue
            );
            scheduleRequest(dto, "/employees/");
        });
    }

}
