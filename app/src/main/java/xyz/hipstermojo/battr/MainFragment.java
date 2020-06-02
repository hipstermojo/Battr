package xyz.hipstermojo.battr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.recipe.Recipe;
import xyz.hipstermojo.battr.recipe.RecipeViewModel;

import static android.app.Activity.RESULT_OK;
import static xyz.hipstermojo.battr.MainActivity.RECIPE;

public class MainFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
    private List<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    private CardStackView cardStackView;
    private RecipeViewModel recipeViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private boolean canSave = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        CardStackLayoutManager layoutManager;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        recipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);

        layoutManager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                if (direction == Direction.Right) {
                    canSave = true;
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {
                if (canSave) {
                    Recipe recipe = recipes.get(position);
                    recipeViewModel.insert(recipe, firebaseUser.getEmail());
                    canSave = false;
                }
            }
        });

        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);
        layoutManager.setTranslationInterval(8.0f);
        layoutManager.setScaleInterval(0.95f);
        layoutManager.setSwipeThreshold(0.3f);
        layoutManager.setMaxDegree(20.0f);
        layoutManager.setDirections(Direction.FREEDOM);
        layoutManager.setOverlayInterpolator(new LinearInterpolator());
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration * 2)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        layoutManager.setSwipeAnimationSetting(setting);

        cardStackView = view.findViewById(R.id.swipe_cards_view);
        cardStackView.setHasFixedSize(true);
        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        recipes = new ArrayList<>();

        RecipeViewModel viewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        viewModel.fetchRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> fetchRecipes) {
                recipes = fetchRecipes;
                recipeAdapter = new RecipeAdapter(getContext(), recipes);
                cardStackView.setAdapter(recipeAdapter);
                recipeAdapter.setOnItemClickListener(MainFragment.this::onItemClick);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position, ImageView imageView, TextView textView) {
        Intent recipeDetailIntent = new Intent(getActivity(), RecipeDetailActivity.class);
        Recipe clickedRecipe = recipes.get(position);
        recipeDetailIntent.putExtra(Utils.FRAGMENT_TAG, MainFragment.class.getSimpleName());
        recipeDetailIntent.putExtra(RECIPE, clickedRecipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), Pair.create(imageView, ViewCompat.getTransitionName(imageView)),
                Pair.create(textView, ViewCompat.getTransitionName(textView))
        );
        startActivityForResult(recipeDetailIntent, 123, optionsCompat.toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                cardStackView.swipe();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
