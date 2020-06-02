package xyz.hipstermojo.battr.recipe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import xyz.hipstermojo.battr.FirestoreService;
import xyz.hipstermojo.battr.R;

import static androidx.core.content.res.TypedArrayUtils.getString;
import static xyz.hipstermojo.battr.FirestoreService.RECIPES_COLLECTION;
import static xyz.hipstermojo.battr.FirestoreService.SAVED_RECIPE_COLLECTION;

public class RecipeRepository {
    private MutableLiveData<List<Recipe>> allRecipes = new MutableLiveData<>();
    private MutableLiveData<Recipe> recipe;
    private MutableLiveData<List<Recipe>> savedRecipes;
    private FirebaseFirestore firestoreDB;

    public RecipeRepository(Application application) {
        firestoreDB = FirestoreService.getInstance();
        SharedPreferences sharedPref = application.getSharedPreferences("Battr",Context.MODE_PRIVATE);
        FirestoreService.getAllRecipes(allRecipes,sharedPref.getInt("curId",-1));
        recipe = new MutableLiveData<>();
    }

    public void insert(Recipe recipe, String userId) {
        firestoreDB.collection(SAVED_RECIPE_COLLECTION)
                .document(userId).collection(RECIPES_COLLECTION)
                .document(Integer.toString(recipe.getId())).set(recipe).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Error adding recipe ", e);
            }
        });
    }

    // TODO: Do in Firebase
//    public void delete(Recipe recipe) {
//        new DeleteRecipeAsyncTask(recipeDao).execute(recipe);
//    }

    public LiveData<List<Recipe>> getSavedRecipes(String userId) {
        savedRecipes = new MutableLiveData<>();
        FirestoreService.getUserRecipes(savedRecipes, userId);
        return savedRecipes;
    }

    public LiveData<List<Recipe>> fetchRecipes() {
        return allRecipes;
    }

    public MutableLiveData<Recipe> fetchRecipeById(int recipeId) {
        FirestoreService.findOneRecipe(recipe, Integer.toString(recipeId));
        return recipe;
    }
}
