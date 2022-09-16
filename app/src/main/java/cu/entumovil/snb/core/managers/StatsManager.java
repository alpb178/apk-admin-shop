package cu.entumovil.snb.core.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.Fielder;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.models.LifeTime;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.models.SerieLeader;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.service.IStatsService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatsManager {

    private static final String TAG = SNBApp.APP_TAG + StatsManager.class.getSimpleName();

    public StatsManager() { }

    public Call<ArrayList<Hitter>> loadHittersStats(int page, StatsType statsType, int teamId) {
        Call<ArrayList<Hitter>> l = null;
        Map<String, String> params = new HashMap<>();
        params.put("pag", Integer.toString(page + 1));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IStatsService restService = service.create(IStatsService.class);
            switch (statsType) {
                case SINGLE:
                    l = restService.getHittersStats(params);
                    break;
                case SPECIALIZED:
                    l = restService.getHittersKnowment(params);
                    break;
            }
            return l;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<Fielder>> loadFieldersStats(int page, StatsType statsType, int teamId) {
        Map<String, String> params = new HashMap<>();
        params.put("pag", Integer.toString(page + 1));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IStatsService restService = service.create(IStatsService.class);
            return restService.getFieldersStats(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<Pitcher>> loadPitchersStats(int page, StatsType statsType, int teamId) {
        Call<ArrayList<Pitcher>> l = null;
        Map<String, String> params = new HashMap<>();
        params.put("pag", Integer.toString(page + 1));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IStatsService restService = service.create(IStatsService.class);
            switch (statsType) {
                case SINGLE:
                    l = restService.getPitchersStats(params);
                    break;
                case SPECIALIZED:
                    l = restService.getPitchersKnowment(params);
                    break;
            }
            return l;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<SerieLeader>> serieLeader(int type) {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IStatsService restService = service.create(IStatsService.class);
            if (type == 0) return restService.getSerieLeaderHitters();
            else return restService.getSerieLeaderPitchers();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<LifeTime>> loadLifetimeStats(int page, int type) {
        Call<ArrayList<LifeTime>> l = null;
        Map<String, String> params = new HashMap<>();
        params.put("pag", Integer.toString(page + 1));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IStatsService restService = service.create(IStatsService.class);
            if (type == 0) l = restService.getHittersLifetime(params);
            else l = restService.getPitchersLifetime(params);
            return l;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}
