package cu.entumovil.snb.core.managers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.PlayerDetails;
import cu.entumovil.snb.core.models.Qualification;
import cu.entumovil.snb.service.ITeamService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamManager {

    private static final String TAG = SNBApp.APP_TAG + TeamManager.class.getSimpleName();

    public TeamManager() { }

    public Call<ArrayList<Qualification>> positions() {
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ITeamService restService = service.create(ITeamService.class);
            return restService.positions();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Call<ArrayList<PlayerDetails>> getPlayerById(long id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", Long.toString(id));
        try {
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(SNBApp.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ITeamService restService = service.create(ITeamService.class);
            return restService.playerDetails(params);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}
