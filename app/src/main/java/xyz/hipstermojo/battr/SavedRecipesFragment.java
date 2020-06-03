package xyz.hipstermojo.battr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.recipe.Recipe;
import xyz.hipstermojo.battr.recipe.RecipeViewModel;

import static xyz.hipstermojo.battr.MainActivity.RECIPE;

public class SavedRecipesFragment extends Fragment implements SavedRecipesAdapter.OnItemClickListener {
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecipeViewModel recipeViewModel;
    private SavedRecipesAdapter adapter;
    private List<Recipe> savedRecipes = new ArrayList<>();
    private FirebaseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ((MainActivity) getActivity()).getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);
        adapter = new SavedRecipesAdapter(getContext());
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.saved_recipes_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes(currentUser.getEmail()).observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Toast.makeText(getContext(), String.format("Loaded %d recipes", recipes.size()), Toast.LENGTH_SHORT).show();
                savedRecipes.addAll(recipes);
                adapter.setRecipes(savedRecipes);
                adapter.setOnItemClickListener(SavedRecipesFragment.this::onItemClick);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position, ImageView imageView, TextView textView) {
        Intent recipeDetailIntent = new Intent(getActivity(), RecipeDetailActivity.class);
        Recipe clickedRecipe = savedRecipes.get(position);
        recipeDetailIntent.putExtra(Utils.FRAGMENT_TAG, SavedRecipesFragment.class.getSimpleName());
        recipeDetailIntent.putExtra(RECIPE, clickedRecipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), Pair.create(imageView, ViewCompat.getTransitionName(imageView)),
                Pair.create(textView, ViewCompat.getTransitionName(textView))
        );
        startActivity(recipeDetailIntent, optionsCompat.toBundle());
    }
}
