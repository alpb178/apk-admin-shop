package cu.entumovil.snb.ui.fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Cache;
import cu.entumovil.snb.core.managers.TeamManager;
import cu.entumovil.snb.core.models.Qualification;
import cu.entumovil.snb.ui.activities.MainActivity;
import cu.entumovil.snb.ui.adapters.QualificationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QualificationsFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + QualificationsFragment.class.getSimpleName();

    private QualificationsFragment fragment;

    private Context context;

    private RecyclerView qualificationRecyclerView;

    private QualificationAdapter qualificationAdapter;

    public ArrayList<Qualification> teams;

    private View rootView;

    private Dao<Cache, Long> dao;

    private Gson gson;

    private SwipeRefreshLayout swipeRefresh;

    public QualificationsFragment() { }

    public static QualificationsFragment newInstance() {
        QualificationsFragment fragment = new QualificationsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qualification, container, false);
        rootView = view;
        context = view.getContext();
        initRecyclerView();
        gson = new Gson();
        try {
            dao = SNBApp.DB_HELPER.getCachesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        qualificationRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_position_list);
        qualificationAdapter = new QualificationAdapter(teams, (MainActivity) getActivity());
        qualificationRecyclerView.setAdapter(qualificationAdapter);
        qualificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        qualificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshQualification);
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
            TeamManager teamManager = new TeamManager();
            final Call<ArrayList<Qualification>> call = teamManager.positions();
            call.enqueue(new Callback<ArrayList<Qualification>>() {
                @Override
                public void onResponse(Call<ArrayList<Qualification>> call, Response<ArrayList<Qualification>> response) {
                    if (null != response.body()) {
                        teams = response.body();
                        qualificationAdapter.clear();
                        qualificationRecyclerView.setVisibility(View.VISIBLE);
                        qualificationAdapter.animateTo(teams);
                        qualificationRecyclerView.scrollToPosition(0);

                        try {
                            List q = dao.queryForEq("guiType", "qualification");
                            if (!q.isEmpty()) {
                                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                                deleteBuilder.where().eq("id", ((Cache) q.get(0)).getId());
                                deleteBuilder.delete();
                            }
                            dao.create(new Cache("qualification", gson.toJson(teams)));
                        } catch (SQLException e) {
                            Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Qualification>> call, Throwable throwable) {
                    try {
                        List q = dao.queryForEq("guiType", "qualification");
                        if (!q.isEmpty()) {
                            Type listType = new TypeToken<ArrayList<Qualification>>(){}.getType();
                            teams = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                            qualificationAdapter.clear();
                            qualificationRecyclerView.setVisibility(View.VISIBLE);
                            qualificationAdapter.animateTo(teams);
                            qualificationRecyclerView.scrollToPosition(0);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    swipeRefresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            try {
                List q = dao.queryForEq("guiType", "qualification");
                if (!q.isEmpty()) {
                    Type listType = new TypeToken<ArrayList<Qualification>>(){}.getType();
                    teams = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                    qualificationAdapter.clear();
                    qualificationRecyclerView.setVisibility(View.VISIBLE);
                    qualificationAdapter.animateTo(teams);
                    qualificationRecyclerView.scrollToPosition(0);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException ex) {
                Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.getMessage());
            }
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_qualification);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = qualificationAdapter.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            default:
                return super.onContextItemSelected(item);
        }
    }

}
