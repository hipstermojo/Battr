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
        holder.textView.setText(savedRecipe.getTitle());
        Picasso.get().load(savedRecipe.getImage()).fit().into(holder.imageView);
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

        public ImageView imageView;
        public TextView textView;

        public SavedRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.getRootView().findViewById(R.id.saved_recipes_image);
            textView = itemView.getRootView().findViewById(R.id.saved_recipes_title);
        }
    }
}
