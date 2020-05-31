package xyz.hipstermojo.battr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.hipstermojo.battr.recipe.Recipe;

public interface RecipeService {
    @GET("recipes/search")
    Call<ListRecipesResponse> listRecipes(@Query("apiKey") String apiKey);

    @GET("recipes/{id}/information")
    Call<Recipe> fetchRecipeInfo(@Path("id") int id, @Query("apiKey") String apiKey);
}
