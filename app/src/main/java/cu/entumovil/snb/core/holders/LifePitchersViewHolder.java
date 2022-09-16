package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class LifePitchersViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + LifePitchersViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_stats_player_photo;

    public TextView lbl_stats_player_name, lbl_stats_player_average, lbl_stats_player_jg, lbl_stats_player_jp;

    public TextView lbl_stats_player_js, lbl_stats_player_pcl, lbl_stats_player_whip, lbl_stats_player_kkball;

    public TextView lbl_stats_player_runsallowed, lbl_stats_player_team;

    public LifePitchersViewHolder(View v) {
        super(v);
        context = v.getContext();
        img_stats_player_photo = (ImageView) v.findViewById(R.id.img_stats_player_photo);
        lbl_stats_player_name = (TextView) v.findViewById(R.id.lbl_stats_player_name);
        lbl_stats_player_team = (TextView) v.findViewById(R.id.lbl_stats_player_team);

        lbl_stats_player_jg = (TextView) v.findViewById(R.id.lbl_stats_player_jg);
        lbl_stats_player_jp = (TextView) v.findViewById(R.id.lbl_stats_player_jp);
        lbl_stats_player_js = (TextView) v.findViewById(R.id.lbl_stats_player_js);
        lbl_stats_player_average = (TextView) v.findViewById(R.id.lbl_stats_player_average);
        lbl_stats_player_runsallowed = (TextView) v.findViewById(R.id.lbl_stats_player_runsallowed);
        lbl_stats_player_pcl = (TextView) v.findViewById(R.id.lbl_stats_player_pcl);
        lbl_stats_player_whip = (TextView) v.findViewById(R.id.lbl_stats_player_whip);
        lbl_stats_player_kkball = (TextView) v.findViewById(R.id.lbl_stats_player_kkball);
    }

}
