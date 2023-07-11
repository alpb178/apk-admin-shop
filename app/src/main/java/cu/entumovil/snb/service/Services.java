package cu.entumovil.snb.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cu.entumovil.snb.core.models.Data;
import cu.entumovil.snb.core.models.ResetPassword;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPost;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPut;
import cu.entumovil.snb.core.models.Register;
import cu.entumovil.snb.core.models.login;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Services {

    @POST("jewls")
    Call<JsonObject> createJewel(@Body Data params);

    @PUT("jewls/{id}")
    Call<JsonObject> updateJewel(@Path("id") String id, @Body Data.DataUpdateJewelSales params);



    @DELETE("jewls/{id}")
    Call<JsonObject> deleteJewels(@Path("id") String id);

    @GET("jewlCatalogue/findByCode/{model}")
    Call<JsonArray> findJewelCatalogueByModel(@Path("model") String model);

    @GET("user/findByUserName/{username}")
    Call<JsonArray> findUserByUserName(@Path("username") String username);

    @GET("jwel/findByVendedor/{id}")
    Call<JsonArray> findJewelByVendor(@Path("id") String id);

    @POST("auth/local")
    Call<JsonObject> login(@Body login params);

    @POST("auth/local/register")
    Call<JsonObject> register(@Body Register params);

    @GET("users/{id}?populate=*")
    Call<JsonObject> getUsers(@Path("id") String id);

    @GET("jwel/findByUser/{id}")
    Call<JsonArray> findByJewelByUser(@Path("id") String id);

    @GET("user/vendedor/findByUserName/{username}")
    Call<JsonArray> findVendorByUserName(@Path("username") String username);

    @POST("jewl-catalogues")
    Call<JsonObject> createJewelCatalog(@Body JewelCatalogPost params);

    @PUT("jewl-catalogues/{id}")
    Call<JsonObject> updateJewelCatalog(@Path("id") String id,@Body JewelCatalogPut params);

    @PUT("jewl-catalogues/{id}")
    Call<JsonObject> updateJewelCatalogue(@Path("id") String id, @Body Data.DataUpdate params);

    @GET("user/updateUserBlocked/{id}/{blocked}")
    Call<JsonArray> updateUserBlocked(@Path("id") String id, @Path("blocked") Boolean blocked);

    @GET("user/update/password/username/{username}/{password}")
    Call<JsonArray> updatePasswordByUsername(@Path("username") String username, @Path("password") String password);

    @GET("user/update/password/id/{id}/{password}")
    Call<JsonArray> updatePasswordById(@Path("id") String id, @Path("blocked") String password);

    @Multipart
    @POST("upload")
    Call<JsonArray> uploadImage(
            @Part MultipartBody.Part file);

    @GET("user/delete/{id}/{phone}")
    Call<JsonObject> deleteVendor(@Path("id") String id,@Path("phone") String phone);

    @PUT("users/{id}")
    Call<JsonObject> updateUser(@Path("id") String id,@Body User params);

    @PUT("users/{id}")
    Call<JsonObject> resetPass(@Path("id") String id, @Body ResetPassword user);

    @GET("user/findIdByUserName/{username}")
    Call<Integer> findIdByUserName(@Path("username") String username);


}
