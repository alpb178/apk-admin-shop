package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Favorite;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + FavoriteViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_favorite;

    public TextView lbl_favorite_name;

    public FavoriteViewHolder(View v) {
        super(v);
        context = v.getContext();
        img_favorite = (ImageView) v.findViewById(R.id.img_favorite);
        lbl_favorite_name = (TextView) v.findViewById(R.id.lbl_favorite_name);
    }

    public void removeFromFavorites(Favorite fav) {
        try {
            Dao<Favorite, Long> dao = SNBApp.DB_HELPER.getFavoritesDao();
            DeleteBuilder<Favorite, ?> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("id", fav.getId());
            deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
