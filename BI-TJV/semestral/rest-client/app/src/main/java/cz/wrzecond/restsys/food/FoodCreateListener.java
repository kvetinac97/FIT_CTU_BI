package cz.wrzecond.restsys.food;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.spinner.MultiSelectionSpinner;
import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.CreateListener;
import wrzecond.allergen.TjvAllergenReadDTO;
import wrzecond.food.TjvFoodCreateDTO;
import wrzecond.food.TjvFoodReadDTO;

public class FoodCreateListener extends CreateListener<TjvFoodReadDTO> {

    private Boolean cookedValue = null;

    private boolean allergensSet = false;
    private List<Integer> selection = null;

    public FoodCreateListener (FoodActivity activity, Dialog dialog, TjvFoodReadDTO dto) { super(activity, dialog, dto); }

    @Override
    public void onShow (DialogInterface di) {
        // Init dialog
        EditText name = dialog.findViewById(R.id.create_food_name);
        EditText price = dialog.findViewById(R.id.create_food_price);

        MultiSelectionSpinner<TjvAllergenReadDTO> allergens = dialog.findViewById(R.id.create_food_allergens);
        ProgressBar progressBar = dialog.findViewById(R.id.items_loading);
        activity.getService().scheduleTask(response -> {
            List<TjvAllergenReadDTO> allergenList = Collections.emptyList();

            if (response.getStatus().isSuccess()) {
                try { allergenList = new Gson().fromJson(response.getResponseBody(), new TypeToken<List<TjvAllergenReadDTO>>(){}.getType()); }
                catch ( JsonIOException exception ) { exception.printStackTrace(); }
            }

            allergens.setItems(allergenList);
            allergensSet = true;
            if (selection != null)
                allergens.setSelection(selection);
            progressBar.setVisibility(View.GONE);
        }, new HttpRequest("/allergens/", HttpMethod.GET));

        SwitchCompat cooked = dialog.findViewById(R.id.create_food_cooked);
        cooked.setOnCheckedChangeListener( (v, checked) -> cookedValue = checked );

        if ( dto != null ) {
            name.setText(dto.getName());
            price.setText(String.format(Locale.getDefault(), "%d", dto.getPrice()));
            cooked.setChecked(dto.getCooked());
            if (allergensSet)
                allergens.setSelection(dto.getAllergens());
            else
                selection = dto.getAllergens();

            Button button = dialog.findViewById(R.id.create_button);
            button.setText(R.string.button_edit);
        }
        else
            cookedValue = false; // pre-fill

        // Perform
        dialog.findViewById(R.id.create_button).setOnClickListener( view -> {
            // Try parse price
            int priceInt;
            try {
                priceInt = Integer.parseInt(price.getText().toString());
                if (priceInt <= 0)
                    throw new NumberFormatException("Invalid price.");
            }
            catch (NumberFormatException exception) {
                exception.printStackTrace();
                price.setError(activity.getString(R.string.error_food_price));
                return;
            }

            // Create DTO
            TjvFoodCreateDTO dto = new TjvFoodCreateDTO(
                name.getText().toString(),
                priceInt,
                cookedValue,
                allergens.getSelectedItems()
                    .stream()
                    .map(TjvAllergenReadDTO::getId)
                    .collect(Collectors.toList())
            );
            scheduleRequest(dto, "/food/");
        });
    }

}
