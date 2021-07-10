package cz.wrzecond.restsys.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.util.function.Consumer;

import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.task.HttpResponse;
import cz.wrzecond.restsys.task.NetworkTask;
import wrzecond.employee.TjvEmployeeReadDTO;

public class NetworkService {

    // Properties
    private EmployeeWrapper wrapper = null;
    private static final String EMPLOYEE_SAVE_PATH = "TJV_EMPLOYEE_CREDENTIALS";
    private Context context;

    public NetworkService (Context context) {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE);
        if ( preferences.contains("user") && preferences.contains("password") ) {
            try {
                wrapper = new EmployeeWrapper(
                        new Gson().fromJson(preferences.getString("user", ""), TjvEmployeeReadDTO.class),
                        preferences.getString("password", "")
                );
            }
            catch (JsonIOException exception) { exception.printStackTrace(); }
        }
    }

    public void setEmployee (TjvEmployeeReadDTO dto, String password) {
        wrapper = new EmployeeWrapper(dto, password);
        context.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE)
            .edit()
            .putString("user", new Gson().toJson(dto))
            .putString("password", password)
            .apply();
    }
    public boolean isLoggedIn () {
        return wrapper != null;
    }
    public boolean isAdmin () {
        return isLoggedIn() && wrapper.dto.getAdmin();
    }
    public int getLoggedUserID () { return isLoggedIn() ? wrapper.dto.getId() : -1; }
    public void logout () {
        context.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE)
            .edit().remove("username").remove("password").apply();
    }

    public void scheduleTask (Consumer<HttpResponse> consumer, HttpRequest request) {
        if (wrapper != null) {
            request.addHeader("Username", wrapper.dto.getUsername());
            request.addHeader("Password", wrapper.password);
        }

        new NetworkTask(consumer, request);
    }

    private static class EmployeeWrapper {
        private TjvEmployeeReadDTO dto;
        private String password;

        public EmployeeWrapper (TjvEmployeeReadDTO dto, String password) {
            this.dto = dto;
            this.password = password;
        }
    }

}
