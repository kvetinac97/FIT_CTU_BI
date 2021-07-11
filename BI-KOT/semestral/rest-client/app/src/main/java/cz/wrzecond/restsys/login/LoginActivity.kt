package cz.wrzecond.restsys.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonParseException
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.databinding.ActivityLoginBinding
import cz.wrzecond.restsys.service.INetworkService
import cz.wrzecond.restsys.service.NetworkService
import cz.wrzecond.restsys.table.TableActivity
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import cz.wrzecond.restsys.task.HttpStatus
import wrzecond.TjvEmployeeReadDTO
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var service: INetworkService

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = NetworkService(this)

        // Check login
        if (service.isLoggedIn) {
            openTableActivity()
            return
        }

        // Not logged in, continue
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            binding.loading.visibility = View.VISIBLE

            val request = HttpRequest("/employees/login", HttpMethod.GET)
            request.headers["Username"] = binding.username.text.toString()
            request.headers["Password"] = binding.password.text.toString()

            service.scheduleTask(request) { response ->
                binding.loading.visibility = View.GONE

                // Did not log in
                if (!response.status.isSuccess) {
                    showLoginFailed(response.status)
                    return@scheduleTask
                }

                try {
                    val dto = Gson().fromJson(response.responseBody, TjvEmployeeReadDTO::class.java)
                    service.login(dto, binding.password.text.toString())
                    updateUiWithUser(dto)
                }
                catch (e: Exception) { e.printStackTrace(); showLoginFailed(HttpStatus.UNAVAILABLE) }
            }

            binding.goBack.setOnClickListener { onBackPressed() }
            binding.password.setOnEditorActionListener { _,actionId,_ ->
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    binding.login.callOnClick()
                return@setOnEditorActionListener false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    private fun openTableActivity () {
        val intent = Intent(this, TableActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    private fun updateUiWithUser (dto: TjvEmployeeReadDTO) {
        try {
            Toast.makeText(this, getString(R.string.login_success, dto.firstName, dto.lastName, dto.id), Toast.LENGTH_LONG).show()
            openTableActivity()
        }
        catch (e: JsonParseException) {
            e.printStackTrace()
            showLoginFailed(HttpStatus.UNAVAILABLE)
        }
    }

    private fun showLoginFailed (error: HttpStatus)
        = Toast.makeText(this, getString(R.string.login_failure, error.name), Toast.LENGTH_SHORT).show()

}