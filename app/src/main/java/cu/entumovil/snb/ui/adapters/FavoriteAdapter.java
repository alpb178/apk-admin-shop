package cu.entumovil.snb.ui.adapters;

import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Favorite;
import cu.entumovil.snb.core.holders.FavoriteViewHolder;
import cu.entumovil.snb.core.utils.FavoriteType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.MainActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private static final String TAG = SNBApp.APP_TAG + FavoriteAdapter.class.getSimpleName();

    private ArrayList<Favorite> favorites;

    private MainActivity mActivity;

    private Drawable image;

    public FavoriteAdapter(List<Favorite> aFavorites, MainActivity aActivity) {
        favorites = (null != aFavorites) ? new ArrayList<>(aFavorites) : new ArrayList<Favorite>();
        mActivity = aActivity;
    }

    public void setFavorites(ArrayList<Favorite> aFavorites) {
        favorites = aFavorites;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewLayout;
        FavoriteViewHolder favoriteViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewLayout = inflater.inflate(R.layout.row_favorite, parent, false);
        favoriteViewHolder = new FavoriteViewHolder(viewLayout);
        return favoriteViewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, final int position) {
        final Favorite handle = favorites.get(position);
        if (FavoriteType.TEAM.toString().equals(handle.getFavoriteType())) {
            holder.img_favorite.setImageResource(TeamHelper.convertAcronymToDrawableResource(handle.getName()));
        } else {
            holder.img_favorite.setImageResource(R.drawable.player_no_photo);
        }
        holder.lbl_favorite_name.setText(handle.getName());
    }

    @Override
    public int getItemCount() {
        return (null != favorites) ? favorites.size() : 0;
    }

    public Drawable getImage() {return this.image; }

    public void addItem(int position, Favorite aFav) {
        favorites.add(position, aFav);
        notifyItemInserted(position);
    }

    public Favorite removeItem(int position) {
        final Favorite fav = favorites.remove(position);
        notifyItemRemoved(position);
        return fav;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Favorite fav = favorites.remove(fromPosition);
        favorites.add(toPosition, fav);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        favorites.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Favorite> aFavorites) {
        applyAndAnimateRemovals(aFavorites);
        applyAndAnimateAdditions(aFavorites);
        applyAndAnimateMovedItems(aFavorites);
    }

    private void applyAndAnimateRemovals(List<Favorite> newFavorites) {
        for (int i = favorites.size() - 1; i >= 0; i--) {
            final Favorite fav = favorites.get(i);
            if (!newFavorites.contains(fav)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Favorite> newFavorites) {
        for (int i = 0, count = newFavorites.size(); i < count; i++) {
            final Favorite fav = newFavorites.get(i);
            if (!favorites.contains(fav)) {
                addItem(i, fav);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Favorite> newFavorites) {
        for (int toPosition = newFavorites.size() - 1; toPosition >= 0; toPosition--) {
            final Favorite fav = newFavorites.get(toPosition);
            final int fromPosition = favorites.indexOf(fav);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
