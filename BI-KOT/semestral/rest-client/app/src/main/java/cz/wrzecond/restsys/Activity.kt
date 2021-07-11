package cz.wrzecond.restsys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonIOException
import cz.wrzecond.restsys.food.FoodActivity
import cz.wrzecond.restsys.service.INetworkService
import cz.wrzecond.restsys.service.NetworkService
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.IReadDTO
import java.lang.reflect.Type

abstract class Activity<T: IReadDTO> : AppCompatActivity() {

    // Abstract properties
    abstract val endpoint: String
    abstract val dtoListType: Type

    // Properties
    private var blockBackPress = false
    private lateinit var srl: SwipeRefreshLayout

    lateinit var service: INetworkService
    private set

    override fun onCreate (si: Bundle?) {
        super.onCreate(si)
        setContentView(R.layout.activity_list)

        srl = findViewById(R.id.list_swipe)
        srl.setOnRefreshListener { loadList() }
        service = NetworkService(this)

        // Initial load
        loadList()
    }

    // Init navigation views
    protected fun loadNavigation (@IdRes selectedMenuItem: Int, @LayoutRes createLayoutRes: Int) {
        // Init basic navigation
        blockBackPress = true
        NavigationListener(this, selectedMenuItem)

        // Create is only for admin
        if (!service.isAdmin)
            return

        // Create button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setView(createLayoutRes)
                .create()
                .apply { setOnShowListener(createListener(this, null)) }
                .show()
        }
    }

    // Log out the current user
    fun logout () {
        val intent = Intent(this, FoodActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        service.logout()
        startActivity(intent)
        finishAffinity()
    }

    override fun onBackPressed () {
        // Back press is not allowed in menu
        if (blockBackPress)
            return
        super.onBackPressed()
    }

    // Load list of food, tables...
    fun loadList () = service.scheduleTask(HttpRequest(endpoint, HttpMethod.GET)) { response ->
        // Load DTOs
        val dtos : List<T>? = try { Gson().fromJson(response.responseBody, dtoListType) }
        catch (e: JsonIOException) { null }

        // Load recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.list_view)
        recyclerView.layoutManager = getLayoutManager(dtos == null)
        recyclerView.adapter = when {
            dtos == null   -> createAdapter(response.status.errorRes)
            dtos.isEmpty() -> createAdapter(R.string.error_empty_content)
            else           -> createAdapter(dtos)
        }
        recyclerView.visibility = View.VISIBLE

        // Hide progress, stop scrolling
        val progressBar = findViewById<ProgressBar>(R.id.list_loading)
        progressBar.visibility = View.GONE
        srl.isRefreshing = false
    }

    // === ABSTRACT / OPEN METHODS ===
    protected abstract fun createAdapter  (dtos: List<T>)              : Adapter<T>
    protected abstract fun createAdapter  (@StringRes errorResId: Int) : Adapter<T>
    open fun createListener (dialog: AlertDialog, dto: T?) : CreateListener<T>? = null
    protected open fun getLayoutManager (error: Boolean) : RecyclerView.LayoutManager
        = LinearLayoutManager(this)
    // === ABSTRACT METHODS ===

}