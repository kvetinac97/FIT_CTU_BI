package cz.wrzecond.restsys.food

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.wrzecond.restsys.Activity
import cz.wrzecond.restsys.R
import cz.wrzecond.restsys.login.LoginActivity
import wrzecond.TjvFoodReadDTO
import wrzecond.TjvOrderFoodUpdateDTO
import java.lang.reflect.Type

class FoodActivity : Activity<TjvFoodReadDTO>() {

    var selectedFood = HashMap<Int, Int>()

    override fun onCreate (si: Bundle?) {
        super.onCreate(si)

        // Logged in - edit list
        if (service.isLoggedIn && intent.hasExtra(EXTRA_SHOW_NAVIGATION)) {
            loadNavigation(R.id.navigation_food, R.layout.create_food)
            return
        }

        // Init button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE

        // Create table list
        if (intent.hasExtra(EXTRA_SELECTION_TABLE)) {
            fab.setImageResource(R.drawable.ic_confirm_button)

            // something is already selected
            if ( intent.hasExtra(EXTRA_SELECTION_MAP) )
                selectedFood = Gson().fromJson(intent.getStringExtra(EXTRA_SELECTION_MAP), object : TypeToken<Map<Int, Int>>(){}.type)

            // selection action
            fab.setOnClickListener {
                val intent = Intent().apply {
                    putExtra(EXTRA_SELECTION, Gson().toJson(selectedFood.keys.map { TjvOrderFoodUpdateDTO(null, it, null, selectedFood[it]) }))
                    putExtra(EXTRA_SELECTION_TABLE, intent.getIntExtra(EXTRA_SELECTION_TABLE, -1))
                }

                setResult(RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            }

            return
        }

        // Not logged in
        fab.setImageResource(R.drawable.ic_login_button)
        fab.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }
            startActivity(intent)
        }
    }

    override val endpoint : String
        get() = "/food/"

    override val dtoListType: Type
        get() = object : TypeToken<List<TjvFoodReadDTO>>(){}.type

    override fun createAdapter (dtos: List<TjvFoodReadDTO>)
    = FoodAdapter (this, dtos = dtos, selecting = intent.hasExtra(EXTRA_SELECTION_TABLE))

    override fun createAdapter (errorResId: Int)
    = FoodAdapter (this, errorResId = errorResId)

    override fun createListener (dialog: AlertDialog, dto: TjvFoodReadDTO?)
        = FoodCreateListener (this, dialog, dto)
            .apply { dialog.setTitle(dto?.let { getString(R.string.food_edit, it.name) } ?: getString(R.string.food_create)) }

    companion object {
        const val EXTRA_SHOW_NAVIGATION = "SHOW_NAVIGATION"
        const val EXTRA_SELECTION       = "SELECTION"
        const val EXTRA_SELECTION_TABLE = "SELECTION_TABLE"
        const val EXTRA_SELECTION_MAP   = "SELECTION_MAP"
        const val SELECTION_RESULT_CODE = 631
    }

}