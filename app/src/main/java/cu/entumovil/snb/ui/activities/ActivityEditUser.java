package cu.entumovil.snb.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.Users;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditUser extends AppCompatActivity {

    EditText register_name, register_last_name;
    TextView text_age, register_username, register_phone;
    RadioGroup radioGroupSex;
    RadioButton radioMale, radioFemale, radioAdmin, radioVendor;
    SeekBar seekBar;
    String age = "0";
    ProgressBar swipeRefresh;
    User userLogged;
    String username;
    private Button btn_back, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        ///-----------------------------

        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.register_button_back);
        register_username = findViewById(R.id.register_username);
        register_phone = findViewById(R.id.register_phone);
        register_name = findViewById(R.id.register_name);
        register_last_name = findViewById(R.id.register_last_name);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        text_age = findViewById(R.id.text_age);
        seekBar = findViewById(R.id.seekBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        //------------------------------------


        btn_back.setOnClickListener(view -> {

            Intent intent = new Intent().setClass(getApplicationContext(), ActivityProfileUser.class);
            intent.putExtra("userLogged", userLogged);
            startActivity(intent);

        });
        btn_register.setOnClickListener(view ->
                doLogin());
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {
                        age = String.valueOf(progress);
                        text_age.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        retrieveDataUser();
    }

    private void retrieveData() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            disableField();
            Users teamManager = new Users();
            String genre = "Hombre";
            if (radioFemale.isChecked()) genre = "mujer";
            userLogged.setName(register_name.getText().toString());
            userLogged.setLastName(register_last_name.getText().toString());
            userLogged.setGenre(genre);
            userLogged.setAge(Integer.valueOf(text_age.getText().toString()));

            final Call<JsonObject> call = teamManager.updateUser(userLogged.getId(), userLogged);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent().setClass(getApplicationContext(), ActivityProfileUser.class);
                        intent.putExtra("userLogged", userLogged);
                        startActivity(intent);

                    }
                    swipeRefresh.setVisibility(View.GONE);
                    enableField();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    swipeRefresh.setVisibility(View.GONE);
                    enableField();
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            swipeRefresh.setVisibility(View.GONE);
            enableField();
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
        }


    }

    private void retrieveDataUser() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            disableField();
            Users teamManager = new Users();
            final Call<JsonArray> call = teamManager.findUserByUserName(username);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        JsonArray jewls = response.body().getAsJsonArray();
                        for (int x = 0; x < jewls.size(); x++) {
                            JsonElement element = jewls.get(x);
                            userLogged = Utils.RetrieveUser(element);
                            register_username.setText(userLogged.getUsername());
                            register_phone.setText(userLogged.getPhone());
                            register_name.setText(userLogged.getName());
                            register_last_name.setText(userLogged.getLastName());
                            radioFemale.setChecked(true);
                            if (userLogged.getGenre().equalsIgnoreCase("Hombre")) {
                                radioMale.setChecked(true);
                            }
                            text_age.setText(String.valueOf(userLogged.getAge()));


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    }
                    enableField();
                    swipeRefresh.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                    enableField();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
            enableField();

        }

    }

    public void enableField() {
        btn_register.setEnabled(true);
        btn_back.setEnabled(true);
        register_username.setEnabled(true);
        register_phone.setEnabled(true);
        register_name.setEnabled(true);
        register_last_name.setEnabled(true);
        radioGroupSex.setEnabled(true);
        radioMale.setEnabled(true);
        radioFemale.setEnabled(true);
        text_age.setEnabled(true);
        seekBar.setEnabled(true);

    }

    public void disableField() {
        btn_register.setEnabled(false);
        btn_back.setEnabled(false);
        register_username.setEnabled(false);
        register_phone.setEnabled(false);
        register_name.setEnabled(false);
        register_last_name.setEnabled(false);
        radioGroupSex.setEnabled(false);
        radioMale.setEnabled(false);
        radioFemale.setEnabled(false);
        text_age.setEnabled(false);
        seekBar.setEnabled(false);
    }

    void doLogin() {


        if (register_name.getText().toString().equalsIgnoreCase("")) {
            register_name.setError(getString(R.string.required));
            return;
        }

        if (register_last_name.getText().toString().equalsIgnoreCase("")) {
            register_last_name.setError(getString(R.string.required));
            return;
        }


        retrieveData();


    }


}
