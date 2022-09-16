package cu.entumovil.snb.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.MainManager;
import cu.entumovil.snb.core.models.Record;
import cu.entumovil.snb.ui.activities.RecordActivity;
import cu.entumovil.snb.ui.adapters.RecordsAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordsFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + RecordsFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView recordRecyclerView;

    private RecordsAdapter recordsAdapter;

    public ArrayList<Record> records;

    private int page = 0;

    private String title;

    private SwipeRefreshLayout swipeRefresh;

    public RecordsFragment() { }

    public static RecordsFragment newInstance() {
        return new RecordsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        rootView = view;
        records = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        recordRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_record);
        recordsAdapter = new RecordsAdapter(records, (RecordActivity) getActivity(), recordRecyclerView);
        recordRecyclerView.setAdapter(recordsAdapter);
        recordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recordRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recordsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                records.add(null);
                recordsAdapter.notifyDataSetChanged();
                records.remove(records.size() - 1);
                page++;
                retrieveData();
                recordsAdapter.setLoaded();
            }
        });
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.md_grey_400
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                records.clear();
                retrieveData();
            }
        });
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            MainManager mainManager = new MainManager();
            int type = 0;
            if (title.equals(SNBApp.application.getResources().getString(R.string.label_tab_record_game))) type = 1;
            else if (title.equals(SNBApp.application.getResources().getString(R.string.label_tab_record_special))) type = 2;
            final Call<ArrayList<Record>> call = mainManager.records(page, type);
            call.enqueue(new Callback<ArrayList<Record>>() {
                @Override
                public void onResponse(Call<ArrayList<Record>> call, Response<ArrayList<Record>> response) {
                    try {
                        if (null != response.body()) {
                            ArrayList<Record> rl = response.body();
                            for (Record r : rl) records.add(r);
                            recordsAdapter.animateTo(records);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<Record>> call, Throwable throwable) {
                    page = (page == 0) ? 0 : page--;
                    Toast.makeText(getActivity(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            page = (page == 0) ? 0 : page--;
            Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
            Log.e(TAG, e.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
