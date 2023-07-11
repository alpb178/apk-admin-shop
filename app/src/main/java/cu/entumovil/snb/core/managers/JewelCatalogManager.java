package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cu.entumovil.snb.core.models.Data;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPost;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPut;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.service.Services;
import cu.entumovil.snb.ui.activities.SplashActivity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JewelCatalogManager {

    public JewelCatalogManager() { }

    public Call<JsonArray> findJewelCatalogueByModel(String model) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(Utils.client())
                    .build();
            Services restService = service.create(Services.class);
            return restService.findJewelCatalogueByModel(model);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> updateJewelCatalogue(long id, Data.DataUpdate params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updateJewelCatalogue(String.valueOf(id),params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> createJewelCatalog(JewelCatalogPost params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.createJewelCatalog(params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

    public Call<JsonObject> updateJewelCatalog(long id, JewelCatalogPut params) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Services restService = service.create(Services.class);
            return restService.updateJewelCatalog(String.valueOf(id),params);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }



}
