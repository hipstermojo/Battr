package xyz.hipstermojo.battr.recipe;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hipstermojo.battr.BuildConfig;
import xyz.hipstermojo.battr.ListRecipesResponse;
import xyz.hipstermojo.battr.RecipeService;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> allRecipes;
    private RecipeService service;
    private MutableLiveData<Recipe> recipe;

    public RecipeRepository(Application application) {
        RecipeDatabase database = RecipeDatabase.getInstance(application);
        recipeDao = database.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
        recipe = new MutableLiveData<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(RecipeService.class);
    }

    public void insert(Recipe recipe) {
        new InsertRecipeAsyncTask(recipeDao).execute(recipe);
    }

    public void delete(Recipe recipe) {
        new DeleteRecipeAsyncTask(recipeDao).execute(recipe);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public MutableLiveData<List<Recipe>> fetchRecipes() {
        Log.d("LIVEDATA", "Fetching all recipes");
        MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
        service.listRecipes(BuildConfig.SpoonacularAPIKey).enqueue(new Callback<ListRecipesResponse>() {
            @Override
            public void onResponse(Call<ListRecipesResponse> call, Response<ListRecipesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", "Response unsuccessful. Status: " + response.code());
                } else {
                    if (response.body() != null) {
                        recipes.setValue(response.body().results);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListRecipesResponse> call, Throwable t) {
                Log.d("RETROFIT", "HTTP call failed\n" + t.getMessage());
            }
        });
        return recipes;
    }

    public MutableLiveData<Recipe> fetchRecipeById(int recipeId) {
        service.fetchRecipeInfo(recipeId, BuildConfig.SpoonacularAPIKey).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", String.format("Call to get recipe information was unsuccessful. Status: %d", response.code()));
                } else {
                    if (response.body() != null) {
                        recipe.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e("RETROFIT", String.format("Call to get recipe information failed \n%s", t.getMessage()));
            }
        });

        return recipe;
    }

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;

        private InsertRecipeAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            this.recipeDao.insert(recipes[0]);
            return null;
        }
    }

    private static class DeleteRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;

        private DeleteRecipeAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            this.recipeDao.delete(recipes[0]);
            return null;
        }
    }
}
