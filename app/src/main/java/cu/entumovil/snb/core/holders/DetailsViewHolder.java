package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class DetailsViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + DetailsViewHolder.class.getSimpleName();

    private Context context;

    public LinearLayout layoutHitter, layoutPitcher;

    public ImageView img_details_hitter_photo, img_details_pitcher_photo;

    public TextView lbl_details_hitter_name, lbl_details_hitter_position, lbl_details_player_average,
                    lbl_details_pitcher_name, lbl_details_pitcher_pcl;

    public TextView lbl_details_hitter_ab, lbl_details_player_runsimpulsed,
                    lbl_details_player_homeruns, lbl_details_player_error;

    public TextView lbl_details_pitcher_ab, lbl_details_player_runs,
                    lbl_details_player_kk, lbl_details_player_bb;

    public DetailsViewHolder(View v) {
        super(v);
        context = v.getContext();
        //Hitter
        layoutHitter = (LinearLayout) v.findViewById(R.id.layoutHitter);
        img_details_hitter_photo = (ImageView) v.findViewById(R.id.img_details_hitter_photo);
        lbl_details_hitter_name = (TextView) v.findViewById(R.id.lbl_details_hitter_name);
        lbl_details_hitter_position = (TextView) v.findViewById(R.id.lbl_details_hitter_position);
        lbl_details_hitter_ab = (TextView) v.findViewById(R.id.lbl_details_hitter_ab);
        lbl_details_player_average = (TextView) v.findViewById(R.id.lbl_details_player_average);
        lbl_details_player_runsimpulsed = (TextView) v.findViewById(R.id.lbl_details_player_runsimpulsed);
        lbl_details_player_homeruns = (TextView) v.findViewById(R.id.lbl_details_player_homeruns);
        lbl_details_player_error = (TextView) v.findViewById(R.id.lbl_details_player_error);

        //Pitcher
        layoutPitcher = (LinearLayout) v.findViewById(R.id.layoutPitcher);
        img_details_pitcher_photo = (ImageView) v.findViewById(R.id.img_details_pitcher_photo);
        lbl_details_pitcher_name = (TextView) v.findViewById(R.id.lbl_details_pitcher_name);
        lbl_details_pitcher_ab = (TextView) v.findViewById(R.id.lbl_details_pitcher_ab);
        lbl_details_pitcher_pcl = (TextView) v.findViewById(R.id.lbl_details_pitcher_pcl);
        lbl_details_player_runs = (TextView) v.findViewById(R.id.lbl_details_player_runs);
        lbl_details_player_kk = (TextView) v.findViewById(R.id.lbl_details_player_kk);
        lbl_details_player_bb = (TextView) v.findViewById(R.id.lbl_details_player_bb);
    }

}
