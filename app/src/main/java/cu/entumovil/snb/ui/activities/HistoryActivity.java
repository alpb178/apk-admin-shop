package cu.entumovil.snb.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.MainManager;
import cu.entumovil.snb.core.models.History;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.adapters.HistoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + HistoryActivity.class.getSimpleName();

    private TextView txt_history_date;

    private ArrayList<History> lHistory;

    private RecyclerView historyRecyclerView;

    private HistoryAdapter historyAdapter;

    private int i;

    private ProgressBar spinnerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_history_date = (TextView) findViewById(R.id.txt_history_date);
        Calendar calendar = Calendar.getInstance();
        txt_history_date.setText(Utils.historyDate(calendar.getTime()));

        spinnerProgressBar = (ProgressBar) findViewById(R.id.spinnerProgressBar);
        spinnerProgressBar.setVisibility(View.VISIBLE);

        initRecyclerView();
        retrieveData();
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

    private void initRecyclerView() {
        historyAdapter = new HistoryAdapter(lHistory, this);
        historyRecyclerView = (RecyclerView) findViewById(R.id.history_list);
        historyRecyclerView.setAdapter(historyAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrieveData() {
        try {
            final MainManager mainManager = new MainManager();
            final Call<ArrayList<History>> historiesCall = mainManager.loadHistories();
            historiesCall.enqueue(new Callback<ArrayList<History>>() {
                @Override
                public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                    if (null != response.body()) {
                        lHistory = response.body();
                        if (null != lHistory.get(0).getId()) {
                            reloadHistories(mainManager);
                        } else {
                            Toast.makeText(HistoryActivity.this, getString(R.string.message_not_exists_history), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HistoryActivity.this, getString(R.string.message_not_exists_history), Toast.LENGTH_SHORT).show();
                    }
                    spinnerProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onFailure(Call<ArrayList<History>> call, Throwable throwable) {
                    Toast.makeText(HistoryActivity.this, getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                    spinnerProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(HistoryActivity.this, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            spinnerProgressBar.setVisibility(View.GONE);
            Log.e(TAG, e.getMessage());
        }
    }

    private void reloadHistories(MainManager mainManager) {
        for (History h : lHistory) {
            final Call<ArrayList<History>> hitoryCall = mainManager.loadHistoryById(h.getId());
            hitoryCall.enqueue(new Callback<ArrayList<History>>() {
                @Override
                public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                    historyAdapter.addItem(historyAdapter.getItemCount(), response.body().get(0));
                    historyRecyclerView.scrollToPosition(0);
                }
                @Override
                public void onFailure(Call<ArrayList<History>> call, Throwable throwable) {
                    Toast.makeText(HistoryActivity.this, getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, throwable.getMessage());
                }
            });
        }
    }

}
