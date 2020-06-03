package xyz.hipstermojo.battr;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;


public class MainActivity extends AppCompatActivity {

    public static final String RECIPE = "Clicked Recipe";

    private DuoDrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    TextView textView = findViewById(R.id.menu_header_email);
                    textView.setText(currentUser.getEmail());
                }
            }
        };

        Fade fadeTransition = new Fade();
        View decor = getWindow().getDecorView();
        fadeTransition.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fadeTransition.excludeTarget(android.R.id.statusBarBackground, true);
        fadeTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fadeTransition);
        getWindow().setExitTransition(fadeTransition);

        drawerLayout = findViewById(R.id.navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, new MainFragment()).commit();
    }

    public void navigate(View view) {
        FragmentManager manager = getSupportFragmentManager();
        if (view.getId() == R.id.menu_header_saved_recipes_label && manager.getBackStackEntryCount() == 0) { // this means the user clicked "Your cookbook" after opening the app
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.container, new SavedRecipesFragment()).commit();
        } else if (manager.getBackStackEntryCount() == 1 && view.getId() == R.id.menu_header_home_label) { // If the back stack is empty then the user had not left the main fragment
            manager.popBackStack();
        }
        drawerLayout.closeDrawer();
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
