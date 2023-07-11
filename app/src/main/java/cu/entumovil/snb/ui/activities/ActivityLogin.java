package cu.entumovil.snb.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.Register;
import cu.entumovil.snb.core.models.ResetPassword;
import cu.entumovil.snb.core.models.login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {


    EditText login_username, login_password;
    TextView login_forget_password;
    Button btn_login;
    private ProgressBar swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login);
        login_forget_password = findViewById(R.id.login_forget_password);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        swipeRefresh = findViewById(R.id.swipeRefreshAlexGoldList);

        btn_login.setOnClickListener(view -> {
            doLogin();

        });

        login_forget_password.setOnClickListener(view -> forgetPassword());

    }

    void doLogin() {

        if (login_username.getText().toString().equalsIgnoreCase("")) {
            login_username.setError("Username Rquerido!");
            return;
        }

        if (login_password.getText().toString().equalsIgnoreCase("")) {
            login_password.setError("Contraseña Requerida");
            return;
        }

        retrieveData();


    }

    public void disabled() {
        login_username.setEnabled(false);
        login_password.setEnabled(false);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    public void enabled() {
        login_username.setEnabled(true);
        login_password.setEnabled(true);
        swipeRefresh.setVisibility(View.GONE);
    }

    private void retrieveData() {
        try {
            disabled();
            Register teamManager = new Register();
            final Call<JsonObject> call = teamManager.login(new login(login_username.getText().toString(), login_password.getText().toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.code() == 400) {
                        enabled();
                        Toast.makeText(getApplicationContext(), "Error en las Credenciales", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 200) {
                        JsonElement selected = response.body();
                        JsonObject user = selected.getAsJsonObject().get("user").getAsJsonObject();
                        String rol = user.getAsJsonObject().get("rol").getAsString();
                        if (rol.equalsIgnoreCase("administrador")) {

                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("id", user.getAsJsonObject().get("id").getAsInt());
                            editor.putString("username", user.getAsJsonObject().get("username").getAsString());
                            editor.putString("name", user.getAsJsonObject().get("name").getAsString());
                            editor.putString("lastName", user.getAsJsonObject().get("lastName").getAsString());
                            editor.putString("email", user.getAsJsonObject().get("email").getAsString());
                            editor.putString("vendor", user.getAsJsonObject().get("vendedor").getAsString());
                            editor.putString("phone", user.getAsJsonObject().get("phone").getAsString());
                            editor.putString("rol", rol);
                            editor.commit();
                            startActivity(new Intent().setClass(ActivityLogin.this, ActivityListCatalog.class));
                            finish();
                        } else {
                            enabled();
                            Toast.makeText(getApplicationContext(), "Este usuario no tiene acceso a esta aplicación", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    enabled();
                }
            });
        } catch (Exception e) {
            Log.w("E", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            enabled();
        }

    }

    public void forgetPassword() {

        final Dialog closeDialog = new Dialog(this);
        closeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        closeDialog.setContentView(R.layout.dialog_forget_password);
        TextView btnOK = closeDialog.findViewById(R.id.btn_ok);
        TextView btnCancel = closeDialog.findViewById(R.id.btn_cancel);
        TextView forget_username = closeDialog.findViewById(R.id.forget_username);
        TextView forget_password = closeDialog.findViewById(R.id.forget_password);
        TextView forget_password_repeat = closeDialog.findViewById(R.id.forget_password_repeat);
        closeDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        btnCancel.setOnClickListener(v -> closeDialog.dismiss());
        btnOK.setOnClickListener(v -> {
            if (forget_username.getText().toString().equalsIgnoreCase("") || forget_password.getText().toString().equalsIgnoreCase("")
                    || forget_password_repeat.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Todos los campos son Obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!forget_password_repeat.getText().toString().equalsIgnoreCase(forget_password.getText().toString())) {
                forget_password_repeat.setError("Las contraseñas deben ser iguales");
                return;
            }

            retrieveForgetPassword(forget_username.getText().toString(), forget_password.getText().toString(), closeDialog);

        });
        closeDialog.show();

    }

    private void retrieveForgetPassword(String username, String password, Dialog dialog) {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.VISIBLE);
            Register teamManager = new Register();
            final Call<Integer> call = teamManager.findIdByUserName(username);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {

                    if (response.code() != 200) {

                        Toast.makeText(getApplicationContext(), "Error en las Credenciales", Toast.LENGTH_SHORT).show();
                    } else {

                        updatePassword(password, response.body(), dialog);
                    }
                    swipeRefresh.setVisibility(View.GONE);

                    btn_login.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);

                }
            });
        } catch (Exception e) {
            Log.w("E", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);

        }

    }

    private void updatePassword(String password, int id, Dialog dialog) {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
            Register teamManager = new Register();
            final Call<JsonObject> call = teamManager.resetPass(id, new ResetPassword(password));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.code() != 200) {

                        Toast.makeText(getApplicationContext(), "Error en las Credenciales", Toast.LENGTH_SHORT).show();
                    } else {

                        dialog.dismiss();

                    }
                    swipeRefresh.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);

                }
            });
        } catch (Exception e) {
            Log.w("E", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);

        }

    }
}