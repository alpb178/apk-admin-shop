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
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.ui.listeners.OnNewsRowClickListener;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + NewsViewHolder.class.getSimpleName();

    private Context context;

    public ImageView img_news_card, img_news_read_off;

    public TextView txt_news_resumen, txt_news_cropbody;

    private OnNewsRowClickListener listener;

    public NewsViewHolder(View v, OnNewsRowClickListener aListener) {
        super(v);
        context = v.getContext();
        this.listener = aListener;
        img_news_card = (ImageView) v.findViewById(R.id.img_news_card);
        txt_news_resumen = (TextView) v.findViewById(R.id.txt_news_resumen);
        txt_news_cropbody = (TextView) v.findViewById(R.id.txt_news_cropbody);
        img_news_read_off = (ImageView) v.findViewById(R.id.img_news_read_off);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onNewsRowPress(view, this.getAdapterPosition());
    }

    public void unmarkLikeReadOff(News news) {
        try {
            Dao<cu.entumovil.snb.core.db.models.News, Long> dao = SNBApp.DB_HELPER.getNewsDao();
            DeleteBuilder<cu.entumovil.snb.core.db.models.News, ?> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("id", news.getId());
            deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
