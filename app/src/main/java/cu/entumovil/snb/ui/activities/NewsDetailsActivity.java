package cu.entumovil.snb.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.MainManager;
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.core.utils.CustomImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + NewsDetailsActivity.class.getSimpleName();

    public static final String INTENT_NEWS_HOLDER = "INTENT_NEWS_HOLDER";

    private News news;

    private ProgressBar spinnerProgressBar;

    private TextView title, text;

    private ImageView img_news_card;

    private Dao<cu.entumovil.snb.core.db.models.News, Long> dao;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        news = (News) getIntent().getExtras().get(INTENT_NEWS_HOLDER);
        spinnerProgressBar = (ProgressBar) findViewById(R.id.spinnerProgressBar);
        spinnerProgressBar.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.title);
        text = (TextView )findViewById(R.id.text);
        img_news_card = (ImageView) findViewById(R.id.img_news_card);
        gson = new Gson();
        try {
            dao = SNBApp.DB_HELPER.getNewsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
          @Override
          public void run() {
              retriveData();
          }
        }).run();
    }

    private void retriveData() {
        MainManager mainManager = new MainManager();
        final Call<ArrayList<News>> call = mainManager.newsById(news.getId());
         call.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                ArrayList<News> l = response.body();
                title.setText(Html.fromHtml(news.getTit()));
                text.setText(Html.fromHtml(l.get(0).getNot()));
                if (null != news.getFoto()) {
                    img_news_card.setVisibility(View.VISIBLE);
                    Glide.with(getBaseContext())
                            .load(getResources().getString(R.string.IMAGE_URL_NEWS).concat(news.getFoto()))
                            .centerCrop()
                            .placeholder(R.drawable.img_default_news)
                            .into(img_news_card);
                } else {
                    img_news_card.setVisibility(View.GONE);
                }

                try {
                    QueryBuilder<cu.entumovil.snb.core.db.models.News, ?> queryBuilder = dao.queryBuilder();
                    Where<cu.entumovil.snb.core.db.models.News, ?> where = queryBuilder.where();
                    where.eq("id", news.getId());
                    cu.entumovil.snb.core.db.models.News jnews = queryBuilder.queryForFirst();
                    if (null != jnews) {
                        JSONObject json = new JSONObject(jnews.getJsonNews());
                        json.put("Not", l.get(0).getNot());
                        UpdateBuilder<cu.entumovil.snb.core.db.models.News, ?> updateBuilder = dao.updateBuilder();
                        updateBuilder.updateColumnValue("jsonNews", json.toString());
                        updateBuilder.where().eq("id", news.getId());
                        updateBuilder.update();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                spinnerProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable throwable) {
                try {
                    QueryBuilder<cu.entumovil.snb.core.db.models.News, ?> queryBuilder = dao.queryBuilder();
                    Where<cu.entumovil.snb.core.db.models.News, ?> where = queryBuilder.where();
                    where.eq("id", news.getId());
                    cu.entumovil.snb.core.db.models.News jnews = queryBuilder.queryForFirst();
                    if (null != jnews) {
                        FileInputStream fis = openFileInput(news.getFoto());
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        CustomImage ri = new CustomImage(bitmap, false);
                        img_news_card.setImageDrawable(ri);
                        img_news_card.setVisibility(View.VISIBLE);
                        fis.close();
                        JSONObject json = new JSONObject(jnews.getJsonNews());
                        title.setText(Html.fromHtml(news.getTit()));
                        text.setText(Html.fromHtml(json.getString("Not")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                spinnerProgressBar.setVisibility(View.GONE);
                Toast.makeText(NewsDetailsActivity.this,"Imposible obtener noticia", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
