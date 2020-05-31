package xyz.hipstermojo.battr.recipe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import xyz.hipstermojo.battr.Converters;
import xyz.hipstermojo.battr.Ingredient;

@Database(entities = {Recipe.class, Ingredient.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), RecipeDatabase.class, "recipe_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
