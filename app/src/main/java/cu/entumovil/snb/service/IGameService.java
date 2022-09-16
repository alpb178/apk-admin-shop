package cu.entumovil.snb.service;

import java.util.ArrayList;
import java.util.Map;

import cu.entumovil.snb.core.models.Boxscore;
import cu.entumovil.snb.core.models.Game;
import cu.entumovil.snb.core.models.Linescore;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IGameService {

    @GET("juegosdia")
    Call<ArrayList<Game>> gamesOnDay(@QueryMap Map<String, String> params);

    @GET("linescore")
    Call<ArrayList<Linescore>> linescoreByGameId(@QueryMap Map<String, String> params);

    @GET("boxscore")
    Call<Boxscore> boxscoreByGameId(@QueryMap Map<String, String> params);

}
