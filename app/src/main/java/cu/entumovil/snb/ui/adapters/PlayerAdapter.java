package cu.entumovil.snb.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Favorite;
import cu.entumovil.snb.core.holders.PlayerViewHolder;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.utils.FavoriteType;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.ui.activities.TeamActivity;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerViewHolder> {

    private static final String TAG = SNBApp.APP_TAG + PlayerAdapter.class.getSimpleName();

    private ArrayList<Player> players;

    private TeamActivity mActivity;

    public PlayerAdapter(List<Player> aPlayers, TeamActivity aActivity) {
        players = (null != aPlayers) ? new ArrayList<>(aPlayers) : new ArrayList<Player>();
        mActivity = aActivity;
    }

    public void setPlayers(ArrayList<Player> aPlayers) {
        players = aPlayers;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewLayout;
        PlayerViewHolder PlayerViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewLayout = inflater.inflate(R.layout.row_player, parent, false);
        PlayerViewHolder = new PlayerViewHolder(viewLayout);
        return PlayerViewHolder;
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, final int position) {
        final Player handle = players.get(position);
        Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
        CustomImage roundedImage = new CustomImage(photo, true);
        holder.img_player_photo.setImageDrawable(roundedImage);

        Glide.with(mActivity)
                .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                .asBitmap()
                .placeholder(R.drawable.player_no_photo)
                .error(R.drawable.player_no_photo)
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        CustomImage ri = new CustomImage(resource, true);
                        holder.img_player_photo.setImageDrawable(ri);
                    }
                });

        holder.lbl_player_name.setText(handle.getNombre());
        holder.lbl_player_number.setText("No. ".concat(String.valueOf(handle.getNumero())));
//        holder.img_player_favorite.setImageResource(isExistsInFavorite(handle) ? R.drawable.ic_favorite_fill : R.drawable.ic_favorite);
//        holder.img_player_favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switchFavorite(holder, handle);
//            }
//        });
        holder.lbl_player_position.setText(handle.getPosicion());
        holder.lbl_player_hand.setText(handle.getTira());
        holder.lbl_player_heigth.setText(String.valueOf(handle.getAltura()).concat(" cm"));
        holder.lbl_player_weight.setText(String.valueOf(handle.getPeso()).concat(" kg"));
    }

    private void switchFavorite(PlayerViewHolder holder, Player player) {
        String msg = "";
        try {
            Dao<Favorite, Long> dao = SNBApp.DB_HELPER.getFavoritesDao();
            Favorite fav = new Favorite(player.getId(), FavoriteType.PLAYER.toString(), player.getNombre());
            Map<String, Object> map = new HashMap<>();
            map.put("id", fav.getId());
            map.put("favoriteType", FavoriteType.PLAYER.toString());
            List<Favorite> l = dao.queryForFieldValues(map);
            if (l.size() == 0) {
                dao.create(fav);
                msg = mActivity.getString(R.string.action_add_favorite);
                holder.img_player_favorite.setImageResource(R.drawable.ic_favorite_fill);
            } else {
                Dao<Favorite, Long> daoR = SNBApp.DB_HELPER.getFavoritesDao();
                DeleteBuilder<Favorite, ?> deleteBuilder = daoR.deleteBuilder();
                deleteBuilder.where().eq("id", fav.getId());
                deleteBuilder.where().eq("favoriteType", fav.getFavoriteType());
                deleteBuilder.delete();
                holder.img_player_favorite.setImageResource(R.drawable.ic_favorite);
                msg = mActivity.getString(R.string.action_duplicate_favorite);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        Toast t = Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public int getItemCount() {
        return (null != players) ? players.size() : 0;
    }

    public void addItem(int position, Player aPlayer) {
        players.add(position, aPlayer);
        notifyItemInserted(position);
    }

    public Player removeItem(int position) {
        final Player player = players.remove(position);
        notifyItemRemoved(position);
        return player;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Player player = players.remove(fromPosition);
        players.add(toPosition, player);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        players.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Player> aPlayers) {
        applyAndAnimateRemovals(aPlayers);
        applyAndAnimateAdditions(aPlayers);
        applyAndAnimateMovedItems(aPlayers);
    }

    private void applyAndAnimateRemovals(List<Player> newPlayers) {
        for (int i = players.size() - 1; i >= 0; i--) {
            final Player player = players.get(i);
            if (!newPlayers.contains(player)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Player> newPlayers) {
        for (int i = 0, count = newPlayers.size(); i < count; i++) {
            final Player player = newPlayers.get(i);
            if (!players.contains(player)) {
                addItem(i, player);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Player> newPlayers) {
        for (int toPosition = newPlayers.size() - 1; toPosition >= 0; toPosition--) {
            final Player player = newPlayers.get(toPosition);
            final int fromPosition = players.indexOf(player);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private boolean isExistsInFavorite(Player player) {
        try {
            Dao<Favorite, Long> dao = SNBApp.DB_HELPER.getFavoritesDao();
            Map<String, Object> map = new HashMap<>();
            map.put("id", player.getId());
            map.put("favoriteType", FavoriteType.PLAYER.toString());
            List<Favorite> l = dao.queryForFieldValues(map);
            return (l.size() > 0);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

}
