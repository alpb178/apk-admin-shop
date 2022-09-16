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
import cu.entumovil.snb.core.models.Fielder;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.adapters.StatsFieldersAdapter;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsFieldersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + StatsFieldersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView statsFieldersRecyclerView;

    private StatsFieldersAdapter statsFieldersAdapter;

    public ArrayList<Fielder> fielders;

    private int page = 0;

    private static StatsType STATSTYPE;

    private SwipeRefreshLayout swipeRefresh;

    public StatsFieldersFragment() { }

    public static StatsFieldersFragment newInstance(StatsType statsType) {
        StatsFieldersFragment.STATSTYPE = statsType;
        return new StatsFieldersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_fielders, container, false);
        rootView = view;
        fielders = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        statsFieldersRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_stats_fielders);
        statsFieldersAdapter = new StatsFieldersAdapter(fielders, (StatsActivity) getActivity(), statsFieldersRecyclerView, STATSTYPE);
        statsFieldersRecyclerView.setAdapter(statsFieldersAdapter);
        statsFieldersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        statsFieldersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!StatsType.COLLECTIVE.equals(StatsFieldersFragment.STATSTYPE)) {
            statsFieldersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    fielders.add(null);
                    statsFieldersAdapter.notifyDataSetChanged();
                    fielders.remove(fielders.size() - 1);
                    page++;
                    retrieveData();
                    statsFieldersAdapter.setLoaded();
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
                fielders.clear();
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
            final Call<ArrayList<Fielder>> call = statsManager.loadFieldersStats(page, StatsFieldersFragment.STATSTYPE, 1);
            call.enqueue(new Callback<ArrayList<Fielder>>() {
                @Override
                public void onResponse(Call<ArrayList<Fielder>> call, Response<ArrayList<Fielder>> response) {
                    try {
                        ArrayList<Fielder> al = response.body();
                        if (al.size() > 0) {
                            for (Fielder f : al) fielders.add(f);
                            statsFieldersAdapter.animateTo(fielders);
                        }
                    } catch (Exception e) {
                        TextView noData = (TextView) rootView.findViewById(R.id.noData);
                        noData.setVisibility(View.VISIBLE);
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<Fielder>> call, Throwable throwable) {
                    page = (page == 0) ? 0 : page--;
                    Toast.makeText(getContext(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
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
        return SNBApp.application.getResources().getString(R.string.label_tab_stats_fielding);
    }
}
