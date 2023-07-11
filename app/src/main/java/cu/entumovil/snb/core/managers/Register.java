package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.JsonObject;

import cu.entumovil.snb.core.models.ResetPassword;
import cu.entumovil.snb.core.models.login;
import cu.entumovil.snb.service.Services;
import cu.entumovil.snb.ui.activities.SplashActivity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register {

    public Register() { }

    public Call<JsonObject> login(login login) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.login(login);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> register(cu.entumovil.snb.core.models.Register register) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.register(register);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> resetPass(long id, ResetPassword password) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.resetPass(String.valueOf(id),password);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<Integer> findIdByUserName(String username) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.findIdByUserName(username);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }


}
