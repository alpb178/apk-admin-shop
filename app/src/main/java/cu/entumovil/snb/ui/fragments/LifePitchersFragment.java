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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.StatsManager;
import cu.entumovil.snb.core.models.LifeTime;
import cu.entumovil.snb.ui.activities.LifetimeActivity;
import cu.entumovil.snb.ui.adapters.LifePitchersAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LifePitchersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + LifePitchersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView lifePitchersRecyclerView;

    private LifePitchersAdapter lifePitchersAdapter;

    public ArrayList<LifeTime> pitchers;

    private int page = 0;

    private SwipeRefreshLayout swipeRefresh;

    public LifePitchersFragment() { }

    public static LifePitchersFragment newInstance() {
        return new LifePitchersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_pitchers, container, false);
        rootView = view;
        pitchers = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        lifePitchersRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_life_pitchers);
        lifePitchersAdapter = new LifePitchersAdapter(pitchers, (LifetimeActivity) getActivity(), lifePitchersRecyclerView);
        lifePitchersRecyclerView.setAdapter(lifePitchersAdapter);
        lifePitchersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        lifePitchersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        lifePitchersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pitchers.add(null);
                lifePitchersAdapter.notifyDataSetChanged();
                pitchers.remove(pitchers.size() - 1);
                page++;
                retrieveData();
                lifePitchersAdapter.setLoaded();
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
                pitchers.clear();
                retrieveData();
            }
        });
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            StatsManager statsManager = new StatsManager();
            final Call<ArrayList<LifeTime>> call = statsManager.loadLifetimeStats(page, 1);
            call.enqueue(new Callback<ArrayList<LifeTime>>() {
                @Override
                public void onResponse(Call<ArrayList<LifeTime>> call, Response<ArrayList<LifeTime>> response) {
                    try {
                        if (null != response.body()) {
                            ArrayList<LifeTime> al = response.body();
                            for (LifeTime l : al) pitchers.add(l);
                            lifePitchersAdapter.animateTo(pitchers);
                        }
                    } catch (Exception e) {
                        TextView noData = (TextView) rootView.findViewById(R.id.noData);
                        noData.setVisibility(View.VISIBLE);
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<LifeTime>> call, Throwable throwable) {
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

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_stats_pitching);
    }
}
