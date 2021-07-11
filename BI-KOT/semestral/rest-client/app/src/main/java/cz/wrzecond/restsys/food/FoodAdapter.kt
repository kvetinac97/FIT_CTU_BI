package cz.wrzecond.restsys.food

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.allergen.AllergenActivity
import cz.wrzecond.restsys.databinding.ItemFoodBinding
import cz.wrzecond.restsys.task.HttpMethod
import cz.wrzecond.restsys.task.HttpRequest
import wrzecond.TjvFoodReadDTO
import java.util.*

class FoodAdapter (activity: FoodActivity, dtos: List<TjvFoodReadDTO>? = null,
val selecting: Boolean = false, errorResId: Int? = null) : Adapter<TjvFoodReadDTO>(activity, dtos, errorResId) {

    override fun createViewHolder (parent: ViewGroup)
        = ViewHolder(ItemFoodBinding.inflate(activity.layoutInflater, parent, false))

    inner class ViewHolder (private val binding: ItemFoodBinding) : Adapter<TjvFoodReadDTO>.ViewHolder(binding.root) {
        override fun update (dto: TjvFoodReadDTO) {
            // Basic information
            binding.foodName.text = dto.name
            binding.foodPrice.text = String.format(Locale.getDefault(), "%d,-", dto.price)

            // Allergen list
            if (dto.allergens.isNotEmpty()) {
                val sb = StringBuilder(activity.getString(R.string.food_allergens) + ":")
                dto.allergens.forEach { sb.append(" $it") }
                binding.allergenText.text = sb.toString()
            }
            else {
                binding.allergenText.visibility = View.GONE
                binding.allergenInfo.visibility = View.GONE
            }

            // Allergen info
            binding.allergenInfo.setOnClickListener {
                val intent = Intent(activity, AllergenActivity::class.java)
                        .putExtra(AllergenActivity.EXTRA_FOOD_ID, dto.id)
                activity.startActivity(intent)
            }

            // Select food on click
            if ( selecting ) {
                initSelection(dto)
                return
            }

            // Basic table view
            if ( !activity.service.isAdmin || !activity.intent.hasExtra(FoodActivity.EXTRA_SHOW_NAVIGATION) ) {
                binding.foodEdit.visibility = View.GONE
                binding.foodDelete.visibility = View.INVISIBLE
                return
            }

            // Admin view (edit + delete enabled)
            binding.foodEdit.setOnClickListener {
                AlertDialog.Builder(activity)
                    .setView(R.layout.create_food)
                    .create()
                    .apply { setOnShowListener(activity.createListener(this, dto)) }
                    .show()
            }
            binding.foodDelete.setOnClickListener {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.food_delete)
                    .setMessage(activity.getString(R.string.food_delete_confirm, dto.name))
                    .setNegativeButton(R.string.button_dismiss, null)
                    .setPositiveButton(R.string.button_delete) { di,_ ->
                        val request = HttpRequest("/food/${dto.id}", HttpMethod.DELETE)
                        activity.service.scheduleTask(request) { response ->
                            Toast.makeText(activity, response.getToastString(activity), Toast.LENGTH_SHORT).show()
                            di.dismiss()
                            if (response.status.isSuccess)
                                activity.loadList()
                        }
                    }
                    .create()
                    .show()
            }
        }
        private fun initSelection (dto: TjvFoodReadDTO) {
            val activity = activity as FoodActivity

            // Add one
            binding.root.setOnClickListener {
                // Add one
                val originalCount = activity.selectedFood.getOrDefault(dto.id, 0)
                activity.selectedFood[dto.id] = originalCount + 1

                binding.foodCount.visibility = View.VISIBLE
                binding.foodCount.text = String.format(Locale.getDefault(), "%dx", originalCount + 1)

                binding.foodDelete.visibility = View.VISIBLE
                it.setBackgroundColor(Color.argb(127, 0, 255, 0))
                activity.findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
            }

            // Remove one
            binding.foodDelete.setOnClickListener {
                // Cannot decrement if null
                val originalCount = activity.selectedFood[dto.id] ?: return@setOnClickListener

                // Remove
                if (originalCount == 1) {
                    activity.selectedFood.remove(dto.id)
                    binding.root.setBackgroundColor(Color.rgb(255, 255, 255))
                    binding.foodCount.visibility = View.GONE
                    binding.foodDelete.visibility = View.INVISIBLE
                }
                // Just decrement
                else {
                    activity.selectedFood[dto.id] = originalCount - 1
                    binding.foodCount.text = String.format(Locale.getDefault(), "%dx", originalCount - 1)
                }

                activity.findViewById<FloatingActionButton>(R.id.fab).visibility = if (activity.selectedFood.isEmpty()) View.GONE else View.VISIBLE
            }

            val selectedCount = activity.selectedFood[dto.id]

            // Already selected
            if (selectedCount != null) {
                binding.foodCount.visibility = View.VISIBLE
                binding.foodCount.text = String.format(Locale.getDefault(), "%dx", selectedCount)
                binding.root.setBackgroundColor(Color.argb(127, 0, 255, 0))
            }

            binding.foodEdit.visibility = View.GONE
            binding.foodDelete.visibility = if (selectedCount != null) View.VISIBLE else View.INVISIBLE
            activity.findViewById<FloatingActionButton>(R.id.fab).visibility = if (activity.selectedFood.isEmpty()) View.GONE else View.VISIBLE
        }
    }

}