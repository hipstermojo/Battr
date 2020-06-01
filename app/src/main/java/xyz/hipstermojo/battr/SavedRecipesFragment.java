package xyz.hipstermojo.battr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.hipstermojo.battr.recipe.Recipe;
import xyz.hipstermojo.battr.recipe.RecipeViewModel;

public class SavedRecipesFragment extends Fragment {
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecipeViewModel recipeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);
        SavedRecipesAdapter adapter = new SavedRecipesAdapter(getContext());
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.saved_recipes_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                Toast.makeText(getContext(), String.format("Loaded %d recipes", recipes.size()), Toast.LENGTH_SHORT).show();
                adapter.setRecipes(recipes);
            }
        });
        return view;
    }
}
