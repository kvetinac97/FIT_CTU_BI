package cz.wrzecond.restsys.employee;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.Locale;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import wrzecond.employee.TjvEmployeeReadDTO;

public class EmployeeAdapter extends Adapter<TjvEmployeeReadDTO> {

    // Constructors
    public EmployeeAdapter (Activity<TjvEmployeeReadDTO> activity, @NonNull List<TjvEmployeeReadDTO> dtos) {
        super (activity, R.layout.item_employee, dtos);
    }
    public EmployeeAdapter (Activity<TjvEmployeeReadDTO> activity, @StringRes int errorResId) {
        super (activity, R.layout.item_employee, errorResId);
    }

    @Override
    protected Adapter<TjvEmployeeReadDTO>.ViewHolder createViewHolder (View view) {
        return new EmployeeAdapter.ViewHolder(view);
    }

    protected class ViewHolder extends Adapter<TjvEmployeeReadDTO>.ViewHolder {

        // === Properties
        private TextView employeeUsername;
        private TextView employeeName;

        private ImageView employeeEdit;
        private ImageView employeeDelete;

        public ViewHolder (@NonNull View view) { super(view); }

        @Override
        protected void initView (@NonNull View view) {
            this.employeeUsername = view.findViewById(R.id.employee_username);
            this.employeeName = view.findViewById(R.id.employee_name);
            this.employeeEdit = view.findViewById(R.id.employee_edit);
            this.employeeDelete = view.findViewById(R.id.employee_delete);
        }

        @Override
        protected void update ( TjvEmployeeReadDTO dto ) {
            employeeUsername.setText(dto.getUsername());
            employeeName.setText(String.format(Locale.getDefault(), "%s %s",
                    dto.getFirstName(), dto.getLastName()));

            // Only for admins
            if ( !activity.getService().isAdmin() ) {
                employeeEdit.setVisibility(View.GONE);
                employeeDelete.setVisibility(View.INVISIBLE);
                return;
            }

            employeeEdit.setOnClickListener( v -> {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(R.layout.create_employee)
                    .create();

                dialog.setOnShowListener(activity.createListener(dialog, dto));
                dialog.show();
            } );

            // Do not allow self-delete
            if (dto.getId() == activity.getService().getLoggedUserID()) {
                employeeDelete.setVisibility(View.INVISIBLE);
                return;
            }

            employeeDelete.setOnClickListener( v ->
                new AlertDialog.Builder(activity)
                    .setTitle(R.string.employee_delete)
                    .setMessage(activity.getString(R.string.employee_delete_confirm, dto.getFirstName(), dto.getLastName()))
                    .setNegativeButton(R.string.button_dismiss, null)
                    .setPositiveButton(R.string.button_delete, ((dialog, which) -> {
                        // Schedule request
                        HttpRequest request = new HttpRequest("/employees/" + dto.getId(), HttpMethod.DELETE);
                        activity.getService().scheduleTask( (response) -> {
                            // Show message and close
                            Toast.makeText(activity,
                                    response.getStatus().isSuccess() ? activity.getString(R.string.delete_success) :
                                        activity.getString(R.string.delete_failure, response.getStatus().name()),
                                    Toast.LENGTH_SHORT).show();

                            // Hide and reload list if success
                            dialog.dismiss();
                            if (response.getStatus().isSuccess())
                                activity.loadList();
                        }, request );
                    }))
                    .create().show()
            );
        }

    }

}
