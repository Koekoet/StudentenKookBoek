package be.howest.nmct.receptenapp;


import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.Author;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.IngredientData.Ingredient;
import be.howest.nmct.receptenapp.data.Login.AbstractGetUserTask;
import be.howest.nmct.receptenapp.data.Login.GetUserInForeground;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.fragments.ReceptBereidingFragment;
import be.howest.nmct.receptenapp.fragments.ReceptBoodschappenlijstjeFragment;
import be.howest.nmct.receptenapp.fragments.ReceptCategoriesFragment;
import be.howest.nmct.receptenapp.fragments.ReceptCreateBereidingFragment;
import be.howest.nmct.receptenapp.fragments.ReceptCreateCategoryFragment;
import be.howest.nmct.receptenapp.fragments.ReceptCreateInfoFragment;
import be.howest.nmct.receptenapp.fragments.ReceptCreateIngredientFragment;
import be.howest.nmct.receptenapp.fragments.ReceptDetailFragment;
import be.howest.nmct.receptenapp.fragments.ReceptFavoriteFragment;
import be.howest.nmct.receptenapp.fragments.ReceptInfoFragment;
import be.howest.nmct.receptenapp.fragments.ReceptIngredientenFragment;
import be.howest.nmct.receptenapp.fragments.ReceptNavigationFragment;
import be.howest.nmct.receptenapp.fragments.ReceptReceptenFragment;

public class MainActivity extends FragmentActivity
        implements
        //Listeners
        ReceptInfoFragment.onReceptInfoSelectedListener,
        ReceptIngredientenFragment.onReceptIngredientSelectedListener,
        ReceptBereidingFragment.onReceptBereidingSelectedListener,
        ReceptNavigationFragment.OnNavigationSelectedListener,
        ReceptCategoriesFragment.OnCategorieSelectedListener,
        ReceptReceptenFragment.OnReceptenSelectedListener,
        ReceptCreateInfoFragment.OnNextCreateInfoSelectedListener,
        ReceptCreateIngredientFragment.OnNextCreateIngredientSelectedListener,
        ReceptCreateBereidingFragment.OnNextCreateBereidingSelectedListener,
        ReceptCreateCategoryFragment.OnNextCreateCategorySelectedListener{

    private String[] arrNavigation;

    //NAVIGATION
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navigationView;

    //globale vars here:
    public static ArrayList<Category> arrCategories;
    public static ArrayList<ArrayList<Recept>> arrRecipes;
    public static ArrayList<Recept> arrFavoriteRecipes;
    public static ArrayList<Ingredient> arrIngredients;


    //Globale vars
    //  Boodschappenlijstje
    public static ArrayList<Ingredient> BOODSCHAPPENLIJSTJE = new ArrayList<Ingredient>();
    public static ArrayList<Category> ARRCATEGORIES = new ArrayList<Category>();

    private Recept recCreateRecipe;

    //  Login
    private String mEmail;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    public static final String EXTRA_ACCOUNTNAME = "extra_accountname";
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    public static Author LOGGEDINUSER;
    //tijdelijk

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrRecipes = new ArrayList<ArrayList<Recept>>();

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

            setCategories();
           // LoadCategoriesTask task = new LoadCategoriesTask();
            //task.execute();
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
    private void setCategories(){
        ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
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
            /*case R.id.action_TestRecepi:
                *//*Intent intent = new Intent(MainActivity.this, ReceptDetailActivity.class);
                intent.putExtra("selectedRecipe", new Recept());
                startActivity(intent);*//*
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
                return true;*/
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
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, catFrag).commit();
                break;

            case 1: //Boodschappenlijstje
                ReceptBoodschappenlijstjeFragment frBoodschap = new ReceptBoodschappenlijstjeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, frBoodschap).addToBackStack(null).commit();
                break;

            case 2: //Favorieten
                ReceptFavoriteFragment favoriteFragment = new ReceptFavoriteFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, favoriteFragment).addToBackStack(null).commit();
                break;
        }

        //indien normal user
        //indien ingelogd
        if(isLogin){
            switch (position){
                case 3:
                    ReceptCreateInfoFragment receptCreateInfoFragment = new ReceptCreateInfoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateInfoFragment).addToBackStack(null).commit();
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
                    LOGGEDINUSER = null;
                    //if you added fragment via layout xml
                    ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                    fragment.ShowNavigation();
                    break;
            }
        }else {
            if (position == 3) {
                //Inloggen

                getUser();


            }
        }

    }

    private void getUser() {
        if(mEmail == null){
            pickUserAccount();
        } else {
            if(isDeviceOnline()){
                getTask(MainActivity.this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(MainActivity.this, "Geen netwerkverbinding...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private AbstractGetUserTask getTask(MainActivity activity, String email,String scope){
        return new GetUserInForeground(activity, email, scope);
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_PICK_ACCOUNT){
            if(resultCode == Activity.RESULT_OK){
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                getUser();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "You must pick an account", Toast.LENGTH_SHORT).show();
            }
        } else if((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR) && resultCode == Activity.RESULT_OK){
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            //Log.i(TAG, "Retrying");
            getTask(MainActivity.this, mEmail, SCOPE).execute();
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
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

    public void user(final JSONObject profile, final String email) {
        runOnUiThread(new Runnable() {
            @Override
            public void run(){
                try{
                    LOGGEDINUSER = new Author();
                    LOGGEDINUSER.setAuthorID(profile.getString("id"));
                    LOGGEDINUSER.setFirstname(profile.getString("given_name"));
                    LOGGEDINUSER.setLastname(profile.getString("family_name"));
                    LOGGEDINUSER.setEmail(email);

                    FragmentManager fm = getSupportFragmentManager();
                    ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                    fragment.ShowNavigation();
                }catch (JSONException e){
                    Log.d("JSONException:", e.getMessage());
                }

            }
        });
    }

    public void showError(final String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            MainActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
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
    public void OnCategorieSelectedListener(int categoryID) {
        //LOAD RECIPES
        showReceptsOfCategory(categoryID);


    }
    private void LoadRecipes(Category category) {
        //checkRecipes(category);
    }
    private void showReceptsOfCategory(int categoryID){
        //krijgen category binnen ==> mss simpeler met een ID alleen in toekomst.
        ReceptReceptenFragment recFrag = new ReceptReceptenFragment();
        Bundle bundle = new Bundle();
        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/RecByCatId/" + categoryID);
        bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
        recFrag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, recFrag).addToBackStack(null).commit();
    }

    @Override
    public void OnReceptenSelectedListener(int id) {

        ReceptDetailFragment fragment = new ReceptDetailFragment();
        Bundle bundle = new Bundle();
        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/" + id);
        bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
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




    @Override
    public void onNextCreateInfoSelectedListener(Recept recept) {
        this.recCreateRecipe = recept;
        ReceptCreateCategoryFragment receptCreateCategoryFragment = new ReceptCreateCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("CREATERECIPEVALUES", recCreateRecipe);
        receptCreateCategoryFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateCategoryFragment).addToBackStack(null).commit();
    }

    @Override
    public void onNextCreateIngredientSelectedListener(Recept recept, String button) {
        this.recCreateRecipe = recept;
        if(button.equals("next")){
            //dan recept = goed, next
            ReceptCreateBereidingFragment receptCreateBereidingFragment = new ReceptCreateBereidingFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateBereidingFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateBereidingFragment).addToBackStack(null).commit();
        } else {
            //vorige pagina
            ReceptCreateCategoryFragment receptCreateCategoryFragment = new ReceptCreateCategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateCategoryFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateCategoryFragment).addToBackStack(null).commit();
        }

    }


    @Override
    public void onNextCreateBereidingSelectedListener(final Recept recept, String button) {
        this.recCreateRecipe = recept;
        if(button.equals("next")){
            this.recCreateRecipe = recept;
            if(button.equals("next")){
                //Dialog maken
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Recept toevoegen");
                builder.setMessage("Bent u zeker dat u dit recept wilt toevoegen?");
                builder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Recept wegschrijven
                                String ingredients = "";
                                for (Ingredient ing : recCreateRecipe.getIngredients()){
                                    ingredients += ing.getID() + ";";
                                }
                                ingredients = ingredients.substring(0, ingredients.length()-1);
                                String cost = recCreateRecipe.getCost();
                                cost = cost.replaceAll(">","").replaceAll("<","").replaceAll("€","").replaceAll(" ", "");

                                recCreateRecipe.setAuthorID(1);

                                new CreateRecipe(recCreateRecipe, ingredients, cost).execute();

                                //Recept detail tonen
                                ReceptDetailFragment receptDetailFragment = new ReceptDetailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("MYSELECTEDRECIPE", recCreateRecipe);
                                receptDetailFragment.setArguments(bundle);
                                clearBackstack();
                                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptDetailFragment).addToBackStack(null).commit();

                            }
                        })
                        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();


            } else {
                ReceptCreateBereidingFragment receptCreateBereidingFragment = new ReceptCreateBereidingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
                receptCreateBereidingFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateBereidingFragment).addToBackStack(null).commit();
            }
        } else {
            ReceptCreateIngredientFragment receptCreateIngredientFragment = new ReceptCreateIngredientFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateIngredientFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateIngredientFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onNextCreateCategorySelectedListener(Recept recept, String button) {
        this.recCreateRecipe = recept;
        if(button.equals("next")){
            ReceptCreateIngredientFragment receptCreateIngredientFragment = new ReceptCreateIngredientFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateIngredientFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateIngredientFragment).addToBackStack(null).commit();
        } else {
            ReceptCreateInfoFragment receptCreateInfoFragment = new ReceptCreateInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateInfoFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateInfoFragment).addToBackStack(null).commit();
        }
    }

    private class CreateRecipe extends AsyncTask<Void,Void,Void>{
        Recept recipe;
        String ingredients;
        String cost;

        public CreateRecipe(Recept recept, String ingredients, String cost){
            this.recipe = recept;
            this.ingredients = ingredients;
            this.cost = cost;
        }

        @Override
        protected Void doInBackground(Void... params) {
            //int receptID = recept.create....
            Recept.createRecipe(recipe.getName(), recipe.getAuthorID(), recipe.getDuration(),
                    cost, recipe.getNumberOfPersons(), recipe.getDifficultyID(), recipe.getPicture(),
                    ingredients, recipe.getRecipeText());

            /*for(Category cat : recipe.getCategories()){
                //voor elke categorie dat gebruiker selecteert
                //moet het receptID toegevoegd worden aan de lijst


            }*/

            return null;
        }
    }
}
