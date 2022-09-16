package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + LoadingViewHolder.class.getSimpleName();

    private Context context;

    public ProgressBar progressBar;

    public LoadingViewHolder(View v) {
        super(v);
        context = v.getContext();
        progressBar = (ProgressBar) itemView.findViewById(R.id.loadMoreProgressBar);
    }

}
