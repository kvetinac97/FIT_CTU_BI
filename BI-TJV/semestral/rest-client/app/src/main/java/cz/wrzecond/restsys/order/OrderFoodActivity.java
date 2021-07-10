package cz.wrzecond.restsys.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.food.FoodActivity;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order_food.TjvOrderFoodReadDTO;

public class OrderFoodActivity extends Activity<TjvOrderFoodReadDTO> {

    public static final String EXTRA_TABLE_ID = "TABLE_ID";
    private int tableId; private int orderId;

    @Override
    protected void onCreate (@Nullable Bundle si) {
        // Init table ID
        if ( getIntent() != null && getIntent().hasExtra(EXTRA_TABLE_ID) )
            tableId = getIntent().getIntExtra(EXTRA_TABLE_ID, -1);

        super.onCreate(si);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected String getEndpoint () {
        // Order food list
        if ( tableId != -1 )
            return "/order-food/at/" + tableId;

        return "/order-food/";
    }

    @Override
    protected Type getDtoListType() {
        return new TypeToken<List<TjvOrderFoodReadDTO>>(){}.getType();
    }

    @Override
    protected Adapter<TjvOrderFoodReadDTO> createAdapter (List<TjvOrderFoodReadDTO> dtos) {
        // Show plus button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_item_edit);

        orderId = dtos.get(0).getOrder();
        FloatingActionButton leftFab = findViewById(R.id.left_fab);
        leftFab.setVisibility(View.VISIBLE);
        leftFab.setOnClickListener(v ->
            new AlertDialog.Builder(this)
                .setTitle(R.string.order_cancel)
                .setMessage(R.string.order_cancel_confirm)
                .setNegativeButton(R.string.button_no, null)
                .setPositiveButton(R.string.button_yes, (di, which) -> service.scheduleTask(response -> {
                    if (!response.getStatus().isSuccess()) {
                        Toast.makeText(this, getString(R.string.order_cancel_failure,
                                response.getStatus().name()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(this, R.string.order_cancel_success, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }, new HttpRequest("/orders/" + orderId, HttpMethod.DELETE)))
                .show()
        );

        HashMap<Integer, Integer> foodCount = new HashMap<>();
        dtos.forEach( orderFood -> foodCount.put(orderFood.getFood().getId(), orderFood.getCount()) );
        fab.setOnClickListener( v -> {
            Intent intent = new Intent(this, FoodActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(FoodActivity.EXTRA_SELECTION_TABLE, tableId);
            intent.putExtra(FoodActivity.EXTRA_SELECTION_MAP, new Gson().toJson(foodCount));
            startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE);
        } );

        // Add dummy sum DTO
        int totalPrice = dtos.stream().mapToInt(dto -> dto.getCount() * dto.getFood().getPrice()).sum();
        dtos.add( new TjvOrderFoodReadDTO(-1, -1,
                new TjvFoodReadDTO(-1, getString(R.string.order_total), totalPrice, null, null),
                null, -1));

        FloatingActionButton middleFab = findViewById(R.id.middle_fab);
        middleFab.setVisibility(View.VISIBLE);

        middleFab.setOnClickListener( v ->
            new AlertDialog.Builder(this)
                .setTitle(R.string.order_close)
                .setMessage(getString(R.string.order_close_message, totalPrice))
                .setNegativeButton(R.string.button_dismiss, null)
                .setPositiveButton(R.string.button_pay, (di, which) -> {
                    HttpRequest request = new HttpRequest("/orders/" + orderId, HttpMethod.PATCH);
                    request.setBody(new Gson().toJson(new TjvOrderCreateDTO(null, null, true)));
                    service.scheduleTask(response -> {
                        if (!response.getStatus().isSuccess()) {
                            Toast.makeText(this, getString(R.string.order_close_failure,
                                    response.getStatus().name()), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(this, R.string.order_close_success, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }, request);
                })
                .show()
        );
        return new OrderFoodAdapter(this, dtos);
    }

    @Override
    protected Adapter<TjvOrderFoodReadDTO> createAdapter (@StringRes int errorResId) {
        return new OrderFoodAdapter(this, errorResId);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Create new order
        if (requestCode == FoodActivity.SELECTION_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            ProgressBar progressBar = findViewById(R.id.list_loading);
            progressBar.setVisibility(View.VISIBLE);

            // Send request
            HttpRequest request = new HttpRequest("/order-food/at/" + tableId, HttpMethod.POST);
            request.setBody(data.getStringExtra(FoodActivity.EXTRA_SELECTION));
            service.scheduleTask( response -> {
                // Failure
                if ( !response.getStatus().isSuccess() ) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, getString(R.string.order_update_failure, response.getStatus().name()), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Reload this view
                loadList();
            }, request );
        }
    }

}
