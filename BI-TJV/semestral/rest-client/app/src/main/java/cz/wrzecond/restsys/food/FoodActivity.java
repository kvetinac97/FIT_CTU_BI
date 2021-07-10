package cz.wrzecond.restsys.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.CreateListener;
import cz.wrzecond.restsys.login.LoginActivity;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.order_food.TjvOrderFoodCreateDTO;

public class FoodActivity extends Activity<TjvFoodReadDTO> {

    public static final String EXTRA_SHOW_NAVIGATION = "SHOW_NAVIGATION";
    public static final String EXTRA_SELECTION = "SELECTION";
    public static final String EXTRA_SELECTION_TABLE = "SELECTION_TABLE";
    public static final String EXTRA_SELECTION_MAP = "SELECTION_MAP";

    public Map<Integer, Integer> selectedFood = new HashMap<>();
    public static final int SELECTION_RESULT_CODE = 631;

    @Override
    protected void onCreate (@Nullable Bundle si) {
        super.onCreate(si);

        // Logged view
        if ( service.isLoggedIn() && getIntent() != null && getIntent().hasExtra(EXTRA_SHOW_NAVIGATION) ) {
            loadNavigation(R.id.navigation_food, R.layout.create_food);
            return;
        }

        // Not logged
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_SELECTION_TABLE)) {
            fab.setImageResource(R.drawable.ic_confirm_button);

            if ( getIntent().hasExtra(EXTRA_SELECTION_MAP) )
                selectedFood = new Gson().fromJson(getIntent().getStringExtra(EXTRA_SELECTION_MAP), new TypeToken<Map<Integer, Integer>>(){}.getType());

            fab.setOnClickListener( v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTION, new Gson().toJson(selectedFood.keySet()
                    .stream()
                    .map(foodId -> new TjvOrderFoodCreateDTO(null, foodId, null, selectedFood.get(foodId)))
                    .collect(Collectors.toList())
                ));
                resultIntent.putExtra(EXTRA_SELECTION_TABLE, getIntent().getIntExtra(EXTRA_SELECTION_TABLE, -1));
                setResult(RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(0, 0);
            } );
            return;
        }

        fab.setImageResource(R.drawable.ic_login_button);
        fab.setOnClickListener( v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }

    @Override
    protected String getEndpoint () {
        return "/food/";
    }

    @Override
    protected Type getDtoListType() {
        return new TypeToken<List<TjvFoodReadDTO>>(){}.getType();
    }

    @Override
    protected Adapter<TjvFoodReadDTO> createAdapter (List<TjvFoodReadDTO> dtos) {
        return new FoodAdapter(this, dtos, getIntent() != null && getIntent().hasExtra(EXTRA_SELECTION_TABLE));
    }

    @Override
    protected Adapter<TjvFoodReadDTO> createAdapter (@StringRes int errorResId) {
        return new FoodAdapter(this, errorResId);
    }

    @Override
    public CreateListener<TjvFoodReadDTO> createListener (AlertDialog dialog, TjvFoodReadDTO dto) {
        dialog.setTitle(dto != null ? getString(R.string.food_edit, dto.getName()) :
                getString(R.string.food_create));
        return new FoodCreateListener(this, dialog, dto);
    }

}
