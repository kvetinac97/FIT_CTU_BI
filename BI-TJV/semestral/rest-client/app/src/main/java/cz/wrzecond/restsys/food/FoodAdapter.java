package cz.wrzecond.restsys.food;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;

import java.util.List;
import java.util.Locale;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.Adapter;
import cz.wrzecond.restsys.allergen.AllergenActivity;
import wrzecond.food.TjvFoodReadDTO;

public class FoodAdapter extends Adapter<TjvFoodReadDTO> {

    // Properties
    private FoodActivity activity;
    private final boolean selection;

    // Constructors
    public FoodAdapter (FoodActivity activity, @NonNull List<TjvFoodReadDTO> dtos, boolean selection) {
        super (activity, R.layout.item_food, dtos);
        this.activity = activity;
        this.selection = selection;
    }
    public FoodAdapter (FoodActivity activity, @StringRes int errorResId) {
        super (activity, R.layout.item_food, errorResId);
        this.activity = activity;
        this.selection = false;
    }

    @Override
    protected Adapter<TjvFoodReadDTO>.ViewHolder createViewHolder (View view) {
        return new ViewHolder(view);
    }

    protected class ViewHolder extends Adapter<TjvFoodReadDTO>.ViewHolder {

        // === Properties
        private View view;
        private TextView foodName;
        private TextView foodPrice;
        private TextView foodCount;

        private TextView allergenText;
        private AppCompatImageButton allergenInfo;

        private ImageView foodEdit;
        private ImageView foodDelete;

        public ViewHolder(@NonNull View view) { super(view); }

        @Override
        protected void initView (@NonNull View view) {
            this.view = view;
            this.foodName = view.findViewById(R.id.food_name);
            this.foodPrice = view.findViewById(R.id.food_price);
            this.foodCount = view.findViewById(R.id.food_count);

            this.allergenText = view.findViewById(R.id.allergen_text);
            this.allergenInfo = view.findViewById(R.id.allergen_info);

            this.foodEdit = view.findViewById(R.id.food_edit);
            this.foodDelete = view.findViewById(R.id.food_delete);
        }

        @Override
        protected void update ( @NonNull TjvFoodReadDTO dto ) {
            foodName.setText(dto.getName());
            foodPrice.setText(String.format(Locale.getDefault(), "%d,-", dto.getPrice()));

            if (dto.getAllergens().size() > 0) {
                StringBuilder builder = new StringBuilder(activity.getString(R.string.food_allergens) + ":");
                dto.getAllergens().forEach( i -> { builder.append(" "); builder.append(i); } );
                allergenText.setText(builder.toString());
            }
            else {
                allergenText.setVisibility(View.GONE);
                allergenInfo.setVisibility(View.GONE);
            }

            // Launch allergen info
            allergenInfo.setOnClickListener( v -> {
                Intent intent = new Intent(activity, AllergenActivity.class);
                intent.putExtra(AllergenActivity.EXTRA_FOOD_ID, dto.getId());
                activity.startActivity(intent);
            } );

            // Selection - select food on click
            if ( selection ) {
                view.setOnClickListener( v -> {
                    // Selected, add one
                    if ( activity.selectedFood.containsKey(dto.getId()) ) {
                        Integer originalCount = activity.selectedFood.get(dto.getId());
                        if (originalCount == null)
                            return;

                        activity.selectedFood.put(dto.getId(), originalCount + 1);
                        foodCount.setText(String.format(Locale.getDefault(), "%dx", originalCount + 1));
                    }
                    else {
                        activity.selectedFood.put(dto.getId(), 1);
                        foodCount.setVisibility(View.VISIBLE);
                        foodCount.setText(String.format(Locale.getDefault(), "%dx", 1));
                        foodDelete.setVisibility(View.VISIBLE);
                        v.setBackgroundColor(Color.argb(127, 0, 255, 0));
                    }

                    activity.findViewById(R.id.fab).setVisibility(activity.selectedFood.isEmpty() ? View.GONE : View.VISIBLE);
                } );

                foodDelete.setOnClickListener( v -> {
                    // Decrement by one
                    if ( activity.selectedFood.containsKey(dto.getId()) ) {
                        Integer originalCount = activity.selectedFood.get(dto.getId());
                        if (originalCount == null)
                            return;

                        if (originalCount == 1) {
                            activity.selectedFood.remove(dto.getId());
                            view.setBackgroundColor(Color.rgb(255, 255, 255));
                            foodCount.setVisibility(View.GONE);
                            foodDelete.setVisibility(View.INVISIBLE);
                        }
                        else {
                            activity.selectedFood.put(dto.getId(), originalCount - 1);
                            foodCount.setText(String.format(Locale.getDefault(), "%dx", originalCount - 1));
                        }
                    }

                    activity.findViewById(R.id.fab).setVisibility(activity.selectedFood.isEmpty() ? View.GONE : View.VISIBLE);
                } );

                // Already pre-selected
                if ( activity.selectedFood.containsKey(dto.getId()) ) {
                    foodCount.setVisibility(View.VISIBLE);
                    foodCount.setText(String.format(Locale.getDefault(), "%dx", activity.selectedFood.get(dto.getId())));

                    foodEdit.setVisibility(View.GONE);
                    foodDelete.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(Color.argb(127, 0, 255, 0));
                }
                else {
                    foodEdit.setVisibility(View.GONE);
                    foodDelete.setVisibility(View.INVISIBLE);
                }

                activity.findViewById(R.id.fab).setVisibility(activity.selectedFood.isEmpty() ? View.GONE : View.VISIBLE);
                return;
            }

            // Only for admins
            if ( !activity.getService().isAdmin() || activity.getIntent() == null ||
                    !activity.getIntent().hasExtra(FoodActivity.EXTRA_SHOW_NAVIGATION) ) {
                foodEdit.setVisibility(View.GONE);
                foodDelete.setVisibility(View.INVISIBLE);
                return;
            }

            foodEdit.setOnClickListener( v -> {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(R.layout.create_food)
                    .create();

                dialog.setOnShowListener(activity.createListener(dialog, dto));
                dialog.show();
            } );

            foodDelete.setOnClickListener( v ->
                new AlertDialog.Builder(activity)
                    .setTitle(R.string.food_delete)
                    .setMessage(activity.getString(R.string.food_delete_confirm, dto.getName()))
                    .setNegativeButton(R.string.button_dismiss, null)
                    .setPositiveButton(R.string.button_delete, ((dialog, which) -> {
                        // Schedule request
                        HttpRequest request = new HttpRequest("/food/" + dto.getId(), HttpMethod.DELETE);
                        activity.getService().scheduleTask( (response) -> {
                            // Show message and close
                            Toast.makeText(activity,
                                response.getStatus().isSuccess() ? activity.getString(R.string.delete_success) :
                                    activity.getString(R.string.delete_failure, response.getStatus().name()),
                            Toast.LENGTH_SHORT).show();

                            // Hide and reload list if success
                            dialog.dismiss();
                            if (response.getStatus().isSuccess())
                                activity.loadList();
                        }, request );
                })).create().show()
            );
        }

    }

}
