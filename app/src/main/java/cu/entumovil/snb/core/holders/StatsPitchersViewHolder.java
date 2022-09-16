package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class StatsPitchersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + StatsPitchersViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_stats_player_photo;

    public TextView lbl_stats_player_name, lbl_stats_player_average, lbl_stats_player_jg, lbl_stats_player_jp;

    public TextView lbl_stats_player_js, lbl_stats_player_pcl, lbl_stats_player_whip, lbl_stats_player_kkball;

    public TextView lbl_stats_player_runsallowed, lbl_stats_player_team;

    public LinearLayout layoutStatsNormalPitcher, layoutStatsSpecialPitcher;

    public TextView lbl_stats_player_special_h9, lbl_stats_player_special_obp, lbl_stats_player_special_ops;

    public TextView lbl_stats_player_special_bb9, lbl_stats_player_special_so9;

    private OnRowClickListener listener;

    public StatsPitchersViewHolder(View v, OnRowClickListener sListener) {
        super(v);
        context = v.getContext();
        listener = sListener;
        img_stats_player_photo = (ImageView) v.findViewById(R.id.img_stats_player_photo);
        lbl_stats_player_name = (TextView) v.findViewById(R.id.lbl_stats_player_name);
        lbl_stats_player_team = (TextView) v.findViewById(R.id.lbl_stats_player_team);

        layoutStatsNormalPitcher = (LinearLayout) v.findViewById(R.id.layoutStatsNormalPitcher);
        lbl_stats_player_jg = (TextView) v.findViewById(R.id.lbl_stats_player_jg);
        lbl_stats_player_jp = (TextView) v.findViewById(R.id.lbl_stats_player_jp);
        lbl_stats_player_js = (TextView) v.findViewById(R.id.lbl_stats_player_js);
        lbl_stats_player_average = (TextView) v.findViewById(R.id.lbl_stats_player_average);
        lbl_stats_player_runsallowed = (TextView) v.findViewById(R.id.lbl_stats_player_runsallowed);
        lbl_stats_player_pcl = (TextView) v.findViewById(R.id.lbl_stats_player_pcl);
        lbl_stats_player_whip = (TextView) v.findViewById(R.id.lbl_stats_player_whip);
        lbl_stats_player_kkball = (TextView) v.findViewById(R.id.lbl_stats_player_kkball);

        layoutStatsSpecialPitcher = (LinearLayout) v.findViewById(R.id.layoutStatsSpecialPitcher);
        lbl_stats_player_special_h9 = (TextView) v.findViewById(R.id.lbl_stats_player_special_h9);
        lbl_stats_player_special_obp = (TextView) v.findViewById(R.id.lbl_stats_player_special_obp);
        lbl_stats_player_special_ops = (TextView) v.findViewById(R.id.lbl_stats_player_special_ops);
        lbl_stats_player_special_bb9 = (TextView) v.findViewById(R.id.lbl_stats_player_special_bb9);
        lbl_stats_player_special_so9 = (TextView) v.findViewById(R.id.lbl_stats_player_special_so9);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onRowPress(view, getAdapterPosition());
    }

}
