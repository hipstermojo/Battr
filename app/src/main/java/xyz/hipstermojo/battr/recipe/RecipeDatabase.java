package xyz.hipstermojo.battr.recipe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import xyz.hipstermojo.battr.Converters;
import xyz.hipstermojo.battr.ingredient.Ingredient;
import xyz.hipstermojo.battr.ingredient.IngredientDao;
import xyz.hipstermojo.battr.instruction.Instruction;
import xyz.hipstermojo.battr.instruction.InstructionDao;

@Database(entities = {Recipe.class, Ingredient.class, Instruction.Step.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase instance;

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), RecipeDatabase.class, "recipe_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract InstructionDao instructionDao();
}
