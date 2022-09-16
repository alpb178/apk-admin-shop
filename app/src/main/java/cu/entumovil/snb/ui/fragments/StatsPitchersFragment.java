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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.managers.StatsManager;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.adapters.StatsPitchersAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsPitchersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + StatsPitchersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView statsPitchersRecyclerView;

    private StatsPitchersAdapter statsPitchersAdapter;

    public ArrayList<Pitcher> pitchers;

    private int page = 0;

    private static StatsType STATSTYPE;

    private LinearLayout layoutStatsPitchersLegendNormal, layoutStatsPitchersLegendSpecial;

    private SwipeRefreshLayout swipeRefresh;

    public StatsPitchersFragment() { }

    public static StatsPitchersFragment newInstance(StatsType statsType) {
        StatsPitchersFragment.STATSTYPE = statsType;
        return new StatsPitchersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_pitchers, container, false);
        layoutStatsPitchersLegendNormal = (LinearLayout) view.findViewById(R.id.layoutStatsPitchersLegendNormal);
        layoutStatsPitchersLegendSpecial = (LinearLayout) view.findViewById(R.id.layoutStatsPitchersLegendSpecial);
        if (StatsType.SINGLE.equals(STATSTYPE)) {
            layoutStatsPitchersLegendNormal.setVisibility(View.VISIBLE);
            layoutStatsPitchersLegendSpecial.setVisibility(View.GONE);
        } else if (StatsType.SPECIALIZED.equals(STATSTYPE)) {
            layoutStatsPitchersLegendNormal.setVisibility(View.GONE);
            layoutStatsPitchersLegendSpecial.setVisibility(View.VISIBLE);
        }
        rootView = view;
        pitchers = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        statsPitchersRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_stats_pitchers);
        statsPitchersAdapter = new StatsPitchersAdapter(pitchers, (StatsActivity) getActivity(), statsPitchersRecyclerView, StatsPitchersFragment.STATSTYPE);
        statsPitchersRecyclerView.setAdapter(statsPitchersAdapter);
        statsPitchersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        statsPitchersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!StatsType.COLLECTIVE.equals(StatsPitchersFragment.STATSTYPE)) {
            statsPitchersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    pitchers.add(null);
                    statsPitchersAdapter.notifyDataSetChanged();
                    pitchers.remove(pitchers.size() - 1);
                    page++;
                    retrieveData();
                    statsPitchersAdapter.setLoaded();
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
                pitchers.clear();
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
            final Call<ArrayList<Pitcher>> call = statsManager.loadPitchersStats(page, StatsPitchersFragment.STATSTYPE, 1);
            call.enqueue(new Callback<ArrayList<Pitcher>>() {
                @Override
                public void onResponse(Call<ArrayList<Pitcher>> call, Response<ArrayList<Pitcher>> response) {
                    try {
                        if (null != response.body()) {
                            ArrayList<Pitcher> al = response.body();
                            for (Pitcher p : al) pitchers.add(p);
                            statsPitchersAdapter.animateTo(pitchers);
                        }
                    } catch (Exception e) {
                        TextView noData = (TextView) rootView.findViewById(R.id.noData);
                        noData.setVisibility(View.VISIBLE);
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<Pitcher>> call, Throwable throwable) {
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
