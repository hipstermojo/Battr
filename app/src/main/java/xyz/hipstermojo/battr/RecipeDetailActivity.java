package xyz.hipstermojo.battr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe recipe;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipe = getIntent().getParcelableExtra(MainActivity.RECIPE);
        insertRecipeInfo();

        ImageView recipeImage = findViewById(R.id.recipe_view_image);
        TextView recipeTitle = findViewById(R.id.recipe_view_name);
        TextView recipeUrl = findViewById(R.id.recipe_view_url);

        Picasso.get()
                .load(recipe.getImage()).fit()
                .centerCrop()
                .into(recipeImage);

        recipeTitle.setText(recipe.getTitle());
        recipeUrl.setText(recipe.getSourceUrl());
    }


    private void insertRecipeInfo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RecipeService recipeService = retrofit.create(RecipeService.class);
        recipeService.fetchRecipeInfo(recipe.getId(), BuildConfig.SpoonacularAPIKey).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", String.format("Call to get recipe information was unsuccessful. Status: %d", response.code()));
                } else {
                    if (response.body() != null) {
                        recipe = response.body();
                        TextView recipeSource = findViewById(R.id.recipe_view_source);
                        TextView recipeServings = findViewById(R.id.recipe_view_servings);
                        TextView recipeDuration = findViewById(R.id.recipe_view_duration);

                        LinearLayout recipesIngredientsView = findViewById(R.id.recipe_view_ingredients_view);
                        LinearLayout recipesInstructionsView = findViewById(R.id.recipe_view_instructions_view);

                        recipeSource.setText(String.format("by %s", recipe.getSource()));
                        recipeServings.setText(String.format("Serves %d", recipe.getServings()));
                        recipeDuration.setText(String.format("%d min", recipe.getDuration()));

                        for (Recipe.Ingredient ingredient : recipe.getIngredients()){
                            TextView ingredientTextView = new TextView(getApplicationContext());
                            ingredientTextView.setText(ingredient.name);
                            recipesIngredientsView.addView(ingredientTextView);
                        }

                        for (Recipe.Step step : recipe.getInstructions().get(0).steps){
                            TextView stepTextView = new TextView(getApplicationContext());
                            stepTextView.setText(step.step);
                            recipesInstructionsView.addView(stepTextView);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e("RETROFIT", String.format("Call to get recipe information failed \n%s", t.getMessage()));
            }
        });
    }

    public void saveRecipe(View view) {
        recipeViewModel.insert(recipe);
    }
}
