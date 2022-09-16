package cu.entumovil.snb.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.NewsViewHolder;
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.ui.activities.NewsActivity;
import cu.entumovil.snb.ui.activities.NewsDetailsActivity;
import cu.entumovil.snb.ui.listeners.OnNewsRowClickListener;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> implements OnNewsRowClickListener {

    private static final String TAG = SNBApp.APP_TAG + NewsAdapter.class.getSimpleName();

    private ArrayList<News> lNews;

    private NewsActivity mActivity;

    private int position;

    private Gson gson;

    private Dao<cu.entumovil.snb.core.db.models.News, Long> dao;

    public NewsAdapter(ArrayList<News> aNews, NewsActivity aActivity) {
        lNews = (null != aNews) ? new ArrayList<>(aNews) : new ArrayList<News>();
        mActivity = aActivity;
        try {
            gson = new Gson();
            dao = SNBApp.DB_HELPER.getNewsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPositions(ArrayList<News> aNews) {
        lNews = aNews;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup aNews, int viewType) {
        View viewLayout;
        NewsViewHolder newsViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aNews.getContext());

        viewLayout = inflater.inflate(R.layout.row_news, aNews, false);
        newsViewHolder = new NewsViewHolder(viewLayout, this);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        final News handle = lNews.get(position);

        holder.txt_news_resumen.setText(Html.fromHtml(handle.getRes()));
//        if(handle.getNot()!=null)
//        holder.txt_news_cropbody.setText(Html.fromHtml(handle.getNot()));

//        holder.img_news_read_off.setImageResource(isExistsLikeReadOff(handle) ? R.drawable.ic_bookmark_fill : R.drawable.ic_bookmark);
        if (handle.isPictureLoaded()) {
            try {
                FileInputStream fis = mActivity.openFileInput(handle.getFoto());
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                CustomImage ri = new CustomImage(bitmap, false);
                holder.img_news_card.setImageDrawable(ri);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (null != handle.getFoto()) {
            Glide.with(mActivity)
                    .load(mActivity.getResources().getString(R.string.IMAGE_URL_NEWS).concat(handle.getFoto()))
                    .centerCrop()
                    .placeholder(R.drawable.img_default_news)
                    .error(R.drawable.img_default_news)
                    .into(new SimpleTarget<GlideDrawable>() {
                       @Override
                       public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                           holder.img_news_card.setImageDrawable(glideDrawable);
                           holder.img_news_card.setDrawingCacheEnabled(true);
                           saveData(handle, holder.img_news_card);
                       }
                   });
        } else {
            holder.img_news_card.setVisibility(View.GONE);
        }

//        holder.img_news_read_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                readOffLine(holder, handle);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != lNews) ? lNews.size() : 0;
    }

    public void addItem(int position, News aNews) {
        lNews.add(position, aNews);
        notifyItemInserted(position);
    }

    public News removeItem(int position) {
        final News news = lNews.remove(position);
        notifyItemRemoved(position);
        return news;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final News news = lNews.remove(fromPosition);
        lNews.add(toPosition, news);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        lNews.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<News> newNews) {
        applyAndAnimateRemovals(newNews);
        applyAndAnimateAdditions(newNews);
        applyAndAnimateMovedItems(newNews);
    }

    private void applyAndAnimateRemovals(ArrayList<News> newNews) {
        for (int i = lNews.size() - 1; i >= 0; i--) {
            final News news = lNews.get(i);
            if (!newNews.contains(news)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<News> newNews) {
        for (int i = 0, count = newNews.size(); i < count; i++) {
            final News news = newNews.get(i);
            if (!lNews.contains(news)) {
                addItem(i, news);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<News> newNews) {
        for (int toPosition = newNews.size() - 1; toPosition >= 0; toPosition--) {
            final News news = newNews.get(toPosition);
            final int fromPosition = lNews.indexOf(news);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onNewsRowPress(View newsHolderRow, int position) {
        position = (position > -1) ? position : 0;
        News n = lNews.get(position);
        Intent i = new Intent().setClass(mActivity, NewsDetailsActivity.class);
        i.putExtra(NewsDetailsActivity.INTENT_NEWS_HOLDER, n);
        mActivity.startActivity(i);
        mActivity.setResult(Activity.RESULT_OK);
    }

    private void readOffLine(NewsViewHolder holder, News handle) {
        try {
            Dao<cu.entumovil.snb.core.db.models.News, Long> dao = SNBApp.DB_HELPER.getNewsDao();
            Map<String, Object> map = new HashMap<>();
            map.put("id", handle.getId());
            List<cu.entumovil.snb.core.db.models.News> l = dao.queryForFieldValues(map);
            if (l.size() == 0) {
                cu.entumovil.snb.core.db.models.News news = new cu.entumovil.snb.core.db.models.News();
                news.setId(handle.getId());
                news.setJsonNews("[{\"Resumen\":\""+handle.getRes()+"\",\"Noticia\":\""+handle.getNot()+"\"}]");
                dao.create(news);
                holder.img_news_read_off.setImageResource(R.drawable.ic_bookmark_fill);
            } else {
                holder.img_news_read_off.setImageResource(R.drawable.ic_bookmark);
                holder.unmarkLikeReadOff(handle);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean isExistsLikeReadOff(News handle) {
        try {
            Dao<cu.entumovil.snb.core.db.models.News, Long> dao = SNBApp.DB_HELPER.getNewsDao();
            Map<String, Object> map = new HashMap<>();
            map.put("id", handle.getId());
            List<cu.entumovil.snb.core.db.models.News> l = dao.queryForFieldValues(map);
            return (l.size() > 0);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private void saveData(News handle, ImageView imageView) {
        try {
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            Bitmap bitmap = imageView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            byte[] image = blob.toByteArray();

            FileOutputStream fos = mActivity.openFileOutput(handle.getFoto(), Context.MODE_PRIVATE);
            fos.write(image);
            fos.flush();
            fos.close();

            QueryBuilder<cu.entumovil.snb.core.db.models.News, ?> queryBuilder = dao.queryBuilder();
            Where<cu.entumovil.snb.core.db.models.News, ?> where = queryBuilder.where();
            where.eq("id", handle.getId());
            cu.entumovil.snb.core.db.models.News jnews = queryBuilder.queryForFirst();
            if (null != jnews) {
                UpdateBuilder<cu.entumovil.snb.core.db.models.News, ?> updateBuilder = dao.updateBuilder();
                updateBuilder.updateColumnValue("pictureLoaded", true);
                updateBuilder.where().eq("id", handle.getId());
                updateBuilder.update();
            }
            imageView.destroyDrawingCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
