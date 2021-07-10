package cz.wrzecond.restsys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.function.Consumer;

import cz.wrzecond.restsys.task.HttpMethod;
import cz.wrzecond.restsys.task.HttpRequest;
import cz.wrzecond.restsys.task.HttpResponse;
import wrzecond.ICreateDTO;
import wrzecond.IReadDTO;

public abstract class CreateListener<T extends IReadDTO> implements DialogInterface.OnShowListener {

    // === Properties
    protected Activity<T> activity;
    protected Dialog dialog;
    protected T dto;

    // === Constructor
    public CreateListener (Activity<T> activity, Dialog dialog, T dto) {
        this.activity = activity;
        this.dialog = dialog;
        this.dto = dto;
    }

    protected void scheduleRequest (ICreateDTO dto, String path) {
        HttpRequest request = this.dto == null ? new HttpRequest(path, HttpMethod.POST)
                : new HttpRequest(path + this.dto.getId(), HttpMethod.PATCH);
        request.setBody(new Gson().toJson(dto));
        activity.getService().scheduleTask(getResponseConsumer(path), request);
    }
    private Consumer<HttpResponse> getResponseConsumer (String path) {
        return (response) -> {
            // Show message and close
            Toast.makeText(activity,
                response.getStatus().isSuccess() && response.getLocationHeader() != null ?
                    activity.getString(R.string.create_success,
                    response.getLocationHeader().replace(path, "")) :
                    ( response.getStatus().isSuccess() ? activity.getString(R.string.edit_success) :
                    activity.getString(R.string.create_edit_failure, response.getStatus().name())
            ), Toast.LENGTH_SHORT).show();

            // Hide and reload list if success
            dialog.dismiss();
            if (response.getStatus().isSuccess())
                activity.loadList();
        };
    }

}
