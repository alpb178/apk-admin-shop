package cu.entumovil.informationmodule.UI.Fragments;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.informationmodule.R;
import cu.entumovil.informationmodule.Util.ModuleUtil;


/**
 * Created by alejandro.guerra on 04/11/2016.
 */
public class DescriptionFragment extends Fragment implements GestureOverlayView.OnGesturePerformedListener {
    private GestureLibrary gestureLibrary;
    View rootView;
    public DescriptionFragment() {
    }

    public static DescriptionFragment newInstance() {
        DescriptionFragment fragment = new DescriptionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_description,
                container, false);
        this.getActivity().setTitle(R.string.app_details_label);
        this.getActivity().setTitleColor(getResources().getColor(R.color.accent));
        TextView txtDescription = (TextView) rootView.findViewById(R.id.txtDescription);
        txtDescription.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));
        txtDescription.setText(getResources().getString(R.string.app_description_start)+getResources().getString(R.string.app_description_end));

        if(ModuleUtil.appDescriptionTextStartLocal!=null){
            txtDescription.setText(txtDescription.getText().toString().replace(getResources().getString(R.string.app_description_start),ModuleUtil.appDescriptionTextStartLocal));
        }
        if(ModuleUtil.appDescriptionTextEndLocal!=null){
            txtDescription.setText(txtDescription.getText().toString().replace(getResources().getString(R.string.app_description_end),ModuleUtil.appDescriptionTextEndLocal));
        }
        if(ModuleUtil.descriptionTextColorLocal!=-1){
            txtDescription.setTextColor(getResources().getColor(ModuleUtil.descriptionTextColorLocal));
        }

        GestureOverlayView gestureOverlayView = (GestureOverlayView) rootView.findViewById(R.id.gesture_view);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureOverlayView.setGestureColor(Color.TRANSPARENT);
        gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
        gestureLibrary = GestureLibraries.fromRawResource(rootView.getContext(),R.raw.gesture);
        gestureLibrary.load();

        return rootView;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

        for (int i = 0; i < predictions.size(); i++) {
            if(predictions.get(i).score>3.0){
                if(ModuleUtil.UpdateActivity!=null) {
                    Intent intent = new Intent(DescriptionFragment.this.getActivity(), (Class<?>) ModuleUtil.UpdateActivity);
                    startActivity(intent);
                }
                Toast.makeText(rootView.getContext(), predictions.get(i).name,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
