package xyz.hipstermojo.battr;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;


public class MainActivity extends AppCompatActivity {

    public static final String RECIPE = "Clicked Recipe";

    private DuoDrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        if (view.getId() == R.id.menu_header_saved_recipes_label && manager.getBackStackEntryCount() == 0) { // this means the user clicked "Your cookbook" after opening the app
            transaction.add(R.id.container, new SavedRecipesFragment()).commit();
        } else if(manager.getBackStackEntryCount() == 1 && view.getId() == R.id.menu_header_app_name) { // If the back stack is empty then the user had not left the main fragment
            manager.popBackStack();
        }
        drawerLayout.closeDrawer();
    }
}
