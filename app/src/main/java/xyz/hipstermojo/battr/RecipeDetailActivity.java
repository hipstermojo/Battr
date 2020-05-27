package xyz.hipstermojo.battr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecipeDetailActivity extends AppCompatActivity {
    private ImageView recipeImage;
    private TextView recipeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeImage = findViewById(R.id.recipe_view_image);
        recipeTitle = findViewById(R.id.recipe_view_name);

        Recipe recipe = getIntent().getParcelableExtra(MainActivity.RECIPE);
        Picasso.get()
                .load(recipe.getImage()).fit()
                .centerCrop()
                .into(recipeImage);

        recipeTitle.setText(recipe.getTitle());
    }
}
