package cz.wrzecond.restsys.food

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.SwitchCompat
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.CreateListener
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvAllergenReadDTO
import wrzecond.TjvFoodReadDTO
import wrzecond.TjvFoodUpdateDTO
import java.util.*

class FoodCreateListener (activity: FoodActivity, dialog: Dialog, dto: TjvFoodReadDTO?)
: CreateListener<TjvFoodReadDTO>(activity, dialog, dto) {

    // Properties
    private var cookedValue: Boolean? = null
    private var allergensSet: Boolean = false
    private var selection: List<Int>? = null

    override fun onShow (di: DialogInterface) {
        val name = dialog.findViewById<EditText>(R.id.create_food_name)
        val price = dialog.findViewById<EditText>(R.id.create_food_price)

        val allergens = dialog.findViewById<MultiSelectionSpinner<TjvAllergenReadDTO>>(R.id.create_food_allergens)
        val progressBar = dialog.findViewById<ProgressBar>(R.id.items_loading)

        // Load allergen list (async)
        activity.service.scheduleTask(HttpRequest("/allergens/", HttpMethod.GET)) { response ->
            val allergenList = mutableListOf<TjvAllergenReadDTO>()

            if (response.status.isSuccess) {
                try { allergenList.addAll(Gson().fromJson(response.responseBody, object : TypeToken<List<TjvAllergenReadDTO>>(){}.type)) }
                catch (exception: JsonIOException) { exception.printStackTrace() }
            }

            allergens.setItems(allergenList)
            allergensSet = true
            if (selection != null)
                allergens.setSelection(selection)

            progressBar.visibility = View.GONE
        }

        val cooked = dialog.findViewById<SwitchCompat>(R.id.create_food_cooked)
        cooked.setOnCheckedChangeListener { _,checked -> cookedValue = checked }

        val button = dialog.findViewById<Button>(R.id.create_button)

        // Set data
        if (dto != null) {
            name.setText(dto.name)
            price.setText(String.format(Locale.getDefault(), "%d", dto.price))
            cooked.isChecked = dto.cooked

            if (allergensSet)
                allergens.setSelection(dto.allergens)
            else
                selection = dto.allergens

            button.text = activity.getString(R.string.button_edit)
        }
        else
            cookedValue = false

        // Perform action
        button.setOnClickListener {
            val priceInt = price.text.toString().toIntOrNull()

            // Invalid price
            if (priceInt == null) {
                price.error = activity.getString(R.string.error_food_price)
                return@setOnClickListener
            }

            // Create
            scheduleRequest(TjvFoodUpdateDTO(name.text.toString(), priceInt,
                cookedValue, allergens.selectedItems.map { it.id }), "/food/")
        }
    }

}