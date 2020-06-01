package xyz.hipstermojo.battr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hipstermojo.battr.ingredient.Ingredient;
import xyz.hipstermojo.battr.ingredient.IngredientAdapter;
import xyz.hipstermojo.battr.ingredient.IngredientViewModel;
import xyz.hipstermojo.battr.instruction.InstructionAdapter;
import xyz.hipstermojo.battr.recipe.Recipe;
import xyz.hipstermojo.battr.recipe.RecipeViewModel;

import static xyz.hipstermojo.battr.Utils.formatDuration;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe recipe;
    private RecipeViewModel recipeViewModel;
    private IngredientViewModel ingredientViewModel;

    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;

    private RecyclerView instructionsRecyclerView;
    private InstructionAdapter instructionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        recipe = getIntent().getParcelableExtra(MainActivity.RECIPE);
        insertRecipeInfo();

        ImageView recipeImage = findViewById(R.id.recipe_view_image);
        TextView recipeTitle = findViewById(R.id.recipe_view_name);

        ingredientsRecyclerView = findViewById(R.id.recipe_view_ingredients_view);
        ingredientAdapter = new IngredientAdapter(this);

        instructionsRecyclerView = findViewById(R.id.recipe_view_instructions_view);
        instructionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        instructionAdapter = new InstructionAdapter(this);

        Picasso.get()
                .load(recipe.getImage()).fit()
                .centerCrop()
                .into(recipeImage);

        recipeTitle.setText(recipe.getTitle());
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

                        recipeSource.setText(String.format("by %s", recipe.getSource()));
                        recipeServings.setText(String.format("Serves %d", recipe.getServings()));
                        recipeDuration.setText(formatDuration(recipe.getDuration()));

                        ingredientAdapter.setIngredients(recipe.getIngredients());
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), recipe.getIngredients().size() / 2);
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        layoutManager.setSpanCount(2);

                        ingredientsRecyclerView.setLayoutManager(layoutManager);
                        ingredientsRecyclerView.setHasFixedSize(true);
                        ingredientsRecyclerView.setAdapter(ingredientAdapter);

                        instructionAdapter.setSteps(recipe.getInstructions().get(0).steps);
                        instructionsRecyclerView.setAdapter(instructionAdapter);

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
        recipe.setCreatedAt(new Date());
        recipeViewModel.insert(recipe);
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.recipeId = recipe.getId();
        }
        ingredientViewModel.insertAll(recipe.getIngredients());
    }

    public void openLink(View view) {
        Uri link = Uri.parse(recipe.getSourceUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, link);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }
}
