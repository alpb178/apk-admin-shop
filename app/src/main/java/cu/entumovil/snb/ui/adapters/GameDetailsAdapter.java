package cu.entumovil.snb.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.DetailsViewHolder;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.ui.activities.GameDetailsActivity;

public class GameDetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder> {

    private static final String TAG = SNBApp.APP_TAG + GameDetailsAdapter.class.getSimpleName();

    private ArrayList players;

    private GameDetailsActivity mActivity;

    public GameDetailsAdapter(ArrayList aPlayers, GameDetailsActivity aActivity) {
        players = (null != aPlayers) ? new ArrayList<>(aPlayers) : new ArrayList();
        mActivity = aActivity;
    }

    public void setPlayers(ArrayList aPlayers) {
        players = aPlayers;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewLayout;
        DetailsViewHolder detailsViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewLayout = inflater.inflate(R.layout.row_boxscore, parent, false);
        detailsViewHolder = new DetailsViewHolder(viewLayout);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailsViewHolder holder, int position) {
        final Player handle;
        holder.layoutHitter.setVisibility(View.GONE);
        holder.layoutPitcher.setVisibility(View.GONE);
        Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
        CustomImage roundedImage = new CustomImage(photo, true);
        if (players.get(position) instanceof Hitter) {
            handle = (Hitter) players.get(position);
            holder.layoutHitter.setVisibility(View.VISIBLE);
            holder.img_details_hitter_photo.setImageDrawable(roundedImage);

            if (null != handle.getFoto()) {
                Glide.with(mActivity)
                        .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                        .asBitmap()
                        .placeholder(R.drawable.player_no_photo)
                        .error(R.drawable.player_no_photo)
                        .into(new SimpleTarget<Bitmap>(100,100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                CustomImage ri = new CustomImage(resource, true);
                                holder.img_details_hitter_photo.setImageDrawable(ri);
                            }
                        });
            }

            holder.lbl_details_hitter_name.setText(((Hitter) handle).getNombre());
            holder.lbl_details_hitter_position.setText("Posición: " + handle.getPosicion());
            holder.lbl_details_hitter_ab.setText(String.valueOf(((Hitter) handle).getHits()).concat("/").concat(String.valueOf(((Hitter) handle).getVB())));
            holder.lbl_details_player_average.setText(handle.getAVE());
            holder.lbl_details_player_homeruns.setText(String.valueOf(((Hitter) handle).getJonrones()));
            holder.lbl_details_player_runsimpulsed.setText(String.valueOf(((Hitter) handle).getImpulsadas()));
            holder.lbl_details_player_error.setText(String.valueOf(((Hitter) handle).getErrores()));
        } else {
            handle = (Pitcher) players.get(position);
            holder.layoutPitcher.setVisibility(View.VISIBLE);
            holder.img_details_pitcher_photo.setImageDrawable(roundedImage);

            if (null != handle.getFoto()) {
                Glide.with(mActivity)
                        .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                        .asBitmap()
                        .placeholder(R.drawable.player_no_photo)
                        .error(R.drawable.player_no_photo)
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                CustomImage ri = new CustomImage(resource, true);
                                holder.img_details_pitcher_photo.setImageDrawable(ri);
                            }
                        });
            }

            holder.lbl_details_pitcher_name.setText(handle.getNombre());
            holder.lbl_details_pitcher_ab.setText(String.valueOf(((Pitcher) handle).getVB()));
            holder.lbl_details_player_runs.setText(String.valueOf(((Pitcher) handle).getCarreras()));
            holder.lbl_details_player_kk.setText(String.valueOf(((Pitcher) handle).getPonches()));
            holder.lbl_details_player_bb.setText(String.valueOf(((Pitcher) handle).getBases()));
        }
    }

    @Override
    public int getItemCount() {
        return (null != players) ? players.size() : 0;
    }

    public void addItem(int position, Object aPlayer) {
        players.add(position, aPlayer);
        notifyItemInserted(position);
    }

//    public Player removeItem(int position) {
//        final Player player = players.remove(position);
//        notifyItemRemoved(position);
//        return player;
//    }
//
//    public void moveItem(int fromPosition, int toPosition) {
//        final Player player = players.remove(fromPosition);
//        players.add(toPosition, player);
//        notifyItemMoved(fromPosition, toPosition);
//    }

    public void clear() {
        players.clear();
        notifyDataSetChanged();
    }

//    public void animateTo(ArrayList aPlayers) {
//        applyAndAnimateRemovals(aPlayers);
//        applyAndAnimateAdditions(aPlayers);
//        applyAndAnimateMovedItems(aPlayers);
//    }

//    private void applyAndAnimateRemovals(ArrayList newPlayers) {
//        for (int i = players.size() - 1; i >= 0; i--) {
//            final Player player = players.get(i);
//            if (!newPlayers.contains(player)) {
//                removeItem(i);
//            }
//        }
//    }
//
//    private void applyAndAnimateAdditions(ArrayList newPlayers) {
//        for (int i = 0, count = newPlayers.size(); i < count; i++) {
//            final Player player = newPlayers.get(i);
//            if (!players.contains(player)) {
//                addItem(i, player);
//            }
//        }
//    }
//
//    private void applyAndAnimateMovedItems(ArrayList<Player> newPlayers) {
//        for (int toPosition = newPlayers.size() - 1; toPosition >= 0; toPosition--) {
//            final Player player = newPlayers.get(toPosition);
//            final int fromPosition = players.indexOf(player);
//            if (fromPosition >= 0 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }

}
