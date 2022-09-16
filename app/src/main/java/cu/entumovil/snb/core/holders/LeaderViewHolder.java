package cu.entumovil.snb.core.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.SerieLeader;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class LeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + LeaderViewHolder.class.getSimpleName();

    public View mView;

    public ImageView img_leader_photo;

    public TextView mName, mTit, mSig, mValue, mMore;

    public SerieLeader mItem;

    private OnRowClickListener listener;

    public LeaderViewHolder(View v, OnRowClickListener sListener) {
        super(v);
        mView = v;
        listener = sListener;
        img_leader_photo = (ImageView) v.findViewById(R.id.img_leader_photo);
        mName = (TextView) v.findViewById(R.id.name);
        mSig = (TextView) v.findViewById(R.id.team);
        mTit = (TextView) v.findViewById(R.id.tit);
        mValue = (TextView) v.findViewById(R.id.titvalue);
        mMore = (TextView) v.findViewById(R.id.more);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onRowPress(view, getAdapterPosition());
    }

}
