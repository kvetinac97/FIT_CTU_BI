package cz.wrzecond.restsys.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.service.NetworkService;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.task.HttpStatus;
import cz.wrzecond.restsys.table.TableActivity;
import wrzecond.employee.TjvEmployeeReadDTO;

public class LoginActivity extends AppCompatActivity {

    private NetworkService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.service = new NetworkService(this);

        // Login check
        if (service.isLoggedIn()) {
            openTableActivity();
            return;
        }

        // Not logged in, continue
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener( v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            HttpRequest request = new HttpRequest("/employees/login", HttpMethod.GET);
            request.addHeader("Username", usernameEditText.getText().toString());
            request.addHeader("Password", passwordEditText.getText().toString());

            service.scheduleTask(response -> runOnUiThread(() -> {
                loadingProgressBar.setVisibility(View.GONE);

                if (!response.getStatus().isSuccess()) {
                    showLoginFailed(response.getStatus());
                    return;
                }

                try {
                    TjvEmployeeReadDTO dto = new Gson().fromJson(response.getResponseBody(), TjvEmployeeReadDTO.class);
                    service.setEmployee(dto, passwordEditText.getText().toString());
                    updateUiWithUser(dto);
                }
                catch (Exception jsonException) {
                    jsonException.printStackTrace();
                    showLoginFailed(HttpStatus.UNAVAILABLE);
                }

            }), request);
        } );

        findViewById(R.id.go_back).setOnClickListener( v -> onBackPressed() );

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                loginButton.callOnClick();
            return false;
        });
    }

    private void openTableActivity () {
        Intent intent = new Intent(this, TableActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(0, 0);
    }

    private void updateUiWithUser(TjvEmployeeReadDTO dto) {
        try {
            Toast.makeText(this, getString(R.string.login_success, dto.getFirstName(),
                    dto.getLastName(), dto.getId()), Toast.LENGTH_LONG).show();
            openTableActivity();
        }
        catch (JsonParseException exception) {
            exception.printStackTrace();
            showLoginFailed(HttpStatus.UNAVAILABLE);
        }
    }

    private void showLoginFailed (HttpStatus error) {
        Toast.makeText(getApplicationContext(), getString(R.string.login_failure, error.name()), Toast.LENGTH_SHORT).show();
    }

}