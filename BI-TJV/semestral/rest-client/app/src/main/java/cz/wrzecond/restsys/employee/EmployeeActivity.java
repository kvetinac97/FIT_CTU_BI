package cz.wrzecond.restsys.employee;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.CreateListener;
import wrzecond.employee.TjvEmployeeReadDTO;

public class EmployeeActivity extends Activity<TjvEmployeeReadDTO> {

    @Override
    protected void onCreate (@Nullable Bundle si) {
        super.onCreate(si);
        loadNavigation(R.id.navigation_employees, R.layout.create_employee);
    }

    @Override
    protected String getEndpoint () {
        return "/employees/";
    }

    @Override
    protected Type getDtoListType() {
        return new TypeToken<List<TjvEmployeeReadDTO>>(){}.getType();
    }

    @Override
    protected Adapter<TjvEmployeeReadDTO> createAdapter (List<TjvEmployeeReadDTO> dtos) {
        return new EmployeeAdapter(this, dtos);
    }

    @Override
    protected Adapter<TjvEmployeeReadDTO> createAdapter (@StringRes int errorResId) {
        return new EmployeeAdapter(this, errorResId);
    }

    @Override
    public CreateListener<TjvEmployeeReadDTO> createListener (AlertDialog dialog, TjvEmployeeReadDTO dto) {
        dialog.setTitle(dto != null ? getString(R.string.employee_edit, dto.getFirstName(), dto.getLastName()) :
                getString(R.string.employee_create));
        return new EmployeeCreateListener (this, dialog, dto);
    }

}
