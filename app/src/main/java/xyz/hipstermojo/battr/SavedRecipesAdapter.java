package xyz.hipstermojo.battr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.recipe.Recipe;

import static xyz.hipstermojo.battr.Utils.formatDuration;

public class SavedRecipesAdapter extends RecyclerView.Adapter<SavedRecipesAdapter.SavedRecipesViewHolder> {
    private List<Recipe> recipes = new ArrayList<>();
    private Context context;

    public SavedRecipesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SavedRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_recipe_card, parent, false);
        return new SavedRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipesViewHolder holder, int position) {
        Recipe savedRecipe = recipes.get(position);
        holder.recipeTitle.setText(savedRecipe.getTitle());
        Picasso.get().load(savedRecipe.getImage()).fit().into(holder.recipeImage);
        holder.recipeServings.setText(String.format("Serves %d", savedRecipe.getServings()));
        holder.recipeDuration.setText(formatDuration(savedRecipe.getDuration()));
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public class SavedRecipesViewHolder extends RecyclerView.ViewHolder {

        public ImageView recipeImage;
        public TextView recipeTitle;
        public TextView recipeDuration;
        public TextView recipeServings;

        public SavedRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.getRootView().findViewById(R.id.saved_recipes_image);
            recipeTitle = itemView.getRootView().findViewById(R.id.saved_recipes_title);
            recipeDuration = itemView.getRootView().findViewById(R.id.saved_recipes_duration);
            recipeServings = itemView.getRootView().findViewById(R.id.saved_recipes_servings);
        }
    }
}
