package be.howest.nmct.receptenapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import data.Ingredient;
import data.Recept;
import fragments.ReceptBereidingFragment;
import fragments.ReceptDetailFragment;
import fragments.ReceptInfoFragment;
import fragments.ReceptIngredientenFragment;

public class MainActivity extends FragmentActivity
        implements ReceptInfoFragment.onReceptInfoSelectedListener,
        ReceptIngredientenFragment.onReceptIngredientSelectedListener,
        ReceptBereidingFragment.onReceptBereidingSelectedListener{
    private String[] arrNavigation;
    private DrawerLayout mDrawerLayout;
    private ListView mNavigationList;

    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        arrNavigation = getResources().getStringArray(R.array.MenuBasic);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mNavigationList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.navigation_list_item, R.id.menuItem ,arrNavigation));
        // Set the list's click listener
        //mNavigationList.setOnItemClickListener(new DrawerItemClickListener(){});
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

        switch (id){
            case R.id.action_TestRecepi:
                /*Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
                intent.putExtra("selectedRecipe", new Recept());
                startActivity(intent);*/
                ReceptDetailFragment fragment = new ReceptDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("MYSELECTEDRECIPE", new Recept());
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,fragment).addToBackStack(null).commit();
                return true;
            case R.id.action_TestFavorite:
                Intent intent2 = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onReceptBereidingSelected(String tekst) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onReceptInfoSelected(String tekst) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onReceptIngredientSelectedListener(String tekst) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
    }
}
