package cz.wrzecond.restsys.order

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.databinding.ItemOrderFoodBinding
import wrzecond.TjvOrderFoodReadDTO
import java.util.*

class OrderFoodAdapter (activity: OrderFoodActivity, dtos: List<TjvOrderFoodReadDTO>? = null, errorResId: Int? = null)
: Adapter<TjvOrderFoodReadDTO>(activity, dtos, errorResId) {

    override fun createViewHolder (parent: ViewGroup)
    = ViewHolder(ItemOrderFoodBinding.inflate(activity.layoutInflater, parent, false))

    inner class ViewHolder (private val binding: ItemOrderFoodBinding) : Adapter<TjvOrderFoodReadDTO>.ViewHolder(binding.root) {
        override fun update (dto: TjvOrderFoodReadDTO) {
            // Set basic data
            binding.orderFoodName.text = dto.food.name
            binding.orderFoodCount.text = String.format(Locale.getDefault(), "%dx", dto.count)
            binding.orderFoodPrice.text = String.format(Locale.getDefault(), "%d,-", dto.food.price)

            // Special - sum field
            if (dto.order == -1) {
                binding.orderFoodName.typeface = Typeface.DEFAULT_BOLD
                binding.orderFoodPrice.typeface = Typeface.DEFAULT_BOLD
                binding.orderFoodCount.visibility = View.GONE
                binding.orderFoodLine.visibility = View.GONE
            }
        }
    }

}