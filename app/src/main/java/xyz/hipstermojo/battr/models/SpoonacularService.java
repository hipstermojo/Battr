package xyz.hipstermojo.battr.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface SpoonacularService {
    @GET("recipes/search")
    Call<ListRecipesResponse> listRecipes(@Query("apiKey") String apiKey);
}

