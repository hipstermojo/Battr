package xyz.hipstermojo.battr.ingredient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class IngredientViewModel extends AndroidViewModel {
    private IngredientRepository ingredientRepository;

    public IngredientViewModel(@NonNull Application application) {
        super(application);
        ingredientRepository = new IngredientRepository(application);
    }

    public void insertAll(List<Ingredient> ingredients) {
        ingredientRepository.insertAll(ingredients);
    }

    public void deleteRecipeIngredients(int recipeId) {
        ingredientRepository.deleteRecipeIngredients(recipeId);
    }
}
