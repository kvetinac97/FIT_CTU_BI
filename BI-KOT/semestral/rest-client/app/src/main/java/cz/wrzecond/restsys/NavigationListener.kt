package cz.wrzecond.restsys

import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.wrzecond.restsys.employee.EmployeeActivity
import cz.wrzecond.restsys.food.FoodActivity
import cz.wrzecond.restsys.table.TableActivity
import wrzecond.IReadDTO

class NavigationListener<T: IReadDTO>(private val activity: Activity<T>, selectedMenuItem: Int)
: BottomNavigationView.OnNavigationItemSelectedListener {

    init {
        // Logged in
        if (activity.service.isLoggedIn) {
            val view = activity.findViewById<BottomNavigationView>(R.id.navigation)

            if (!activity.service.isAdmin)
                view.menu.removeItem(R.id.navigation_employees)

            view.selectedItemId = selectedMenuItem
            view.setOnNavigationItemSelectedListener(this)
            view.visibility = View.VISIBLE
        }
        else
            activity.logout()
    }

    override fun onNavigationItemSelected (item: MenuItem) = when(item.itemId) {
        R.id.navigation_tables -> {
            activity.startActivity(Intent(activity, TableActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            true
        }
        R.id.navigation_food -> {
            activity.startActivity(Intent(activity, FoodActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(FoodActivity.EXTRA_SHOW_NAVIGATION, true))
            true
        }
        R.id.navigation_employees -> {
            activity.startActivity(Intent(activity, EmployeeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            true
        }
        R.id.navigation_logout -> {
            activity.logout()
            true
        }
        else -> false
    }

}