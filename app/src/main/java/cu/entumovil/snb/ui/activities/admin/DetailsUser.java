package cu.entumovil.snb.ui.activities.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.Users;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.activities.ActivityListCatalog;
import cu.entumovil.snb.ui.activities.ActivityLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsUser extends AppCompatActivity {


    ProgressBar swipeRefresh;
    ArrayList<String> items = new ArrayList<>();
    private TextView profile_username;
    private TextView profile_email;
    private TextView profile_phone;
    private TextView profile_rol;
    private TextView profile_name;
    private TextView profile_blocked;
    private TextView profile_record;
    private TextView delete_user;
    private TextView edit_user;
    private SharedPreferences sharedPreferences;
    private Button button_profile_back;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_details);
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.get("user");

        // Build elements Layout
        profile_email = findViewById(R.id.profile_email);
        profile_username = findViewById(R.id.profile_username);
        profile_phone = findViewById(R.id.profile_phone);
        profile_rol = findViewById(R.id.profile_rol);
        profile_name = findViewById(R.id.profile_name);
        profile_blocked = findViewById(R.id.profile_blocked);
        TextView close_session = findViewById(R.id.close_session);
        edit_user = findViewById(R.id.edit_user);
        button_profile_back = findViewById(R.id.button_profile_back);
        profile_record = findViewById(R.id.profile_record);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        delete_user = findViewById(R.id.delete_user);
        // fill element
        profile_username.setText(user.getUsername());
        profile_email.setText(user.getEmail());
        profile_phone.setText(user.getPhone());
        profile_rol.setText(user.getRol());
        profile_name.setText(user.getName() + " " + user.getLastName());
        profile_record.setVisibility(View.VISIBLE);
        button_profile_back.setVisibility(View.VISIBLE);

        if (user.getRol().equalsIgnoreCase("vendedor") || user.getRol().equalsIgnoreCase("administrador")) {
            delete_user.setVisibility(View.VISIBLE);
        }

        delete_user.setOnClickListener(view -> deleteUser());


        button_profile_back.setOnClickListener(view1 ->
                onBackPressed()
        );
        profile_record.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SalesHistorical.class);
            intent.putExtra("client", user.getId());
            startActivity(intent);
        });

        close_session.setOnClickListener(view1 -> {
                    startActivity(new Intent().setClass(this, ActivityLogin.class));
                }
        );

        edit_user.setOnClickListener(view -> retrieveData());

        if (user.isBlocked()) {
            edit_user.setText("Desbloquear Usuario");
        } else {
            edit_user.setText("Bloquear Usuario");
        }

        getPhoneVendor();

    }

    private void retrieveData() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            Users teamManager = new Users();
            final Call<JsonArray> call = teamManager.updateUserBlocked(user.getId(), !user.isBlocked);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() != 200) {
                        swipeRefresh.setVisibility(View.GONE);
                        String message = "";
                        try {
                            message = response.errorBody().string();
                        } catch (IOException e) {
                            message = "Error en los Datos";
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {

                        isBlockedUser();


                    }
                    swipeRefresh.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    swipeRefresh.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            swipeRefresh.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
        }


    }

    private void isBlockedUser() {
        user.setBlocked(!user.isBlocked);
        if (user.isBlocked()) {
            edit_user.setText("Desbloquear Usuario");
        } else {
            edit_user.setText("Bloquear Usuario");
        }
    }

    public void deleteUser() {

        final Dialog closeDialog = new Dialog(this);
        closeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        closeDialog.setContentView(R.layout.dialog_delete_user);
        TextView btnOK = closeDialog.findViewById(R.id.btn_ok);
        TextView btnCancel = closeDialog.findViewById(R.id.btn_cancel);
        Spinner dropdown = closeDialog.findViewById(R.id.phone);
        String[] arr = new String[items.size()];
        items.toArray(arr);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        dropdown.setAdapter(adapter);
        closeDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        btnCancel.setOnClickListener(v -> closeDialog.dismiss());
        btnOK.setOnClickListener(v -> {


            retrieveDeleteUser(user.getId(), dropdown.getSelectedItem().toString(), closeDialog);

        });
        closeDialog.show();

    }

    private void retrieveDeleteUser(long id, String phone, Dialog dialog) {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            Users teamManager = new Users();
            final Call<JsonObject> call = teamManager.deleteVendor(id, phone);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.code() != 200) {

                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else {

                        dialog.dismiss();
                        startActivity(new Intent().setClass(DetailsUser.this, ActivityListCatalog.class));
                        finish();

                    }
                    swipeRefresh.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.w("E", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
        }

    }

    private void getPhoneVendor() {

        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            Users teamManager = new Users();
            String username = "all";
            final Call<JsonArray> call = teamManager.findUserByUserName(username);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (null != response.body()) {
                        JsonArray users = response.body().getAsJsonArray();

                        for (int x = 0; x < users.size(); x++) {
                            JsonElement element = users.get(x);
                            User user = Utils.RetrieveUser(element);
                            if (user.getRol().equalsIgnoreCase("vendedor") || user.getRol().equalsIgnoreCase("administrador"))
                                items.add(user.getPhone());

                        }

                        swipeRefresh.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);

        }

    }

}