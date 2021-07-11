package cz.wrzecond.restsys.allergen

import android.view.ViewGroup
import cz.wrzecond.restsys.Adapter
import cz.wrzecond.restsys.databinding.ItemAllergenBinding
import wrzecond.TjvAllergenReadDTO
import java.util.*

class AllergenAdapter (activity: AllergenActivity, dtos: List<TjvAllergenReadDTO>? = null, errorResId: Int? = null)
: Adapter<TjvAllergenReadDTO>(activity, dtos, errorResId) {

    override fun createViewHolder (parent: ViewGroup)
        = ViewHolder(ItemAllergenBinding.inflate(activity.layoutInflater, parent, false))

    inner class ViewHolder (private val binding: ItemAllergenBinding) : Adapter<TjvAllergenReadDTO>.ViewHolder(binding.root) {
        override fun update (dto: TjvAllergenReadDTO) {
            binding.allergenId.text = String.format(Locale.getDefault(), "%d", dto.id)
            binding.allergenName.text = dto.name
        }
    }

}