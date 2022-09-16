package cu.entumovil.snb.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.StatsManager;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.adapters.StatsHittersAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsHittersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + StatsHittersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView statsHittingRecyclerView;

    private StatsHittersAdapter statsHittersAdapter;

    public ArrayList<Hitter> hitters;

    private int page = 0;

    public static StatsType STATSTYPE;

    private int teamId;

    private LinearLayout layoutStatsHittersLegendNormal, layoutStatsHittersLegendSpecial;

    private SwipeRefreshLayout swipeRefresh;

    private int orderBy;

    public StatsHittersFragment() { }

    public static StatsHittersFragment newInstance(StatsType statsType) {
        StatsHittersFragment.STATSTYPE = statsType;
        return new StatsHittersFragment();
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_hitters, container, false);
        setHasOptionsMenu(true);
        layoutStatsHittersLegendNormal = (LinearLayout) view.findViewById(R.id.layoutStatsHittersLegendNormal);
        layoutStatsHittersLegendSpecial = (LinearLayout) view.findViewById(R.id.layoutStatsHittersLegendSpecial);
        if (StatsType.SINGLE.equals(STATSTYPE)) {
            layoutStatsHittersLegendNormal.setVisibility(View.VISIBLE);
            layoutStatsHittersLegendSpecial.setVisibility(View.GONE);
        } else if (StatsType.SPECIALIZED.equals(STATSTYPE)) {
            layoutStatsHittersLegendNormal.setVisibility(View.GONE);
            layoutStatsHittersLegendSpecial.setVisibility(View.VISIBLE);
        }
        rootView = view;
        hitters = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        statsHittingRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_stats_hitters);
        statsHittersAdapter = new StatsHittersAdapter(hitters, (StatsActivity) getActivity(), statsHittingRecyclerView, StatsHittersFragment.STATSTYPE);
        statsHittingRecyclerView.setAdapter(statsHittersAdapter);
        statsHittingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        statsHittingRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!StatsType.COLLECTIVE.equals(StatsHittersFragment.STATSTYPE)) {
            statsHittersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    hitters.add(null);
                    statsHittersAdapter.notifyDataSetChanged();
                    hitters.remove(hitters.size() - 1);
                    page++;
                    retrieveData();
                    statsHittersAdapter.setLoaded();
                }
            });
        }
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
            //TODO: poner el ID del equipo que se selecciono para mostrar las estadisticas
            //TODO: hay que refrescar el reciclerView e inicializar page = 0
            final Call<ArrayList<Hitter>> call = statsManager.loadHittersStats(page, StatsHittersFragment.STATSTYPE, getTeamId());
            call.enqueue(new Callback<ArrayList<Hitter>>() {
                @Override
                public void onResponse(Call<ArrayList<Hitter>> call, Response<ArrayList<Hitter>> response) {
                    try {
                        if (null != response.body()) {
                            ArrayList<Hitter> al = response.body();
                            for (Hitter h : al) hitters.add(h);
                            statsHittersAdapter.animateTo(hitters);
                        }
                    } catch (Exception e) {
                        TextView noData = (TextView) rootView.findViewById(R.id.noData);
                        noData.setVisibility(View.VISIBLE);
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<Hitter>> call, Throwable throwable) {
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

    public void reloadFragment(boolean clean) {
        page = 0;
        statsHittersAdapter.setStatsType(clean ? StatsType.COLLECTIVE : StatsType.SINGLE);
        StatsHittersFragment.STATSTYPE = clean ? StatsType.COLLECTIVE : StatsType.SINGLE;
        statsHittersAdapter.clear();
        retrieveData();
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_stats_hitting);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_orderbyAVE) {
            page = 0;
            orderBy = 0;
            hitters.clear();
            retrieveData();
        } else if (id == R.id.menu_orderbyHR) {
            page = 0;
            orderBy = 0;
            hitters.clear();
            retrieveData();
        } else if (id == R.id.menu_orderbyCI) {
            page = 0;
            orderBy = 0;
            hitters.clear();
            retrieveData();
        } else if (id == R.id.menu_orderbyBB) {
            page = 0;
            orderBy = 0;
            hitters.clear();
            retrieveData();
        }
        return super.onOptionsItemSelected(item);
    }

}
