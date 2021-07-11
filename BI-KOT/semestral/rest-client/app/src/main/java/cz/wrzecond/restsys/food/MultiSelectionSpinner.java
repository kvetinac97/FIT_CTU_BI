package cz.wrzecond.restsys.food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wrzecond.IReadDTO;

/**
 * Multi selection spinner class
 * source: https://www.woolha.com/tutorials/android-multi-select-spinner-example
 * license (for non-commercial use)
 */
public class MultiSelectionSpinner<T extends IReadDTO> extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {

    // Properties
    List<T> items = null;
    boolean[] selection = null;
    ArrayAdapter<String> adapter;

    public MultiSelectionSpinner (Context context) {
        super(context);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }
    public MultiSelectionSpinner (Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick (DialogInterface dialog, int which, boolean isChecked) {
        if (selection != null && which < selection.length) {
            selection[which] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] itemNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++)
            itemNames[i] = items.get(i).getDisplayName();

        builder.setMultiChoiceItems(itemNames, selection, this);
        builder.setPositiveButton("OK", null);
        builder.show();
        return true;
    }

    @Override
    public void setAdapter (SpinnerAdapter adapter) {
        throw new UnsupportedOperationException();
    }

    public void setItems (List<T> items) {
        this.items = items;
        selection = new boolean[this.items.size()];
        adapter.clear();
        adapter.add("");
        Arrays.fill(selection, false);
    }
    public void setSelection (List<Integer> ids) {
        Arrays.fill(this.selection, false);

        for (Integer id : ids)
            for (int j = 0; j < items.size(); ++j)
                if (items.get(j).getId() == id)
                    this.selection[j] = true;

        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false; int remaining = 3;

        for (int i = 0; i < items.size(); ++i) {
            if (selection[i]) {
                if ( remaining <= 0 ) {
                    sb.append("...");
                    break;
                }

                if (foundOne)
                    sb.append(", ");

                foundOne = true;
                sb.append(items.get(i).getDisplayName());
                remaining--;
            }
        }

        return sb.toString();
    }
    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i)
            if (selection[i])
                selectedItems.add(items.get(i));

        return selectedItems;
    }

}