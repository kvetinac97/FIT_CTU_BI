package cz.wrzecond.restsys.allergen

import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.Activity
import wrzecond.TjvAllergenReadDTO
import java.lang.reflect.Type

class AllergenActivity : Activity<TjvAllergenReadDTO>() {

    override val endpoint : String
        get() = if (intent.hasExtra(EXTRA_FOOD_ID)) "/food/allergens/${intent.getIntExtra(EXTRA_FOOD_ID, -1)}"
                else "/allergens/"

    override val dtoListType: Type
        get() = object : TypeToken<List<TjvAllergenReadDTO>>(){}.type

    override fun createAdapter (dtos: List<TjvAllergenReadDTO>)
        = AllergenAdapter (this, dtos = dtos)

    override fun createAdapter (errorResId: Int)
        = AllergenAdapter (this, errorResId = errorResId)

    companion object {
        const val EXTRA_FOOD_ID = "FOOD_ID"
    }

}