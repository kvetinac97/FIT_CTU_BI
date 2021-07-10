package cz.wrzecond.restsys.table;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.CreateListener;
import cz.wrzecond.restsys.food.FoodActivity;
import cz.wrzecond.restsys.order.OrderFoodActivity;
import wrzecond.table.TjvTableReadDTO;

public class TableActivity extends Activity<TjvTableReadDTO> {

    private boolean loaded = false;

    @Override
    protected void onCreate (@Nullable Bundle si) {
        super.onCreate(si);
        super.overridePendingTransition(0, 0);

        loadNavigation(R.id.navigation_tables, R.layout.create_table);
        loaded = true;

        FloatingActionButton leftFab = findViewById(R.id.left_fab);
        leftFab.setImageResource(R.drawable.ic_inside_table);
        leftFab.setVisibility(View.VISIBLE);
        leftFab.setOnClickListener( v -> {
            Intent intent = new Intent(this, FoodActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(FoodActivity.EXTRA_SELECTION_TABLE, -2);
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE);
        } );

        FloatingActionButton middleFab = findViewById(service.isAdmin() ? R.id.middle_fab : R.id.fab);
        middleFab.setImageResource(R.drawable.ic_outside_table);
        middleFab.setVisibility(View.VISIBLE);
        middleFab.setOnClickListener( v -> {
            Intent intent = new Intent(this, FoodActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(FoodActivity.EXTRA_SELECTION_TABLE, -3);
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE);
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loaded)
            loadList();
    }

    @Override
    protected String getEndpoint () {
        return "/tables/";
    }

    @Override
    protected Type getDtoListType() {
        return new TypeToken<List<TjvTableReadDTO>>(){}.getType();
    }

    @Override
    protected Adapter<TjvTableReadDTO> createAdapter (List<TjvTableReadDTO> dtos) {
        return new TableAdapter(this, dtos);
    }

    @Override
    protected Adapter<TjvTableReadDTO> createAdapter (@StringRes int errorResId) {
        return new TableAdapter(this, errorResId);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager (boolean error) {
        return error ? super.getLayoutManager(true) : new GridLayoutManager(this, 4);
    }

    @Override
    public CreateListener<TjvTableReadDTO> createListener (AlertDialog dialog, TjvTableReadDTO dto) {
        dialog.setTitle(dto != null ? getString(R.string.table_edit, dto.getId()) : getString(R.string.table_new));
        return new TableCreateListener(this, dialog, dto);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Create new order
        if (requestCode == FoodActivity.SELECTION_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            ProgressBar progressBar = findViewById(R.id.list_loading);
            int tableId = data.getIntExtra(FoodActivity.EXTRA_SELECTION_TABLE, -1);
            progressBar.setVisibility(View.VISIBLE);

            String url = ( tableId >= 0 ? "/order-food/at/" + tableId : (
                tableId == -2 ? "/order-food/type/INSIDE" : "/order-food/type/OUTSIDE"
            ) );

            // Send request
            HttpRequest request = new HttpRequest(url, HttpMethod.POST);
            request.setBody(data.getStringExtra(FoodActivity.EXTRA_SELECTION));
            service.scheduleTask( response -> {
                // Failure
                if ( !response.getStatus().isSuccess() ) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, getString(R.string.table_order_create_failure,
                            response.getStatus().name()), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tableId < 0) {
                    Toast.makeText(this, R.string.table_order_create_success, Toast.LENGTH_SHORT).show();
                    loadList();
                    return;
                }

                // Launch order view
                Intent intent = new Intent(this, OrderFoodActivity.class);
                intent.putExtra(OrderFoodActivity.EXTRA_TABLE_ID, tableId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }, request );
        }
    }

}
