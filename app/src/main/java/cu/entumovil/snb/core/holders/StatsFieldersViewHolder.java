package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class StatsFieldersViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + StatsFieldersViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_stats_player_photo;

    public TextView lbl_stats_player_name, lbl_stats_player_average, lbl_stats_player_jj, lbl_stats_player_ga;

    public TextView lbl_stats_player_inn, lbl_stats_player_o, lbl_stats_player_a, lbl_stats_player_e;

    public TextView lbl_stats_player_dp, lbl_stats_player_team;

    public StatsFieldersViewHolder(View v) {
        super(v);
        context = v.getContext();
        img_stats_player_photo = (ImageView) v.findViewById(R.id.img_stats_player_photo);
        lbl_stats_player_name = (TextView) v.findViewById(R.id.lbl_stats_player_name);
        lbl_stats_player_team = (TextView) v.findViewById(R.id.lbl_stats_player_team);
        lbl_stats_player_jj = (TextView) v.findViewById(R.id.lbl_stats_player_jj);
        lbl_stats_player_ga = (TextView) v.findViewById(R.id.lbl_stats_player_ga);
        lbl_stats_player_inn = (TextView) v.findViewById(R.id.lbl_stats_player_inn);
        lbl_stats_player_o = (TextView) v.findViewById(R.id.lbl_stats_player_o);
        lbl_stats_player_a = (TextView) v.findViewById(R.id.lbl_stats_player_a);
        lbl_stats_player_e = (TextView) v.findViewById(R.id.lbl_stats_player_e);
        lbl_stats_player_average = (TextView) v.findViewById(R.id.lbl_stats_player_average);
        lbl_stats_player_dp = (TextView) v.findViewById(R.id.lbl_stats_player_dp);
    }

}
