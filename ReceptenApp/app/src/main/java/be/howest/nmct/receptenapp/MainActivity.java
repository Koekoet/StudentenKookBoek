package be.howest.nmct.receptenapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import data.Category;
import data.Ingredient;
import data.Recept;
import data.RecipeView;
import fragments.FavoriteFragment;
import fragments.ReceptBereidingFragment;
import fragments.ReceptCategoriesFragment;
import fragments.ReceptDetailFragment;
import fragments.ReceptInfoFragment;
import fragments.ReceptIngredientenFragment;
import fragments.ReceptNavigationFragment;
import fragments.ReceptReceptenFragment;

public class MainActivity extends FragmentActivity
        implements ReceptInfoFragment.onReceptInfoSelectedListener,
        ReceptIngredientenFragment.onReceptIngredientSelectedListener,
        ReceptBereidingFragment.onReceptBereidingSelectedListener,
        ReceptNavigationFragment.OnNavigationSelectedListener,
        ReceptCategoriesFragment.OnCategorieSelectedListener,
        ReceptReceptenFragment.OnReceptenSelectedListener{

    private String[] arrNavigation;

    //NAVIGATION
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navigationView;

    //globale vars here:
    private ArrayList<Category> arrCategories;
    private ArrayList<ArrayList<Recept>> arrRecipes;

    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();
    public static ArrayList<Category> ARRCATEGORIES = new ArrayList<Category>();
    //tijdelijk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation
        arrNavigation = getResources().getStringArray(R.array.MenuBasic);
        navigationView = (View) findViewById(R.id.navigation);
        mTitle = mDrawerTitle = getTitle();
        navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        navigationDrawer.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        /*if(navigationDrawer.isDrawerOpen(navigationView)){
            navigationDrawer.closeDrawer(navigationView);}*/


        if (savedInstanceState == null) {
            LoadCategoriesTask task = new LoadCategoriesTask();
            task.execute();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    //LOADING DATA ON START
    class LoadCategoriesTask extends AsyncTask<String, Void, ArrayList<Category>> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected ArrayList<Category> doInBackground(String... params) {

            ArrayList<Category> categories = data.Category.getAllCategories();
            /*for (Category cat : categories){
                cat.setPicture("" + R.drawable.cat_vleesgerechten);
            }*/
            return categories;
        }
        @Override
        protected void onPostExecute(ArrayList<Category> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            arrCategories = result;
            arrRecipes = new ArrayList<ArrayList<Recept>>();
            for (int i = 0; i < arrCategories.size(); i++) {
                arrRecipes.add(new ArrayList<Recept>());
            }
            setCategories();
            Toast.makeText(MainActivity.this, "Categories ready.", Toast.LENGTH_SHORT).show();
        }
    }
    private void setCategories(){
        ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(catFrag.ARR_CATEGORIE, arrCategories);
        catFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.mainfragment, catFrag).commit();
    }

        //ON MENU OR ITEM SELECTED
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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

                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();
                return true;
            case R.id.action_TestFavorite:
                FavoriteFragment fragment1 = new FavoriteFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, fragment1).addToBackStack(null).commit();
                return true;
            case R.id.action_TestDiff:
                Intent intent3 = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menu_item_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //onNavigationSelect
    @Override
    public void onNavigationSelected(int position, boolean isLogin) {
        //case 0-2 (blijft hetzelfde)
        if(navigationDrawer.isDrawerOpen(navigationView)){
            navigationDrawer.closeDrawer(navigationView);}

        clearBackstack();
        switch (position){
            case 0: //Categories
                ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
                Bundle args = new Bundle();
                args.putParcelableArrayList(catFrag.ARR_CATEGORIE, arrCategories );
                catFrag.setArguments(args);


                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, catFrag).commit();
                break;

            case 1: //Boodschappenlijstje
                Toast.makeText(MainActivity.this, "Boodschappenlijstje", Toast.LENGTH_SHORT).show();
                break;

            case 2: //Favorieten
                FavoriteFragment fragment1 = new FavoriteFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, fragment1).addToBackStack(null).commit();
                break;
        }

        //indien normal user
        //indien ingelogd
        if(isLogin){
            switch (position){
                case 3:
                    Toast.makeText(MainActivity.this, "Recept toevoegen", Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Toast.makeText(MainActivity.this, "Vrienden", Toast.LENGTH_SHORT).show();
                    break;

                case 5:
                    Toast.makeText(MainActivity.this, "Profiel", Toast.LENGTH_SHORT).show();
                    break;

                case 6:
                    //USER UITLOGGEN
                    FragmentManager fm = getSupportFragmentManager();

                    //if you added fragment via layout xml
                    ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                    fragment.ShowNavigation();
                    break;
            }
        }else {
            if (position == 3) {
                FragmentManager fm = getSupportFragmentManager();
                ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                fragment.ShowNavigation();
            }
        }

    }
    public void clearBackstack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    //ON CATEGORIE SELECTED
    @Override
    public void OnCategorieSelectedListener(Category category) {
        //LOAD RECIPES
        LoadRecipes(category);


    }
    private void LoadRecipes(Category category){
        checkRecipes(category);

        ReceptReceptenFragment recFrag = new ReceptReceptenFragment();
        Bundle args = new Bundle();
        args.putParcelable(recFrag.RECIPE_VIEW, prepareRecipeView(category));

        recFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, recFrag).addToBackStack(null).commit();
    }
    //TEMP
    private void checkRecipes(Category category){

        if(arrRecipes.isEmpty()){
            Toast.makeText(MainActivity.this, "arr recipes empty", Toast.LENGTH_SHORT);
        }


        if(arrRecipes == null || arrRecipes.get(category.getID()).isEmpty()){
            Toast.makeText(MainActivity.this, "Load recipe", Toast.LENGTH_LONG);
            //get recepis of selected categorie
            if(category.getID() == 0){
                ArrayList<Recept> dessert = new ArrayList<Recept>();
                //desserts
                Recept rec5 = new Recept();
                rec5.setName("Chocoladetaart");
                rec5.setPicture("" + R.drawable.rec_chocoladetaart);

                Recept rec6 = new Recept();
                rec6.setName("Banana split");
                rec6.setPicture("" + R.drawable.rec_bananasplit);

                Recept rec7 = new Recept();
                rec7.setName("Pudding!");
                rec7.setPicture("" + R.drawable.rec_pudding);

                Recept rec8 = new Recept();
                rec8.setName("Fruittaart");
                rec8.setPicture("" + R.drawable.rec_fruittaart);

                dessert.add(rec5);
                dessert.add(rec6);
                dessert.add(rec7);
                dessert.add(rec8);

                arrRecipes.set(category.getID(), dessert);
            } else if (category.getID() == 1){
                ArrayList<Recept> vlees = new ArrayList<Recept>();

                //VLEES
                Recept rec0 = new Recept();
                rec0.setName("Hamburger op oma's wijze");
                rec0.setPicture("" + R.drawable.rec_hamburger);

                Recept rec1 = new Recept();
                rec1.setName("Steak met frietjes");
                rec1.setPicture("" + R.drawable.rec_steakmetfrieten);

                Recept rec2 = new Recept();
                rec2.setName("Kip met rijst");
                rec2.setPicture("" + R.drawable.rec_kipmetrijst);

                vlees.add(rec0);
                vlees.add(rec1);
                vlees.add(rec2);

                arrRecipes.set(category.getID(), vlees);
            } else if(category.getID()== 2){
                ArrayList<Recept> vis = new ArrayList<Recept>();
                Recept rec3 = new Recept();
                rec3.setName("Zalmrolletjes");
                rec3.setPicture("" + R.drawable.rec_zalmrolletjes);

                Recept rec4 = new Recept();
                rec4.setName("Gebakken scampi's");
                rec4.setPicture("" + R.drawable.rec_scampi);

                vis.add(rec3);
                vis.add(rec4);

                arrRecipes.set(category.getID(), vis);
            }

        }
    }
    private RecipeView prepareRecipeView(Category category){
        RecipeView data = new RecipeView();
        data.setCategory(category);
        data.setArrRecipes(arrRecipes.get(category.getID()));
        return data;
    }

    @Override
    public void OnReceptenSelectedListener(Recept recept) {
        /*Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
        intent.putExtra("selectedRecipe", recept);
        startActivity(intent);*/
        ReceptDetailFragment fragment = new ReceptDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MYSELECTEDRECIPE", recept);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onReceptBereidingSelected(String tekst) {
        /*Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmen);*/
    }

    @Override
    public void onReceptInfoSelected(String tekst) {
        /*Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);*/
    }

    @Override
    public void onReceptIngredientSelectedListener(String tekst) {
        /*Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);*/
    }

    //EXITING APPLICATION
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            super.onBackPressed();
            return;
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }


}
