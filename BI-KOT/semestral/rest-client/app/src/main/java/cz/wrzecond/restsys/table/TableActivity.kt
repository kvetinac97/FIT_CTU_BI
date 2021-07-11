package cz.wrzecond.restsys.table

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.Activity
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.food.FoodActivity
import cz.wrzecond.restsys.order.OrderFoodActivity
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvTableReadDTO
import java.lang.reflect.Type

class TableActivity : Activity<TjvTableReadDTO>() {

    // Properties
    private var loaded = false

    override fun onCreate (si: Bundle?) {
        super.onCreate(si)
        overridePendingTransition(0, 0)

        // Init navigation
        loadNavigation(R.id.navigation_tables, R.layout.create_table)
        loaded = true

        // Order on all tables inside
        val leftFab = findViewById<FloatingActionButton>(R.id.left_fab)
        leftFab.setImageResource(R.drawable.ic_inside_table)
        leftFab.visibility = View.VISIBLE
        leftFab.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra(FoodActivity.EXTRA_SELECTION_TABLE, TABLE_ID_SPECIAL_ALL_INSIDE)
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE)
        }

        // Order on all tables outside
        val middleFab = findViewById<FloatingActionButton>(R.id.middle_fab)
        middleFab.setImageResource(R.drawable.ic_outside_table)
        middleFab.visibility = View.VISIBLE
        middleFab.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(FoodActivity.EXTRA_SELECTION_TABLE, TABLE_ID_SPECIAL_ALL_OUTSIDE)
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        if (loaded)
            loadList()
    }

    override val endpoint : String
        get() = "/tables/"

    override val dtoListType: Type
        get() = object : TypeToken<List<TjvTableReadDTO>>(){}.type

    override fun createAdapter (dtos: List<TjvTableReadDTO>)
        = TableAdapter (this, dtos = dtos)

    override fun createAdapter (errorResId: Int)
        = TableAdapter (this, errorResId = errorResId)

    override fun getLayoutManager (error: Boolean) : RecyclerView.LayoutManager
        = if (error) super.getLayoutManager(error) else GridLayoutManager(this, 4)

    override fun createListener (dialog: AlertDialog, dto: TjvTableReadDTO?)
        = TableCreateListener (this, dialog, dto)
            .apply {
                dialog.setTitle(dto?.let { getString(R.string.table_edit, it.id) } ?:
                getString(R.string.table_new))
            }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Not interested
        if (requestCode != FoodActivity.SELECTION_RESULT_CODE || resultCode != RESULT_OK || data == null)
            return

        // Show progress
        val progressBar = findViewById<ProgressBar>(R.id.list_loading)
        progressBar.visibility = View.VISIBLE

        // Init data
        val tableId = data.getIntExtra(FoodActivity.EXTRA_SELECTION_TABLE, -1)
        val url = when (tableId) {
            TABLE_ID_SPECIAL_ALL_INSIDE  -> "/order-food/type/INSIDE"
            TABLE_ID_SPECIAL_ALL_OUTSIDE -> "/order-food/type/OUTSIDE"
            else -> "/order-food/at/$tableId"
        }

        // Schedule request
        val request = HttpRequest(url, HttpMethod.POST, body = data.getStringExtra(FoodActivity.EXTRA_SELECTION))
        service.scheduleTask(request) { response ->
            // Creation failure
            if (!response.status.isSuccess) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.table_order_create_failure,
                        response.status.name), Toast.LENGTH_SHORT).show()
                return@scheduleTask
            }

            // Creation (at specific table type) success
            if (tableId == TABLE_ID_SPECIAL_ALL_INSIDE || tableId == TABLE_ID_SPECIAL_ALL_OUTSIDE) {
                Toast.makeText(this, R.string.table_order_create_success, Toast.LENGTH_SHORT).show()
                loadList()
                return@scheduleTask
            }

            // Creation (at specific table) success = show order view
            val intent = Intent(this, OrderFoodActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra(OrderFoodActivity.EXTRA_TABLE_ID, tableId)
            startActivity(intent)
        }
    }

    companion object {
        private const val TABLE_ID_SPECIAL_ALL_INSIDE  = -2
        private const val TABLE_ID_SPECIAL_ALL_OUTSIDE = -3
    }

}