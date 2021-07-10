package cz.wrzecond.restsys.allergen;

import androidx.annotation.StringRes;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cz.wrzecond.restsys.Activity;
import cz.wrzecond.restsys.Adapter;
import wrzecond.allergen.TjvAllergenReadDTO;

public class AllergenActivity extends Activity<TjvAllergenReadDTO> {

    public static final String EXTRA_FOOD_ID = "FOOD_ID";

    @Override
    protected String getEndpoint () {
        // Allergen list
        if ( getIntent() != null && getIntent().hasExtra(EXTRA_FOOD_ID) )
            return "/food/allergens/" + getIntent().getIntExtra(EXTRA_FOOD_ID, -1);

        return "/allergens/";
    }

    @Override
    protected Type getDtoListType() {
        return new TypeToken<List<TjvAllergenReadDTO>>(){}.getType();
    }

    @Override
    protected Adapter<TjvAllergenReadDTO> createAdapter (List<TjvAllergenReadDTO> dtos) {
        return new AllergenAdapter(this, dtos);
    }

    @Override
    protected Adapter<TjvAllergenReadDTO> createAdapter (@StringRes int errorResId) {
        return new AllergenAdapter(this, errorResId);
    }

}
