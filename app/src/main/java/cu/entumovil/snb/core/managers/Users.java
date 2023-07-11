package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.service.Services;
import cu.entumovil.snb.ui.activities.SplashActivity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Users {

    public Users() {
    }

    public Call<JsonObject> getUserId(long id) {


        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.getUsers(String.valueOf(id));
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> findUserByUserName(String username) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.findUserByUserName(username);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> findVendorByUserName(String username) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.findVendorByUserName(username);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> updateUserBlocked(long id, boolean blocked) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updateUserBlocked(String.valueOf(id),blocked);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> updatePasswordByUsername(String username, String password) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updatePasswordByUsername(username,password);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }
    public Call<JsonArray> updateUserBlocked(long id, String password) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updatePasswordById(String.valueOf(id),password);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }
    public Call<JsonObject> deleteVendor(long id,String phone) {


        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.deleteVendor(String.valueOf(id),phone);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }
    public Call<JsonObject> updateUser(long id, User params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updateUser(String.valueOf(id),params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }


}
