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
import cu.entumovil.snb.ui.adapters.LifeHittersAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LifeHittersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + LifeHittersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView lifeHittingRecyclerView;

    private LifeHittersAdapter lifeHittersAdapter;

    public ArrayList<LifeTime> hitters;

    private int page = 0;

    private SwipeRefreshLayout swipeRefresh;

    public LifeHittersFragment() { }

    public static LifeHittersFragment newInstance() {
        return new LifeHittersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_hitters, container, false);
        rootView = view;
        hitters = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        lifeHittingRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_life_hitters);
        lifeHittersAdapter = new LifeHittersAdapter(hitters, (LifetimeActivity) getActivity(), lifeHittingRecyclerView);
        lifeHittingRecyclerView.setAdapter(lifeHittersAdapter);
        lifeHittingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        lifeHittingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        lifeHittersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                hitters.add(null);
                lifeHittersAdapter.notifyDataSetChanged();
                hitters.remove(hitters.size() - 1);
                page++;
                retrieveData();
                lifeHittersAdapter.setLoaded();
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
                hitters.clear();
                retrieveData();
            }
        });
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            StatsManager statsManager = new StatsManager();
            final Call<ArrayList<LifeTime>> call = statsManager.loadLifetimeStats(page, 0);
            call.enqueue(new Callback<ArrayList<LifeTime>>() {
                @Override
                public void onResponse(Call<ArrayList<LifeTime>> call, Response<ArrayList<LifeTime>> response) {
                    try {
                        if (null != response.body()) {
                            ArrayList<LifeTime> al = response.body();
                            for (LifeTime l : al) hitters.add(l);
                            lifeHittersAdapter.animateTo(hitters);
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
        return SNBApp.application.getResources().getString(R.string.label_tab_stats_hitting);
    }
}
