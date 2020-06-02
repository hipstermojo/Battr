package xyz.hipstermojo.battr.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository recipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        recipeRepository = new RecipeRepository(application);
    }

    public void insert(Recipe recipe, String userId) {
        recipeRepository.insert(recipe, userId);
    }

    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public LiveData<List<Recipe>> getAllRecipes(String userId) {
        return recipeRepository.getSavedRecipes(userId);
    }

    public LiveData<List<Recipe>> fetchRecipes() {
        return recipeRepository.fetchRecipes();
    }

    public MutableLiveData<Recipe> fetchRecipe(int recipeId) {
        return recipeRepository.fetchRecipeById(recipeId);
    }
}
