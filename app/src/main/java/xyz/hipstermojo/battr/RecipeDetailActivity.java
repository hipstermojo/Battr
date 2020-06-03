package xyz.hipstermojo.battr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import xyz.hipstermojo.battr.ingredient.IngredientAdapter;
import xyz.hipstermojo.battr.instruction.InstructionAdapter;
import xyz.hipstermojo.battr.recipe.Recipe;
import xyz.hipstermojo.battr.recipe.RecipeViewModel;

import static xyz.hipstermojo.battr.Utils.formatDuration;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe recipe;
    private RecipeViewModel recipeViewModel;

    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;

    private RecyclerView instructionsRecyclerView;
    private InstructionAdapter instructionAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Fade fadeTransition = new Fade();
        View decor = getWindow().getDecorView();
        fadeTransition.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fadeTransition.excludeTarget(android.R.id.statusBarBackground, true);
        fadeTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fadeTransition);
        getWindow().setExitTransition(fadeTransition);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        Intent intent = getIntent();
        String sender = intent.getStringExtra(Utils.FRAGMENT_TAG);
        recipe = intent.getParcelableExtra(MainActivity.RECIPE);
        recipeViewModel.fetchRecipe(recipe.getId()).observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipeWithInfo) {
                recipe = recipeWithInfo;
                if (Objects.equals(sender, MainFragment.class.getSimpleName())) {
                    FloatingActionButton floatingActionButton = findViewById(R.id.save_btn);
                    floatingActionButton.animate().scaleX(1.0f).scaleY(1.0f);
                }

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
        });

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

    public void saveRecipe(View view) {
        recipe.setCreatedAt(new Date());
        recipeViewModel.insert(recipe, firebaseUser.getEmail());
        Intent intent = new Intent();
        intent.putExtra("RESULT_MSG", "saved");
        setResult(RESULT_OK);
        finish();
    }

    public void openLink(View view) {
        Uri link = Uri.parse(recipe.getSourceUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, link);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }
}
