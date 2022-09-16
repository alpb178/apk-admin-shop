package cu.entumovil.snb.service;

import java.util.ArrayList;
import java.util.Map;

import cu.entumovil.snb.core.models.Fielder;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.models.LifeTime;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.models.SerieLeader;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface IStatsService {

    @GET("bateo")
    Call<ArrayList<Hitter>> getHittersStats(@QueryMap(encoded = true) Map<String, String> options);

    @GET("pitcheo")
    Call<ArrayList<Pitcher>> getPitchersStats(@QueryMap(encoded = true) Map<String, String> options);

    @GET("fildeo")
    Call<ArrayList<Fielder>> getFieldersStats(@QueryMap(encoded = true) Map<String, String> options);

    @GET("lideresbateo")
    Call<ArrayList<SerieLeader>> getSerieLeaderHitters();

    @GET("liderespitcheo")
    Call<ArrayList<SerieLeader>> getSerieLeaderPitchers();

    @GET("bateoacumesp")
    Call<ArrayList<Hitter>> getHittersKnowment(@QueryMap(encoded = true) Map<String, String> options);

    @GET("pitcheoacumesp")
    Call<ArrayList<Pitcher>> getPitchersKnowment(@QueryMap(encoded = true) Map<String, String> options);

    @GET("bateoacum")
    Call<ArrayList<LifeTime>> getHittersLifetime(@QueryMap(encoded = true) Map<String, String> options);

    @GET("pitcheoacum")
    Call<ArrayList<LifeTime>> getPitchersLifetime(@QueryMap(encoded = true) Map<String, String> options);

}
