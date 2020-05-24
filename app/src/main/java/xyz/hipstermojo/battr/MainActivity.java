package xyz.hipstermojo.battr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hipstermojo.battr.models.ListRecipesResponse;
import xyz.hipstermojo.battr.models.Recipe;
import xyz.hipstermojo.battr.models.SpoonacularService;


public class MainActivity extends AppCompatActivity {

    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loader = findViewById(R.id.loadingBar);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SpoonacularService service = retrofit.create(SpoonacularService.class);
        service.listRecipes(BuildConfig.SpoonacularAPIKey).enqueue(new Callback<ListRecipesResponse>() {
            @Override
            public void onResponse(Call<ListRecipesResponse> call, Response<ListRecipesResponse> response) {
                loader.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    Log.d("RETROFIT", "Response unsuccessful. Status: " + response.code());
                } else {
                    List<Recipe> recipes = response.body().results;
                    Toast.makeText(getApplicationContext(), String.format("Got %d recipes", recipes.size()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListRecipesResponse> call, Throwable t) {
                Log.d("RETROFIT", "Response NOT successful " + t.getMessage());
            }
        });
    }


}
