package cu.entumovil.snb.core.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.Boxscore;
import cu.entumovil.snb.core.models.Game;
import cu.entumovil.snb.core.models.Linescore;
import cu.entumovil.snb.service.IGameService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameManager {

    private static final String TAG = SNBApp.APP_TAG + GameManager.class.getSimpleName();

    public GameManager() { }

    public Call<ArrayList<Game>> gamesOnDay(String gameDay) {
        Map<String, String> params = new HashMap<>();
        params.put("fecha", gameDay);
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd/mm/yyyy")
                    .create();
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IGameService restService = service.create(IGameService.class);

            return restService.gamesOnDay(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

      public Call<ArrayList<Linescore>> linescore(int gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("idjuego", String.valueOf(gameId));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IGameService restService = service.create(IGameService.class);
            return restService.linescoreByGameId(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<Boxscore> boxscore(int gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("idjuego", String.valueOf(gameId));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IGameService restService = service.create(IGameService.class);
            return restService.boxscoreByGameId(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}
