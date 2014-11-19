package be.howest.nmct.receptenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import data.Ingredient;
import data.Recept;
import fragments.ReceptCategoriesFragment;
import fragments.ReceptNavigationFragment;

public class MainActivity extends FragmentActivity implements ReceptNavigationFragment.OnNavigationSelectedListener {
    private String[] arrNavigation;
    private DrawerLayout navigationDrawer;
    private View navigationView;
    //private ListView mNavigationList;

    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();

    //tijdelijk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrNavigation = getResources().getStringArray(R.array.MenuBasic);
        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (View) findViewById(R.id.navigation);
        }


    protected void OnStart(){
        super.onStart();
        onNavigationSelected(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_TestRecepi) {
            Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
            intent.putExtra("selectedRecipe", new Recept());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNavigationSelected(int position) {
        //case 0-2 (blijft hetzelfde)
        Toast.makeText(MainActivity.this, "Selected: " + position, Toast.LENGTH_SHORT).show();
        if(position == 0){
            //Categorien

            ReceptCategoriesFragment receptFragment = new ReceptCategoriesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainfragment,receptFragment).commit();

        }
        //indien normal user

        //indien ingelogd


        //uiteindelijk
        navigationDrawer.closeDrawer(navigationView);
    }


}
