package cu.entumovil.snb.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.MainManager;
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.core.utils.SNBEvent;
import cu.entumovil.snb.ui.adapters.NewsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + NewsActivity.class.getSimpleName();

    final private static int NETWORK_CHECK_INTERVAL = 5000;

    private ArrayList<News> lNews;

    private RecyclerView newsRecyclerView;

    private NewsAdapter newsAdapter;

    private SwipeRefreshLayout swipeRefresh;

    private Dao<cu.entumovil.snb.core.db.models.News, Long> dao;

    private Gson gson;

    private TextView txtConnectionStatus;

    protected boolean isCheckNetworkThreadActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lNews = new ArrayList<>();
        txtConnectionStatus = (TextView) findViewById(R.id.txtConnectionStatus);
        initRecyclerView();
        gson = new Gson();
        try {
            dao = SNBApp.DB_HELPER.getNewsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        retrieveData();
    }

    private void initRecyclerView() {
        newsRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(lNews, this);
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.md_grey_400
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
            }
        });
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            MainManager mainManager = new MainManager();
            final Call<ArrayList<News>> call = mainManager.loadNews();
            call.enqueue(new Callback<ArrayList<News>>() {
                @Override
                public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                    if (null != response.body()) {
                        lNews = response.body();
                        try {
                            DeleteBuilder<cu.entumovil.snb.core.db.models.News, ?> deleteBuilder = dao.deleteBuilder();
                            deleteBuilder.delete();
                            for (News n : lNews) {
                                cu.entumovil.snb.core.db.models.News jnews = new cu.entumovil.snb.core.db.models.News();
                                jnews.setId(n.getId());
                                jnews.setJsonNews(gson.toJson(n));
                                jnews.setPictureLoaded(true);
                                dao.create(jnews);
                            }
                            String path = getApplicationContext().getFilesDir().getAbsolutePath();
                            File f = new File(path);
                            if (f.isDirectory()) {
                                for (File c : f.listFiles()) {
                                    c.delete();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                        newsAdapter.animateTo(lNews);
                        newsRecyclerView.scrollToPosition(0);
                        swipeRefresh.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<News>> call, Throwable throwable) {
                    try {
                        lNews.clear();
                        List<cu.entumovil.snb.core.db.models.News> q = dao.queryForAll();
                        if (!q.isEmpty()) {
                            for (int i = 0; i < q.size(); i++) {
                                cu.entumovil.snb.core.db.models.News qn = q.get(i);
                                Type type = new TypeToken<News>(){}.getType();
                                News n = gson.fromJson(qn.getJsonNews(), type);
                                n.setPictureLoaded(qn.isPictureLoaded());
                                lNews.add(n);
                            }
                            newsAdapter.clear();
                            newsAdapter.animateTo(lNews);
                            newsRecyclerView.scrollToPosition(0);
                        } else {
                            Toast.makeText(NewsActivity.this, "Imposible obtener noticias", Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        Toast.makeText(NewsActivity.this, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    swipeRefresh.setRefreshing(false);
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            try {
                lNews.clear();
                List<cu.entumovil.snb.core.db.models.News> q = dao.queryForAll();
                if (!q.isEmpty()) {
                    for (int i = 0; i < q.size(); i++) {
                        cu.entumovil.snb.core.db.models.News qn = q.get(i);
                        Type type = new TypeToken<News>(){}.getType();
                        News n = gson.fromJson(qn.getJsonNews(), type);
                        n.setPictureLoaded(qn.isPictureLoaded());
                        lNews.add(n);
                    }
                    newsAdapter.clear();
                    newsAdapter.animateTo(lNews);
                    newsRecyclerView.scrollToPosition(0);
                } else {
                    Toast.makeText(NewsActivity.this, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException ex) {
                Toast.makeText(NewsActivity.this, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.getMessage());
            }
            swipeRefresh.setRefreshing(false);
            Log.e(TAG, e.getMessage());
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        SNBApp.BUS.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCheckNetwork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCheckNetwork();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SNBApp.BUS.unregister(this);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            updateConnectionStatusUI(SNBEvent.CONNECTION_LOST);
            return false;
        } else {
            updateConnectionStatusUI(SNBEvent.CONNECTED);
        }
        return true;
    }

    protected void startCheckNetwork() {
        if (!isCheckNetworkThreadActive) {
            isCheckNetworkThreadActive = true;
            Thread checkNetworkThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isCheckNetworkThreadActive) {
                        isNetworkAvailable();
                        try {
                            Thread.sleep(NETWORK_CHECK_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            checkNetworkThread.start();
        }
    }

    protected void stopCheckNetwork() {
        isCheckNetworkThreadActive = false;
    }

    @Subscribe
    public void updateConnectionStatusUI(final SNBEvent snbEvent) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (snbEvent) {
                    case CONNECTION_LOST:
                        txtConnectionStatus.setText(R.string.connection_close);
                        txtConnectionStatus.setVisibility(View.VISIBLE);
                        break;
                    case RECONNECTING:
                        txtConnectionStatus.setText(R.string.connection_reconnect);
                        txtConnectionStatus.setVisibility(View.VISIBLE);
                        break;
                    case CONNECTED:
                        txtConnectionStatus.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

}
