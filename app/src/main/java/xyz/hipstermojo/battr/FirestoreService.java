package xyz.hipstermojo.battr;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.recipe.Recipe;

public class FirestoreService {
    public static final String RECIPES_COLLECTION = "recipes";
    public static final String SAVED_RECIPE_COLLECTION = "saved_recipes";
    private static FirebaseFirestore dbInstance;

    public static FirebaseFirestore getInstance() {
        if (dbInstance == null) {
            dbInstance = FirebaseFirestore.getInstance();
        }
        return dbInstance;
    }

    public static void getAllRecipes(MutableLiveData<List<Recipe>> recipes) {
        CollectionReference recipeCollectionRef = getInstance().collection(RECIPES_COLLECTION);
        recipeCollectionRef.orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Recipe> recipeList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        recipeList.add(Recipe.fromHashMap(document.getData()));
                    }
                    recipes.setValue(recipeList);
                } else {
                    Log.d("Firestore", "Retrieving all recipes failed");
                }
            }
        });
    }

    public static void getUserRecipes(MutableLiveData<List<Recipe>> recipeMutableLiveData, String userId) {
        CollectionReference savedRecipeCollectionRef = getInstance()
                .collection(SAVED_RECIPE_COLLECTION).document(userId)
                .collection(RECIPES_COLLECTION);
        savedRecipeCollectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Recipe> recipeList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        recipeList.add(Recipe.fromHashMap(document.getData()));
                    }
                    recipeMutableLiveData.setValue(recipeList);
                } else {
                    Log.d("Firestore", "Retrieving user recipes failed");
                }
            }
        });
    }

    public static void findOneRecipe(MutableLiveData<Recipe> recipeMutableLiveData, String recipeId) {
        CollectionReference savedRecipeCollectionRef = getInstance().collection(RECIPES_COLLECTION);
        savedRecipeCollectionRef.document(recipeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    recipeMutableLiveData.setValue(Recipe.fromHashMap(document.getData()));
                }
            }
        });
    }
}
