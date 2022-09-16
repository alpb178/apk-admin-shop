package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.History;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.core.models.Record;
import cu.entumovil.snb.service.IMainService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainManager {

    private static final String TAG = SNBApp.APP_TAG + MainManager.class.getSimpleName();

    public MainManager() { }

    public Call<ArrayList<News>> loadNews() {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/mm/yyyy HH:mm:ss")
                    .create();
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            IMainService restService = service.create(IMainService.class);
            return restService.getNews();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<News>> newsById(long id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", Long.toString(id));

        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IMainService restService = service.create(IMainService.class);
            return restService.detailsOfNews(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<History>> loadHistories() {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IMainService restService = service.create(IMainService.class);
            return restService.aDayOnHistory();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<History>> loadHistoryById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IMainService restService = service.create(IMainService.class);
            return restService.aDayOnHistoryById(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<Record>> records(int page, int type) {
        Call<ArrayList<Record>> l = null;
        Map<String, String> params = new HashMap<>();
        params.put("pag", Integer.toString(page + 1));

        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IMainService restService = service.create(IMainService.class);
            switch (type) {
                case 0:
                    l = restService.serieRecords(params);
                    break;
                case 1:
                    l = restService.gameRecords(params);
                    break;
                case 2:
                    l = restService.specializedRecords(params);
                    break;
            }
            return l;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}
