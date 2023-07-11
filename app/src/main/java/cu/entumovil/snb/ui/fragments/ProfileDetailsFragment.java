package cu.entumovil.snb.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cu.entumovil.snb.R;
import cu.entumovil.snb.ui.activities.ActivityEditUser;
import cu.entumovil.snb.ui.activities.ActivityLogin;

public class ProfileDetailsFragment extends Fragment {
    TextView profile_username, profile_email, profile_phone, profile_rol, profile_name, close_session, edit_user;
    private View rootView;


    public ProfileDetailsFragment() {

    }

    public static ProfileDetailsFragment newInstance(
    ) {
        return new ProfileDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_details, container, false);
        rootView = view;
        Context context = view.getContext();
        setHasOptionsMenu(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // Build elements Layout
        profile_email = rootView.findViewById(R.id.profile_email);
        profile_username = rootView.findViewById(R.id.profile_username);
        profile_phone = rootView.findViewById(R.id.profile_phone);
        profile_rol = rootView.findViewById(R.id.profile_rol);
        profile_name = rootView.findViewById(R.id.profile_name);
        close_session = rootView.findViewById(R.id.close_session);
        edit_user = rootView.findViewById(R.id.edit_user);
        // fill element
        profile_username.setText(sharedPreferences.getString("username", ""));
        profile_email.setText(sharedPreferences.getString("email", ""));
        profile_phone.setText(sharedPreferences.getString("phone", ""));
        profile_rol.setText(sharedPreferences.getString("rol", ""));
        profile_name.setText(sharedPreferences.getString("name", "") + " " + sharedPreferences.getString("lastName", ""));
        close_session.setOnClickListener(view1 -> {
                    startActivity(new Intent().setClass(getContext(), ActivityLogin.class));
                }
        );

        edit_user.setOnClickListener(view1 -> startActivity(new Intent().setClass(getContext(), ActivityEditUser.class)));

        return view;
    }

    @Override
    public String toString() {
        return "Ajustes";
    }


}
