package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + PlayerViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_player_photo, img_player_favorite;

    public TextView lbl_player_name, lbl_player_number, lbl_player_position;

    public TextView lbl_player_hand, lbl_player_heigth, lbl_player_weight;

    public PlayerViewHolder(View v) {
        super(v);
        context = v.getContext();
        img_player_photo = (ImageView) v.findViewById(R.id.img_player_photo);
        lbl_player_name = (TextView) v.findViewById(R.id.lbl_player_name);
        lbl_player_number = (TextView) v.findViewById(R.id.lbl_player_number);
        lbl_player_position = (TextView) v.findViewById(R.id.lbl_player_position);
        lbl_player_hand = (TextView) v.findViewById(R.id.lbl_player_hand);
        lbl_player_heigth = (TextView) v.findViewById(R.id.lbl_player_heigth);
        lbl_player_weight = (TextView) v.findViewById(R.id.lbl_player_weight);
        img_player_favorite = (ImageView) v.findViewById(R.id.img_player_favorite);
    }

}
