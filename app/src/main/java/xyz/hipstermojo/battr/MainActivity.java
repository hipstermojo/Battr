package xyz.hipstermojo.battr;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hipstermojo.battr.recipe.Recipe;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {

    public static final String RECIPE = "Clicked Recipe";

    private CardStackLayoutManager layoutManager;
    private CardStackView cardStackView;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fade fadeTransition = new Fade();
        View decor = getWindow().getDecorView();
        fadeTransition.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fadeTransition.excludeTarget(android.R.id.statusBarBackground,true);
        fadeTransition.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fadeTransition);
        getWindow().setExitTransition(fadeTransition);

        layoutManager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

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

            }
        });

        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);
        layoutManager.setTranslationInterval(8.0f);
        layoutManager.setScaleInterval(0.95f);
        layoutManager.setSwipeThreshold(0.3f);
        layoutManager.setMaxDegree(20.0f);
        layoutManager.setDirections(Direction.FREEDOM);
        layoutManager.setSwipeableMethod(SwipeableMethod.Manual);
        layoutManager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView = findViewById(R.id.swipe_cards_view);
        cardStackView.setHasFixedSize(true);
        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        recipes = new ArrayList<>();

        fetchRecipes();
        DuoDrawerLayout drawerLayout = findViewById(R.id.navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private ArrayList<Recipe> recipes;

    private void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RecipeService service = retrofit.create(RecipeService.class);
        service.listRecipes(BuildConfig.SpoonacularAPIKey).enqueue(new Callback<ListRecipesResponse>() {
            @Override
            public void onResponse(Call<ListRecipesResponse> call, Response<ListRecipesResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", "Response unsuccessful. Status: " + response.code());
                } else {
                    if (response.body() != null) {
                        recipes = response.body().results;
                        recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
                        cardStackView.setAdapter(recipeAdapter);
                        recipeAdapter.setOnItemClickListener(MainActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListRecipesResponse> call, Throwable t) {
                Log.d("RETROFIT", "HTTP call failed\n" + t.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(int position, ImageView imageView, TextView textView) {

        Intent recipeDetailIntent = new Intent(MainActivity.this, RecipeDetailActivity.class);
        Recipe clickedRecipe = recipes.get(position);
        recipeDetailIntent.putExtra(RECIPE, clickedRecipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this, Pair.create(imageView, ViewCompat.getTransitionName(imageView)),
                Pair.create(textView,ViewCompat.getTransitionName(textView))
        );
        startActivity(recipeDetailIntent,optionsCompat.toBundle());
    }

    public void navigate(View view) {
        Intent intent = new Intent(MainActivity.this, SavedRecipesActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
