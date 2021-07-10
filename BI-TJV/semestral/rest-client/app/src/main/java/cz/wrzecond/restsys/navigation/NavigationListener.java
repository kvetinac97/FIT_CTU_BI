package cz.wrzecond.restsys.navigation;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.employee.EmployeeActivity;
import cz.wrzecond.restsys.food.FoodActivity;
import cz.wrzecond.restsys.table.TableActivity;
import wrzecond.IReadDTO;

public class NavigationListener<T extends IReadDTO> implements BottomNavigationView.OnNavigationItemSelectedListener {

    // === Properties
    private Activity<T> activity;

    public NavigationListener ( Activity<T> activity, @IdRes int selectedMenuItem ) {
        this.activity = activity;

        // Not logged in
        if (!activity.getService().isLoggedIn()) {
            activity.logout();
            return;
        }

        // Logged in, show everything (not employees - cannot be managed by non-admin)
        BottomNavigationView view = activity.findViewById(R.id.navigation);
        if (!activity.getService().isAdmin())
            view.getMenu().removeItem(R.id.navigation_employees);

        view.setSelectedItemId(selectedMenuItem);
        view.setOnNavigationItemSelectedListener(this);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        // Menu
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navigation_tables:
                intent = new Intent(activity, TableActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                return true;
            case R.id.navigation_food:
                intent = new Intent(activity, FoodActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(FoodActivity.EXTRA_SHOW_NAVIGATION, true);
                activity.startActivity(intent);
                return true;
            case R.id.navigation_employees:
                intent = new Intent(activity, EmployeeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                return true;
            case R.id.navigation_logout:
                activity.logout();
                return true;
        }
        return false;
    }

}
