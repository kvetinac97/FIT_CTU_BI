package cz.wrzecond.restsys.table

import android.app.Dialog
import android.content.DialogInterface
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.widget.AppCompatSpinner
import cz.wrzecond.restsys.CreateListener
import cz.wrzecond.restsys.R
import wrzecond.TjvTableReadDTO
import wrzecond.TjvTableType
import wrzecond.TjvTableUpdateDTO

class TableCreateListener (activity: TableActivity, dialog: Dialog, dto: TjvTableReadDTO?)
: CreateListener<TjvTableReadDTO>(activity, dialog, dto) {

    override fun onShow (di: DialogInterface) {
        // Set type spinner
        val types = ArrayAdapter<TjvTableType>(activity, R.layout.support_simple_spinner_dropdown_item)
        types.addAll(TjvTableType.values().toMutableList())

        val tableType = dialog.findViewById<AppCompatSpinner>(R.id.create_table_type)
        tableType.adapter = types

        // Init button
        val button = dialog.findViewById<Button>(R.id.create_button)

        // Existing type
        if (dto != null) {
            tableType.setSelection(dto.type.ordinal)
            button.setText(R.string.button_edit)
        }

        // Perform creation / edit
        button.setOnClickListener {
            val dto = TjvTableUpdateDTO(tableType.selectedItem as TjvTableType)
            scheduleRequest(dto, "/tables/")
        }
    }

}