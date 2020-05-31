package xyz.hipstermojo.battr.ingredient;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import xyz.hipstermojo.battr.recipe.RecipeDatabase;

public class IngredientRepository {
    private IngredientDao ingredientDao;

    public IngredientRepository(Application application) {
        ingredientDao = RecipeDatabase.getInstance(application).ingredientDao();
    }

    public void insertAll(List<Ingredient> ingredients) {
        new InsertAllIngredientsAsyncTask(this.ingredientDao).execute(ingredients);
    }

    public void deleteRecipeIngredients(int recipeId) {
        new DeleteRecipeIngredientsAsyncTask(this.ingredientDao).execute(recipeId);
    }

    private static class InsertAllIngredientsAsyncTask extends AsyncTask<List<Ingredient>, Void, Void> {
        private IngredientDao ingredientDao;

        private InsertAllIngredientsAsyncTask(IngredientDao ingredientDao) {
            this.ingredientDao = ingredientDao;
        }

        @Override
        protected Void doInBackground(List<Ingredient>... lists) {
            ingredientDao.insertAll(lists[0]);
            return null;
        }
    }

    private static class DeleteRecipeIngredientsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private IngredientDao ingredientDao;

        private DeleteRecipeIngredientsAsyncTask(IngredientDao ingredientDao) {
            this.ingredientDao = ingredientDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            ingredientDao.deleteRecipeIngredients(ints[0]);
            return null;
        }
    }
}
