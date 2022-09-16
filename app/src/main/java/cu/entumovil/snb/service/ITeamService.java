package cu.entumovil.snb.service;

import java.util.ArrayList;
import java.util.Map;

import cu.entumovil.snb.core.models.PlayerDetails;
import cu.entumovil.snb.core.models.Qualification;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ITeamService {

    @GET("estado")
    Call<ArrayList<Qualification>> positions();

    @GET("datosatleta")
    Call<ArrayList<PlayerDetails>> playerDetails(@QueryMap(encoded=true) Map<String, String> options);

}
