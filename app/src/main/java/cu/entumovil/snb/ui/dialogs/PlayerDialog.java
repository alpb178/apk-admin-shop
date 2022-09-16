package cu.entumovil.snb.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.ui.activities.PlayerActivity;

public class PlayerDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private Player player;

    private ImageButton imgBtnFavorite, imgBtnInfo;

    public PlayerDialog(Context context, Player aPlayer) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.player = aPlayer;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_player);

        TextView txtContactName = (TextView) findViewById(R.id.txtPlayerName);
        txtContactName.setText(this.player.getNombre());

        final ImageView imgProfile = (ImageView) findViewById(R.id.imgPlayer);
        if (null != player.getFoto()) {
            Glide.with(context)
                    .load(context.getResources().getString(R.string.IMAGE_URL_PALYER).concat(player.getFoto()))
                    .asBitmap()
                    .placeholder(R.drawable.player_no_photo)
                    .error(R.drawable.player_no_photo)
                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            imgProfile.setImageBitmap(resource);
                        }
                    });
        } else {
            imgProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.player_no_photo));
        }

        imgBtnFavorite = (ImageButton) findViewById(R.id.imgPlayerFavorite);
        imgBtnFavorite.setOnClickListener(this);

        imgBtnInfo = (ImageButton) findViewById(R.id.imgPlayerStats);
        imgBtnInfo.setOnClickListener(this);

//        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layoutPlayerDialog);
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_player_pict);
//        relativeLayout.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgPlayerFavorite:
                break;
            case R.id.imgPlayerStats:
                intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(PlayerActivity.INTENT_PLAYER_HOLDER, player);
                context.startActivity(intent);
                break;
        }
        dismiss();
    }

}
