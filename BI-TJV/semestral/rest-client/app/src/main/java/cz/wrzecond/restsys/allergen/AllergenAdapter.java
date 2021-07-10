package cz.wrzecond.restsys.allergen;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.List;
import java.util.Locale;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import wrzecond.allergen.TjvAllergenReadDTO;

public class AllergenAdapter extends Adapter<TjvAllergenReadDTO> {

    // Constructors
    public AllergenAdapter (Activity<TjvAllergenReadDTO> activity, @NonNull List<TjvAllergenReadDTO> dtos) {
        super (activity, R.layout.item_allergen, dtos);
    }
    public AllergenAdapter (Activity<TjvAllergenReadDTO> activity, @StringRes int errorResId) {
        super (activity, R.layout.item_allergen, errorResId);
    }

    @Override
    protected Adapter<TjvAllergenReadDTO>.ViewHolder createViewHolder (View view) {
        return new AllergenAdapter.ViewHolder(view);
    }

    protected class ViewHolder extends Adapter<TjvAllergenReadDTO>.ViewHolder {

        // === Properties
        private TextView allergenId;
        private TextView allergenName;

        public ViewHolder (@NonNull View view) { super(view); }

        @Override
        protected void initView (@NonNull View view) {
            this.allergenId = view.findViewById(R.id.allergen_id);
            this.allergenName = view.findViewById(R.id.allergen_name);
        }

        @Override
        protected void update ( TjvAllergenReadDTO dto ) {
            allergenId.setText(String.format(Locale.getDefault(), "%d", dto.getId()));
            allergenName.setText(dto.getName());
        }

    }

}
