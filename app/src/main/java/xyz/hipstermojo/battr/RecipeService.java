package xyz.hipstermojo.battr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("recipes/search")
    Call<ListRecipesResponse> listRecipes(@Query("apiKey") String apiKey);
}
