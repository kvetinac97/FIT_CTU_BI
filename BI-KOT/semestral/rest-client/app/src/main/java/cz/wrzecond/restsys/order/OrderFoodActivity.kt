package cz.wrzecond.restsys.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.Activity
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.food.FoodActivity
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvFoodReadDTO
import wrzecond.TjvOrderFoodReadDTO
import wrzecond.TjvOrderUpdateDTO
import java.lang.reflect.Type
import java.sql.Timestamp

class OrderFoodActivity : Activity<TjvOrderFoodReadDTO>() {

    private var tableId: Int? = null
    private var orderId: Int? = null

    override fun onCreate (si: Bundle?) {
        // Init table ID
        if (intent.hasExtra(EXTRA_TABLE_ID))
            tableId = intent.getIntExtra(EXTRA_TABLE_ID, -1)
        super.onCreate(si)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override val endpoint : String
        get() = if (tableId != null && tableId != -1) "/order-food/at/$tableId" else "/order-food/"

    override val dtoListType: Type
        get() = object : TypeToken<List<TjvOrderFoodReadDTO>>(){}.type

    override fun createAdapter (dtos: List<TjvOrderFoodReadDTO>) : Adapter<TjvOrderFoodReadDTO> {
        // Show edit button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.ic_item_edit)

        orderId = dtos.firstOrNull()?.order
        val leftFab = findViewById<FloatingActionButton>(R.id.left_fab)
        leftFab.visibility = View.VISIBLE
        leftFab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.order_cancel)
                .setMessage(R.string.order_cancel_confirm)
                .setNegativeButton(R.string.button_no, null)
                .setPositiveButton(R.string.button_yes) { _,_ ->
                    service.scheduleTask(HttpRequest("/orders/$orderId", HttpMethod.DELETE)) { response ->
                        // Cancelling order failed
                        if (!response.status.isSuccess) {
                            Toast.makeText(this, getString(R.string.order_cancel_failure,
                                    response.status.name), Toast.LENGTH_SHORT).show()
                            return@scheduleTask
                        }

                        // Cancelling order successful
                        Toast.makeText(this, R.string.order_cancel_success, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
                .show()
        }

        // Prepare map
        val foodCount = HashMap<Int, Int>()
        dtos.forEach { foodCount[it.food.id] = it.count }
        fab.setOnClickListener {
            // Show food list for selection
            val intent = Intent(this, FoodActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra(FoodActivity.EXTRA_SELECTION_TABLE, tableId)
                .putExtra(FoodActivity.EXTRA_SELECTION_MAP, Gson().toJson(foodCount))
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE)
        }

        // Add dummy sum DTO
        val totalPrice = dtos.map { it.count * it.food.price }.sum()
        val newDtos = dtos.toMutableList()
        newDtos.add(TjvOrderFoodReadDTO(-1, -1,
                TjvFoodReadDTO(-1, getString(R.string.order_total), totalPrice, false, listOf()),
                Timestamp(0L), -1))

        // Close order
        val middleFab = findViewById<FloatingActionButton>(R.id.middle_fab)
        middleFab.visibility = View.VISIBLE
        middleFab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.order_close)
                .setMessage(getString(R.string.order_close_message, totalPrice))
                .setNegativeButton(R.string.button_dismiss, null)
                .setPositiveButton(R.string.button_pay) { _,_ ->
                    val request = HttpRequest("/orders/$orderId", HttpMethod.PATCH,
                        body = Gson().toJson(TjvOrderUpdateDTO(null, null, true)))
                    service.scheduleTask(request) { response ->
                        // Completing order failed
                        if (!response.status.isSuccess) {
                            Toast.makeText(this, getString(R.string.order_close_failure,
                                    response.status.name), Toast.LENGTH_SHORT).show()
                            return@scheduleTask
                        }

                        // Closing order successful
                        Toast.makeText(this, R.string.order_close_success, Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
                .show()
        }

        return OrderFoodAdapter(this, dtos = newDtos)
    }

    override fun createAdapter (errorResId: Int)
    = OrderFoodAdapter (this, errorResId = errorResId)

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Invalid
        if (requestCode != FoodActivity.SELECTION_RESULT_CODE || resultCode != RESULT_OK || data == null)
            return

        // Show loading
        val progressBar = findViewById<ProgressBar>(R.id.list_loading)
        progressBar.visibility = View.VISIBLE

        // Send request
        val request = HttpRequest("/order-food/at/$tableId", HttpMethod.POST,
            body = data.getStringExtra(FoodActivity.EXTRA_SELECTION))

        service.scheduleTask(request) { response ->
            // Edit failure
            if (!response.status.isSuccess) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.order_update_failure, response.status.name), Toast.LENGTH_SHORT).show()
                return@scheduleTask
            }

            // Edit success, reload now
            loadList()
        }
    }

    companion object {
        const val EXTRA_TABLE_ID = "TABLE_ID"
    }

}