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
import cu.entumovil.snb.core.managers.StatsManager;
import cu.entumovil.snb.core.models.SerieLeader;
import cu.entumovil.snb.ui.activities.LeadersActivity;
import cu.entumovil.snb.ui.adapters.LeadersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadersFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + LeadersFragment.class.getSimpleName();

    private View rootView;

    private RecyclerView leaderRecyclerView;

    private LeadersAdapter leadersAdapter;

    public ArrayList<SerieLeader> leaders;

    private String title;

    private SwipeRefreshLayout swipeRefresh;

    public LeadersFragment() { }

    public static LeadersFragment newInstance() {
        return new LeadersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaders, container, false);
        rootView = view;
        leaders = new ArrayList<>();
        initRecyclerView();
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        leaderRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_leader);
        leadersAdapter = new LeadersAdapter(leaders, (LeadersActivity) getActivity());
        leaderRecyclerView.setAdapter(leadersAdapter);
        leaderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        leaderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.md_grey_400
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leaders.clear();
                retrieveData();
            }
        });
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            StatsManager statsManager = new StatsManager();
            final Call<ArrayList<SerieLeader>> call = statsManager.serieLeader(title.equals(SNBApp.application.getResources().getString(R.string.label_tab_stats_hitting)) ? 0 : 1);
            call.enqueue(new Callback<ArrayList<SerieLeader>>() {
                @Override
                public void onResponse(Call<ArrayList<SerieLeader>> call, Response<ArrayList<SerieLeader>> response) {
                    if (null != response.body()) {
                        leaders = response.body();
                        leadersAdapter.clear();
                        leadersAdapter.animateTo(leaders);
                        leaderRecyclerView.scrollToPosition(0);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.message_not_exists_info), Toast.LENGTH_SHORT).show();
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ArrayList<SerieLeader>> call, Throwable throwable) {
                    Toast.makeText(getContext(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
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
