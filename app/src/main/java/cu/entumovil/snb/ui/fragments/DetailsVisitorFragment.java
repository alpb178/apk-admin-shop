package cu.entumovil.snb.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.activities.GameDetailsActivity;
import cu.entumovil.snb.ui.adapters.GameDetailsAdapter;

public class DetailsVisitorFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + DetailsVisitorFragment.class.getSimpleName();

    private Context context;

    private View rootView;

    public RecyclerView visitorRecyclerView;

    public GameDetailsAdapter gameDetailsAdapter;

    public ArrayList players;

    public DetailsVisitorFragment() { }

    public static DetailsVisitorFragment newInstance() {
        DetailsVisitorFragment fragment = new DetailsVisitorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_visitor, container, false);
        rootView = view;
        context = view.getContext();
        initRecyclerView();
        return view;
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_visitor);
    }

    private void initRecyclerView() {
        visitorRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_visitor_list);
        gameDetailsAdapter = new GameDetailsAdapter(players, (GameDetailsActivity) getActivity());
        visitorRecyclerView.setAdapter(gameDetailsAdapter);
        visitorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        visitorRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
