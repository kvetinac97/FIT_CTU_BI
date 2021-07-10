package cz.wrzecond.restsys.order;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.List;
import java.util.Locale;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import wrzecond.order_food.TjvOrderFoodReadDTO;

public class OrderFoodAdapter extends Adapter<TjvOrderFoodReadDTO> {

    // Constructors
    public OrderFoodAdapter (Activity<TjvOrderFoodReadDTO> activity, @NonNull List<TjvOrderFoodReadDTO> dtos) {
        super (activity, R.layout.item_order_food, dtos);
    }
    public OrderFoodAdapter (Activity<TjvOrderFoodReadDTO> activity, @StringRes int errorResId) {
        super (activity, R.layout.item_order_food, errorResId);
    }

    @Override
    protected Adapter<TjvOrderFoodReadDTO>.ViewHolder createViewHolder (View view) {
        return new OrderFoodAdapter.ViewHolder(view);
    }

    protected class ViewHolder extends Adapter<TjvOrderFoodReadDTO>.ViewHolder {

        // === Properties
        private TextView orderFoodName;
        private TextView orderFoodCount;
        private TextView orderFoodPrice;
        private View delimiterLine;

        public ViewHolder (@NonNull View view) { super(view); }

        @Override
        protected void initView (@NonNull View view) {
            orderFoodName = view.findViewById(R.id.order_food_name);
            orderFoodCount = view.findViewById(R.id.order_food_count);
            orderFoodPrice = view.findViewById(R.id.order_food_price);
            delimiterLine = view.findViewById(R.id.order_food_line);
        }

        @Override
        protected void update ( TjvOrderFoodReadDTO dto ) {
            orderFoodName.setText(dto.getFood().getName());
            orderFoodCount.setText(String.format(Locale.getDefault(), "%dx", dto.getCount()));
            orderFoodPrice.setText(String.format(Locale.getDefault(), "%d,-", dto.getFood().getPrice()));

            // Total
            if (dto.getOrder() == -1) {
                orderFoodName.setTypeface(Typeface.DEFAULT_BOLD);
                orderFoodCount.setVisibility(View.GONE);
                orderFoodPrice.setTypeface(Typeface.DEFAULT_BOLD);
                delimiterLine.setVisibility(View.GONE);
            }
        }

    }

}
