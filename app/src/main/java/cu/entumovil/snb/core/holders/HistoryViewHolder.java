package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + HistoryViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_hitory;

    public TextView txt_history_resume, txt_history_description;

    public HistoryViewHolder(View v) {
        super(v);
        context = v.getContext();
        img_hitory = (ImageView) v.findViewById(R.id.img_hitory);
        txt_history_resume = (TextView) v.findViewById(R.id.txt_history_resume);
        txt_history_description = (TextView) v.findViewById(R.id.txt_history_description);
    }

}
