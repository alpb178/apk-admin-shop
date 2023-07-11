package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cu.entumovil.snb.core.models.Data;
import cu.entumovil.snb.service.Services;
import cu.entumovil.snb.ui.activities.SplashActivity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JewelManager {

    public JewelManager() { }



    public Call<JsonObject> createJewel(Data params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.createJewel(params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> updateJewel(long id,Data.DataUpdateJewelSales params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updateJewel(String.valueOf(id),params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> deleteJewel(long id) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.deleteJewels(String.valueOf(id));
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> findJewelByVendor(long id) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.findJewelByVendor(String.valueOf(id));
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonArray> findByJewelByUser(long id) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.findByJewelByUser(String.valueOf(id));
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

}
