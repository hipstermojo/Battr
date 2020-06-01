package xyz.hipstermojo.battr.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository recipeRepository;
    private LiveData<List<Recipe>> allRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        recipeRepository = new RecipeRepository(application);
        allRecipes = recipeRepository.getAllRecipes();
    }

    public void insert(Recipe recipe) {
        recipeRepository.insert(recipe);
    }

    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }
}
