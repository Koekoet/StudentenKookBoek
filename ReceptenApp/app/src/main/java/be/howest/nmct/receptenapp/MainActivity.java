package be.howest.nmct.receptenapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import data.RecipesByCategory;
import fragments.ReceptBereidingFragment;
import fragments.ReceptBoodschappenlijstjeFragment;
import fragments.ReceptCategoriesFragment;
import fragments.ReceptDetailFragment;
import fragments.ReceptFavoriteFragment;
import fragments.ReceptInfoFragment;
import fragments.ReceptIngredientenFragment;
import fragments.ReceptNavigationFragment;
import fragments.ReceptReceptenFragment;

public class MainActivity extends FragmentActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        //Listeners
        ReceptInfoFragment.onReceptInfoSelectedListener,
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
    private ArrayList<Recept> arrFavoriteRecipes;

    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();
    public static ArrayList<Category> ARRCATEGORIES = new ArrayList<Category>();

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
            //Test cursor loader
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
                ReceptFavoriteFragment fragment1 = new ReceptFavoriteFragment();
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
                ReceptBoodschappenlijstjeFragment frBoodschap = new ReceptBoodschappenlijstjeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, frBoodschap).addToBackStack(null).commit();
                break;

            case 2: //Favorieten
                checkFavoritesData();
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

    //FAVORITES
    private void checkFavoritesData() {
        if(arrFavoriteRecipes == null){
            //set favorites
            LoadFavoritesTask task = new LoadFavoritesTask();
            task.execute();

        }else {
            ShowFavorites();
        }

    }
    private void ShowFavorites(){
        ReceptFavoriteFragment favoriteFragment = new ReceptFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(favoriteFragment.ARR_FAVORITE_RECIPES, arrFavoriteRecipes);
        favoriteFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, favoriteFragment).addToBackStack(null).commit();
    }
    class LoadFavoritesTask extends AsyncTask<String, Void, ArrayList<Recept>> {
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
        protected ArrayList<Recept> doInBackground(String... params) {

            ArrayList<Recept> recepts = new ArrayList<Recept>();
            recepts.add(Recept.getRecipeById(10));
            recepts.add(Recept.getRecipeById(15));
            recepts.add(Recept.getRecipeById(12));
            arrFavoriteRecipes = recepts;
            return recepts;
        }
        @Override
        protected void onPostExecute(ArrayList<Recept> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            arrFavoriteRecipes = result;
            ShowFavorites();
        }
    }

    //ON CATEGORIE SELECTED
    @Override
    public void OnCategorieSelectedListener(Category category) {
        //LOAD RECIPES
        showReceptsOfCategory(category);


    }
    private void showReceptsOfCategory(Category category){
        if(arrRecipes == null || arrRecipes.get(category.getID()).isEmpty()){
            Toast.makeText(MainActivity.this, "Load recipe", Toast.LENGTH_LONG);
            //get recepis of selected categorie
            LoadReceptsOfCategoryTask task = new LoadReceptsOfCategoryTask(category);
            task.execute();
        } else {
            startReceptFragment(category);
        }


    }
    private void startReceptFragment(Category category){
        ReceptReceptenFragment recFrag = new ReceptReceptenFragment();
        Bundle args = new Bundle();
        args.putParcelable(recFrag.RECIPE_VIEW, prepareRecipeView(category));

        recFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, recFrag).addToBackStack(null).commit();
    }
    private RecipeView prepareRecipeView(Category category){
        RecipeView data = new RecipeView();
        data.setCategory(category);
        data.setArrRecipes(arrRecipes.get(category.getID()));
        return data;
    }
    class LoadReceptsOfCategoryTask extends AsyncTask<String, Void, ArrayList<Recept>> {
        private ProgressDialog pDialog;
        private Category category;

        public LoadReceptsOfCategoryTask(Category category){
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected ArrayList<Recept> doInBackground(String... params) {
            RecipesByCategory receptsByCat = RecipesByCategory.getRecipeByCatId(this.category.getID());
            return receptsByCat.Recipes;
        }
        @Override
        protected void onPostExecute(ArrayList<Recept> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            arrRecipes.set(category.getID(),result);
            startReceptFragment(category);
        }
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
            Toast.makeText(this, "Press back again to leave", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Uri baseUri;
            if(id == 1){
                //Uri.withAppendedPath()

            }
            /*
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = ContactsContract.Contacts.CONTENT_URI;
            }

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
            return new CursorLoader(getActivity(), baseUri,
                    CONTACTS_SUMMARY_PROJECTION, select, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
            */
            return null;
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)

            //mAdapter.swapCursor(data);
        }

        public void onLoaderReset(Loader<Cursor> loader) {
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.

            //mAdapter.swapCursor(null);
        }


}
