package cu.entumovil.snb.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class PlayerDetailsFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + PlayerDetailsFragment.class.getSimpleName();

    private View rootView;

    public PlayerDetailsFragment() { }

    public static PlayerDetailsFragment newInstance() {
        PlayerDetailsFragment fragment = new PlayerDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_details, container, false);
        rootView = view;
        return view;
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_details);
    }
}
