package cu.entumovil.snb.service;

import java.util.ArrayList;
import java.util.Map;

import cu.entumovil.snb.core.models.History;
import cu.entumovil.snb.core.models.News;
import cu.entumovil.snb.core.models.Record;
import cu.entumovil.snb.core.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IMainService {

    @GET("noticias")
    Call<ArrayList<News>> getNews();

    @GET("noticia")
    Call<ArrayList<News>> detailsOfNews(@QueryMap(encoded=true) Map<String, String> options);

    @GET("efemerides")
    Call<ArrayList<History>> aDayOnHistory();

    @GET("efemeride")
    Call<ArrayList<History>> aDayOnHistoryById(@QueryMap(encoded = true) Map<String, String> options);

    @GET("recordsserie")
    Call<ArrayList<Record>> serieRecords(@QueryMap(encoded = true) Map<String, String> options);

    @GET("recordsjuego")
    Call<ArrayList<Record>> gameRecords(@QueryMap(encoded = true) Map<String, String> options);

    @GET("recordsespeciales")
    Call<ArrayList<Record>> specializedRecords(@QueryMap(encoded = true) Map<String, String> options);

    @POST("user/check")
    Call<User> checkRegister(@Body User user);

}
