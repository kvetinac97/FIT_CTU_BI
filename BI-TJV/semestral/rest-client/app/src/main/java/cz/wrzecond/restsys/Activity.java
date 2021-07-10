package cz.wrzecond.restsys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.lang.reflect.Type;
import java.util.List;

import cz.wrzecond.restsys.navigation.NavigationListener;
import cz.wrzecond.restsys.service.NetworkService;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.food.FoodActivity;
import wrzecond.IReadDTO;

public abstract class Activity<T extends IReadDTO> extends AppCompatActivity {

    // === Properties
    protected NetworkService service;
    public NetworkService getService () {
        return service;
    }

    private SwipeRefreshLayout srl;
    private boolean blockBackPress = false;

    @Override
    protected void onCreate (@Nullable Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_list);
        this.service = new NetworkService(this);

        this.srl = findViewById(R.id.list_swipe);
        srl.setOnRefreshListener(this::loadList);
        loadList();
    }
    protected void loadNavigation ( @IdRes int selectedMenuItem, @LayoutRes int createLayoutRes ) {
        this.blockBackPress = true;
        new NavigationListener<>(this, selectedMenuItem);

        // Create is available only for admin
        if ( !service.isAdmin() )
            return;

        // Create button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener( v -> {
            // Init
            AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(createLayoutRes)
                .create();

            dialog.setOnShowListener(createListener(dialog, null));
            dialog.show();
        });
    }
    public void logout () {
        Intent intent = new Intent(this, FoodActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        service.logout();
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        /* Logged in, block back_press */
        if (blockBackPress)
            return;

        super.onBackPressed();
    }

    public void loadList () {
        HttpRequest request = new HttpRequest(getEndpoint(), HttpMethod.GET);
        service.scheduleTask((response -> {
            // Init
            ProgressBar progressBar = findViewById(R.id.list_loading);
            List<T> dtos = null;

            try {
                dtos = new Gson().fromJson(response.getResponseBody(), getDtoListType());
            }
            catch ( JsonIOException exception ) {
                exception.printStackTrace();
            }

            final List<T> finalDtos = dtos;
            runOnUiThread(() -> {
                RecyclerView recyclerView = findViewById(R.id.list_view);
                recyclerView.setLayoutManager(getLayoutManager(finalDtos == null));
                recyclerView.setAdapter(finalDtos == null ? createAdapter(response.getStatus().getErrorRes()) :
                        ( finalDtos.isEmpty() ? createAdapter(R.string.error_empty_content) : createAdapter(finalDtos) ));
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                srl.setRefreshing(false);
            });
        }), request);
    }

    // Necessary methods
    protected abstract String getEndpoint();
    protected abstract Type getDtoListType();

    protected abstract Adapter<T> createAdapter ( List<T> dtos );
    protected abstract Adapter<T> createAdapter ( @StringRes int errorResId );
    public CreateListener<T> createListener ( AlertDialog dialog, T editDto ) {
        return null;
    }
    protected RecyclerView.LayoutManager getLayoutManager ( boolean error ) {
        return new LinearLayoutManager(this);
    }

}
