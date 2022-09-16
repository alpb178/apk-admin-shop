package cu.entumovil.snb.core.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class StatsHittersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + StatsHittersViewHolder.class.getSimpleName();

    public ImageView img_stats_player_photo, img_arrow_right;

    public TextView lbl_stats_player_name, lbl_stats_player_average, lbl_stats_player_hits, lbl_stats_player_runs;

    public TextView lbl_stats_player_runsimpulsed, lbl_stats_player_homeruns, lbl_stats_player_obp, lbl_stats_player_ops;

    public TextView lbl_stats_player_ballkk, lbl_stats_player_team;

    public LinearLayout layoutStatsNormalHitter, layoutStatsSpecialHitter;

    public TextView lbl_stats_player_special_cpr, lbl_stats_player_special_ccr, lbl_stats_player_special_babip;

    public TextView lbl_stats_player_special_db, lbl_stats_player_special_bbcb;

    private OnRowClickListener listener;

    public StatsHittersViewHolder(View v, OnRowClickListener sListener) {
        super(v);
        listener = sListener;
        img_stats_player_photo = (ImageView) v.findViewById(R.id.img_stats_player_photo);
        img_arrow_right = (ImageView) v.findViewById(R.id.img_arrow_right);
        lbl_stats_player_name = (TextView) v.findViewById(R.id.lbl_stats_player_name);

        layoutStatsNormalHitter = (LinearLayout) v.findViewById(R.id.layoutStatsNormalHitter);
        lbl_stats_player_team = (TextView) v.findViewById(R.id.lbl_stats_player_team);
        lbl_stats_player_average = (TextView) v.findViewById(R.id.lbl_stats_player_average);
        lbl_stats_player_hits = (TextView) v.findViewById(R.id.lbl_stats_player_hits);
        lbl_stats_player_runs = (TextView) v.findViewById(R.id.lbl_stats_player_runs);
        lbl_stats_player_runsimpulsed = (TextView) v.findViewById(R.id.lbl_stats_player_runsimpulsed);
        lbl_stats_player_homeruns = (TextView) v.findViewById(R.id.lbl_stats_player_homeruns);
        lbl_stats_player_obp = (TextView) v.findViewById(R.id.lbl_stats_player_obp);
        lbl_stats_player_ops = (TextView) v.findViewById(R.id.lbl_stats_player_ops);
        lbl_stats_player_ballkk = (TextView) v.findViewById(R.id.lbl_stats_player_ballkk);

        layoutStatsSpecialHitter = (LinearLayout) v.findViewById(R.id.layoutStatsSpecialHitter);
        lbl_stats_player_special_cpr = (TextView) v.findViewById(R.id.lbl_stats_player_special_cpr);
        lbl_stats_player_special_ccr = (TextView) v.findViewById(R.id.lbl_stats_player_special_ccr);
        lbl_stats_player_special_babip = (TextView) v.findViewById(R.id.lbl_stats_player_special_babip);
        lbl_stats_player_special_db = (TextView) v.findViewById(R.id.lbl_stats_player_special_db);
        lbl_stats_player_special_bbcb = (TextView) v.findViewById(R.id.lbl_stats_player_special_bbcb);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onRowPress(view, getAdapterPosition());
    }
}
