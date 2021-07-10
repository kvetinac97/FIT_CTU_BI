package cz.wrzecond.restsys.table;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.food.FoodActivity;
import cz.wrzecond.restsys.order.OrderFoodActivity;
import wrzecond.table.TjvTableReadDTO;
import wrzecond.table.TjvTableType;

public class TableAdapter extends Adapter<TjvTableReadDTO> {

    // Constructors
    public TableAdapter (Activity<TjvTableReadDTO> activity, @NonNull List<TjvTableReadDTO> dtos) {
        super (activity, R.layout.item_table, dtos);
    }
    public TableAdapter (Activity<TjvTableReadDTO> activity, @StringRes int errorResId) {
        super (activity, R.layout.item_table, errorResId);
    }

    @Override
    protected Adapter<TjvTableReadDTO>.ViewHolder createViewHolder (View view) {
        return new TableAdapter.ViewHolder(view);
    }

    protected class ViewHolder extends Adapter<TjvTableReadDTO>.ViewHolder {

        // === Properties
        private View view;
        private ImageView tableType;
        private TextView tableId;

        public ViewHolder (@NonNull View view) { super(view); }

        @Override
        protected void initView (@NonNull View view) {
            this.view = view;
            this.tableType = view.findViewById(R.id.table_type);
            this.tableId = view.findViewById(R.id.table_id);
        }

        @Override
        protected void update ( TjvTableReadDTO dto ) {
            // View data
            tableType.setImageResource (
                dto.getType() == TjvTableType.BAR ? ( dto.isEmpty() ? R.drawable.ic_bar_table_empty : R.drawable.ic_bar_table ) :
                ( dto.getType() == TjvTableType.INSIDE ? ( dto.isEmpty() ? R.drawable.ic_inside_table_empty : R.drawable.ic_inside_table ) :
                ( dto.isEmpty() ? R.drawable.ic_outside_table_empty : R.drawable.ic_outside_table ) )
            );

            tableId.setText(activity.getString(R.string.table_id, dto.getId()));
            if ( dto.isEmpty() )
                tableId.setTextColor(Color.rgb(153, 153, 153));

            // Open table detail
            view.setOnClickListener( v -> {
                // Create new order
                if ( dto.isEmpty() ) {
                    Intent intent = new Intent(activity, FoodActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(FoodActivity.EXTRA_SELECTION_TABLE, dto.getId());
                    activity.startActivityForResult(intent, FoodActivity.SELECTION_RESULT_CODE);
                    return;
                }

                // Show existing
                Intent intent = new Intent(activity, OrderFoodActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(OrderFoodActivity.EXTRA_TABLE_ID, dto.getId());
                activity.startActivity(intent);
            } );

            // Only for admins
            if ( !activity.getService().isAdmin() )
                return;

            view.setOnLongClickListener( view -> {
                new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.table_id, dto.getId()))
                    .setMessage(R.string.table_choose_activity)
                    .setPositiveButton(R.string.button_edit, (di, which) -> {
                        AlertDialog dialog = new AlertDialog.Builder(activity)
                            .setView(R.layout.create_table)
                            .create();

                        dialog.setOnShowListener(activity.createListener(dialog, dto));
                        dialog.show();
                    })
                    .setNegativeButton(R.string.button_delete, (di, which) -> {
                        // Schedule request
                        HttpRequest request = new HttpRequest("/tables/" + dto.getId(), HttpMethod.DELETE);
                        activity.getService().scheduleTask((response) -> {
                            // Show message and close
                            Toast.makeText(activity,
                                response.getStatus().isSuccess() ? activity.getString(R.string.delete_success) :
                                activity.getString(R.string.delete_failure, response.getStatus().name()),
                            Toast.LENGTH_SHORT).show();

                            // Hide and reload list if success
                            di.dismiss();
                            if (response.getStatus().isSuccess())
                                activity.loadList();
                        }, request);
                    })
                    .setNeutralButton(R.string.button_dismiss, (di, which) -> di.dismiss())
                .show();
                return true;
            });
        }

    }

}
