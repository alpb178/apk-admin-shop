package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.JsonArray;

import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.service.Services;
import cu.entumovil.snb.ui.activities.SplashActivity;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageManager {

    public ImageManager() {
    }

    public Call<JsonArray> uploadImage(MultipartBody.Part body) {

        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SplashActivity.HOST)
                    .client(Utils.client())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Services restService = service.create(Services.class);
            return restService.uploadImage(body);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }


}
