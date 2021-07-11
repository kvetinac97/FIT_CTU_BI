package cz.wrzecond.restsys.service

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonIOException
import cz.wrzecond.restsys.task.HttpRequest
import cz.wrzecond.restsys.task.HttpResponse
import cz.wrzecond.restsys.task.HttpStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wrzecond.TjvEmployeeReadDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkService (private val activity: AppCompatActivity) : INetworkService {

    // Properties
    private var wrapper: TjvEmployeeWrapper? = null

    override val isAdmin: Boolean
        get() = wrapper?.dto?.admin ?: false

    override val isLoggedIn: Boolean
        get() = wrapper != null

    override val loggedUserID: Int?
        get() = wrapper?.dto?.id

    init {
        val preferences = activity.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE)
        val dtoStr = preferences.getString(USER_SAVE_PATH, null)
        val pasStr = preferences.getString(PASS_SAVE_PATH, null)

        // Try login
        if (dtoStr != null && pasStr != null) {
            try {
                val dto = Gson().fromJson(dtoStr, TjvEmployeeReadDTO::class.java)
                wrapper = TjvEmployeeWrapper(dto, pasStr)
            }
            catch (e: JsonIOException) { e.printStackTrace() }
        }
    }

    override fun login (dto: TjvEmployeeReadDTO, password: String) {
        wrapper = TjvEmployeeWrapper(dto, password)
        activity.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE)
            .edit()
            .putString(USER_SAVE_PATH, Gson().toJson(dto))
            .putString(PASS_SAVE_PATH, password)
            .apply()
    }

    override fun logout () {
        wrapper = null
        activity.getSharedPreferences(EMPLOYEE_SAVE_PATH, Context.MODE_PRIVATE)
            .edit()
            .remove(USER_SAVE_PATH)
            .remove(PASS_SAVE_PATH)
            .apply()
    }

    override fun scheduleTask (request: HttpRequest, action: (HttpResponse) -> Unit) {
        // If logged in, edit request
        val wrapper = wrapper
        if (wrapper != null) {
            request.headers["Username"] = wrapper.dto.username
            request.headers["Password"] = wrapper.password
        }

        // Perform request
        activity.lifecycleScope.launch {
            withContext(Dispatchers.IO) { // network thread
                Log.d("RestSys", "${request.method} $SERVER_URL${request.url} ${request.body}")

                val response = try {
                    // Init connection
                    val connection = URL("$SERVER_URL${request.url}").openConnection() as HttpURLConnection
                    connection.useCaches = false
                    connection.requestMethod = request.method.name

                    // Init headers
                    request.headers.forEach { (k, v) -> connection.setRequestProperty(k, v) }

                    // Init body
                    val body = if (request.body != null && !request.body.isEmpty()) request.body else null
                    body?.let {
                        connection.doOutput = true
                        connection.addRequestProperty("Content-Type", "application/json")
                        val os = connection.outputStream
                        os.write(body.toByteArray())
                        os.flush()
                    }

                    // Init output
                    val output = if (connection.responseCode == HttpStatus.OK.code) {
                        val br = BufferedReader(InputStreamReader(connection.inputStream))
                        val sb = StringBuilder()

                        var tmp: String?
                        do {
                            tmp = br.readLine()
                            tmp?.let { sb.append(it) }
                        }
                        while (tmp != null)

                        sb.toString()
                    } else null

                    // Init response
                    HttpResponse (
                        HttpStatus.fromCode(connection.responseCode),
                        connection.getHeaderField("Location"),
                        output
                    ).apply { connection.disconnect() }
                } catch (e: Exception) { HttpResponse(HttpStatus.UNAVAILABLE, null, null) }

                Log.d("RestSys", "$response")

                // Call action on UI thread
                withContext(Dispatchers.Main) {
                    action(response)
                }
            }
        }
    }

    // Helper data class
    private data class TjvEmployeeWrapper (val dto: TjvEmployeeReadDTO, val password: String)

    // Constants
    companion object {
        const val EMPLOYEE_SAVE_PATH = "TJV_EMPLOYEE_CREDENTIALS"
        const val USER_SAVE_PATH = "user"
        const val PASS_SAVE_PATH = "password"

        private const val SERVER_URL = "http://dev.justtalk.cz:8080"
    }

}