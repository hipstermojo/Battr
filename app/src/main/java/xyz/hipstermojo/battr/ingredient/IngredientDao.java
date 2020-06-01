package xyz.hipstermojo.battr.ingredient;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insertAll(List<Ingredient> ingredients);

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    void deleteRecipeIngredients(int recipeId);
}
