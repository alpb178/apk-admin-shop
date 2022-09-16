package cu.entumovil.snb.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Favorite;
import cu.entumovil.snb.ui.activities.MainActivity;
import cu.entumovil.snb.ui.adapters.FavoriteAdapter;

public class FavoritesFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + FavoritesFragment.class.getSimpleName();

    private FavoritesFragment fragment;

    private Context context;

    private View rootView;

    private Dao<Favorite, Long> favoritesDao;

    private List<Favorite> favorites;

    private LinearLayout layoutNotFavorites;

    private RecyclerView mRecyclerFavorites;

    private FavoriteAdapter mFavoriteAdapter;

    public FavoritesFragment() { }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        rootView = view;
        context = view.getContext();
        layoutNotFavorites = (LinearLayout) rootView.findViewById(R.id.layoutNotFavorites);
        initRecycleView();

        try {
            favoritesDao = SNBApp.DB_HELPER.getFavoritesDao();
            favorites = favoritesDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }

        if (favorites.size() > 0) {
            layoutNotFavorites.setVisibility(View.GONE);
            mRecyclerFavorites.setVisibility(View.VISIBLE);
            mFavoriteAdapter.clear();
            mFavoriteAdapter.animateTo(favorites);
            mRecyclerFavorites.scrollToPosition(0);
        }
        return view;
    }

    private void initRecycleView() {
        mRecyclerFavorites = (RecyclerView) rootView.findViewById(R.id.recycler_favorites_list);
        mFavoriteAdapter = new FavoriteAdapter(favorites, (MainActivity) getActivity());
        mRecyclerFavorites.setAdapter(mFavoriteAdapter);
        mRecyclerFavorites.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerFavorites.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFavorites.setHasFixedSize(true);
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_favorites);
    }
}
