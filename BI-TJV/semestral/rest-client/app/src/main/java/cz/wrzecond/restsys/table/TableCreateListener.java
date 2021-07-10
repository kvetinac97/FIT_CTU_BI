package cz.wrzecond.restsys.table;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatSpinner;

import cz.wrzecond.restsys.R;
import cz.wrzecond.restsys.CreateListener;
import wrzecond.table.TjvTableCreateDTO;
import wrzecond.table.TjvTableReadDTO;
import wrzecond.table.TjvTableType;

public class TableCreateListener extends CreateListener<TjvTableReadDTO> {

    public TableCreateListener (TableActivity activity, Dialog dialog, TjvTableReadDTO dto) { super(activity, dialog, dto); }

    @Override
    public void onShow (DialogInterface di) {
        // Init dialog
        AppCompatSpinner tableType = dialog.findViewById(R.id.create_table_type);
        ArrayAdapter<TjvTableType> types = new ArrayAdapter<>(activity, R.layout.support_simple_spinner_dropdown_item);
        types.addAll(TjvTableType.values());
        tableType.setAdapter(types);

        if ( dto != null ) {
            tableType.setSelection(dto.getType().ordinal());

            Button button = dialog.findViewById(R.id.create_button);
            button.setText(R.string.button_edit);
        }

        // Perform
        dialog.findViewById(R.id.create_button).setOnClickListener( view -> {
            // Create DTO
            TjvTableCreateDTO dto = new TjvTableCreateDTO((TjvTableType) tableType.getSelectedItem());
            scheduleRequest(dto, "/tables/");
        });
    }

}
