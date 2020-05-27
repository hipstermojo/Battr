package xyz.hipstermojo.battr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {

    public static final String RECIPE = "Clicked Recipe";

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipes = new ArrayList<>();

        fetchRecipes();

    }

    private ArrayList<Recipe> recipes;

    private void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RecipeService service = retrofit.create(RecipeService.class);
        service.listRecipes(BuildConfig.SpoonacularAPIKey).enqueue(new Callback<ListRecipesResponse>() {
            @Override
            public void onResponse(Call<ListRecipesResponse> call, Response<ListRecipesResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("RETROFIT","Response unsuccessful. Status: " + response.code());
                } else {
                    if (response.body() != null) {
                        recipes = response.body().results;
                        recipeAdapter = new RecipeAdapter(MainActivity.this,recipes);
                        recyclerView.setAdapter(recipeAdapter);
                        recipeAdapter.setOnItemClickListener(MainActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListRecipesResponse> call, Throwable t) {
                Log.d("RETROFIT","HTTP call failed\n" + t.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent recipeDetailIntent = new Intent(MainActivity.this,RecipeDetailActivity.class);
        Recipe clickedRecipe = recipes.get(position);
        recipeDetailIntent.putExtra(RECIPE,clickedRecipe);
        startActivity(recipeDetailIntent);
    }
}
