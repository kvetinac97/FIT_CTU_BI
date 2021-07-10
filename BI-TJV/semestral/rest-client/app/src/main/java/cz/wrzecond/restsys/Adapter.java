package cz.wrzecond.restsys;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wrzecond.IReadDTO;

public abstract class Adapter<T extends IReadDTO> extends RecyclerView.Adapter<Adapter<T>.ViewHolder> {

    // Properties
    protected Activity<T> activity;
    private List<T> dtos;

    private final int errorResId;
    private final int layoutResId;

    // Constructors
    protected Adapter (Activity<T> activity, @LayoutRes int layoutResId, @NonNull List<T> dtos) {
        this.activity = activity;
        this.dtos = dtos;
        this.layoutResId = layoutResId;
        this.errorResId = -1;
    }
    protected Adapter (Activity<T> activity, @LayoutRes int layoutResId, @StringRes int errorResId) {
        this.activity = activity;
        this.layoutResId = layoutResId;
        this.errorResId = errorResId;
    }

    // Wrapper for RecyclerAdapter methods

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        // Error handling
        if (isError()) {
            TextView errorView = new TextView(activity);
            errorView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            errorView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            errorView.setText(errorResId);
            errorView.setTextSize(25f);
            errorView.setPaddingRelative(0, 25, 0, 0);
            errorView.setTextColor(Color.rgb(255, 0, 0));
            return createViewHolder(errorView);
        }

        // Classic view create
        View view = activity.getLayoutInflater().inflate(layoutResId, parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        if (!isError())
            holder.update(dtos.get(position));
    }

    @Override
    public int getItemViewType (int position) {
        return position;
    }

    @Override
    public int getItemCount () {
        return isError() ? 1 : dtos.size();
    }

    // Necessary methods
    protected abstract ViewHolder createViewHolder (View view);
    private boolean isError () { return errorResId != -1; }

    // ViewHolder
    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        // Constructor
        protected ViewHolder (@NonNull View view) {
            super (view);
            if ( errorResId == -1 )
                initView(view);
        }

        // Necessary methods
        protected abstract void initView (View view);
        protected abstract void update (T dto);

    }

}
