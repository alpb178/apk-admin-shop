package cu.entumovil.snb.ui.activities.admin;

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

import com.google.gson.JsonObject;

import java.io.IOException;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.models.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUser extends AppCompatActivity {

    EditText register_username, register_password, register_repeat_password, register_phone, register_name, register_last_name, register_age;
    TextView text_age;
    RadioGroup radioGroupSex, radioGroupRol;
    RadioButton radioMale, radioFemale, radioAdmin, radioVendor;
    SeekBar seekBar;
    String age = "0";
    ProgressBar swipeRefresh;
    private Button btn_back, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.register_button_back);
        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        register_repeat_password = findViewById(R.id.register_repeat_password);
        register_phone = findViewById(R.id.register_phone);
        register_name = findViewById(R.id.register_name);
        register_last_name = findViewById(R.id.register_last_name);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioGroupRol = findViewById(R.id.radioGroupRol);
        radioAdmin = findViewById(R.id.radioAdmin);
        radioVendor = findViewById(R.id.radioVendor);
        text_age = findViewById(R.id.text_age);
        seekBar = findViewById(R.id.seekBar);
        swipeRefresh = findViewById(R.id.swipeRefresh);


        btn_back.setOnClickListener(view -> {
            onBackPressed();
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


    }

    private void retrieveData() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            enableField();
            cu.entumovil.snb.core.managers.Register teamManager = new cu.entumovil.snb.core.managers.Register();

            String username = register_username.getText().toString();
            String name = register_name.getText().toString();
            String lastName = register_last_name.getText().toString();
            String password = register_password.getText().toString();
            String email = register_username.getText().toString() + "@alexgold.com";
            String rol = "vendedor";
            if (radioAdmin.isChecked()) rol = "Administrador";
            String genre = "Hombre";
            if (radioFemale.isChecked()) genre = "mujer";
            String phone = register_phone.getText().toString();


            Register register = new Register(username, password, email, "", phone, rol, name, lastName, 0, genre, 0, "",false);
            final Call<JsonObject> call = teamManager.register(register);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() == 400) {
                        swipeRefresh.setVisibility(View.GONE);;
                        disableField();
                        String message = "";
                        try {
                            message = response.errorBody().string();
                        } catch (IOException e) {
                            message = "Error en los Datos";
                        }

                        if (message.contains("Email") || message.contains("username"))
                            message = "El Usuario ya esta en uso";
                        if (message.contains("Phone"))
                            message = "El teléfono ya esta en uso";

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 200) {

                         Intent intent = new Intent().setClass(getApplicationContext(), ListVendor.class);
                         startActivity(intent);

                    }
                    swipeRefresh.setVisibility(View.GONE);;
                    disableField();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    swipeRefresh.setVisibility(View.GONE);
                    disableField();
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            swipeRefresh.setVisibility(View.GONE);;
            disableField();
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
        }


    }

    public void enableField() {
        btn_register.setEnabled(true);
        btn_back.setEnabled(true);
        register_username.setEnabled(true);
        register_password.setEnabled(true);
        register_repeat_password.setEnabled(true);
        register_phone.setEnabled(true);
        register_name.setEnabled(true);
        register_last_name.setEnabled(true);
        radioGroupSex.setEnabled(true);
        radioMale.setEnabled(true);
        radioFemale.setEnabled(true);
        radioGroupRol.setEnabled(true);
        radioAdmin.setEnabled(true);
        radioVendor.setEnabled(true);
        text_age.setEnabled(true);
        seekBar.setEnabled(true);

    }

    public void disableField() {
        btn_register.setEnabled(false);
        btn_back.setEnabled(false);
        register_username.setEnabled(false);
        register_password.setEnabled(false);
        register_repeat_password.setEnabled(false);
        register_phone.setEnabled(false);
        register_name.setEnabled(false);
        register_last_name.setEnabled(false);
        radioGroupSex.setEnabled(false);
        radioMale.setEnabled(false);
        radioFemale.setEnabled(false);
        radioGroupRol.setEnabled(false);
        radioAdmin.setEnabled(false);
        radioVendor.setEnabled(false);
        text_age.setEnabled(false);
        seekBar.setEnabled(false);
    }

    void doLogin() {

        if (register_username.getText().toString().equalsIgnoreCase("")) {
            register_username.setError(getString(R.string.required));
            return;
        }

        if (register_password.getText().toString().equalsIgnoreCase("")) {
            register_password.setError(getString(R.string.required));
            return;
        }
        if (register_repeat_password.getText().toString().equalsIgnoreCase("")) {
            register_repeat_password.setError(getString(R.string.required));
            return;
        }
        if (register_phone.getText().toString().equalsIgnoreCase("")) {
            register_phone.setError(getString(R.string.required));
            return;
        }

        if (register_name.getText().toString().equalsIgnoreCase("")) {
            register_name.setError(getString(R.string.required));
            return;
        }

        if (register_last_name.getText().toString().equalsIgnoreCase("")) {
            register_last_name.setError(getString(R.string.required));
            return;
        }

        if (!register_repeat_password.getText().toString().equalsIgnoreCase(register_password.getText().toString())) {
            register_repeat_password.setError(getString(R.string.required_password));
            return;
        }

        if (register_password.getText().toString().length() < 6) {
            register_password.setError(getString(R.string.required_length_password));
            return;
        }


        retrieveData();


    }


}
