package be.howest.nmct.receptenapp;


import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.AuthorData.Author;
import be.howest.nmct.receptenapp.data.AuthorData.AuthorDatabaseHelper;
import be.howest.nmct.receptenapp.data.AuthorData.AuthorTable;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryDatabaseHelper;
import be.howest.nmct.receptenapp.data.IngredientData.Ingredient;
import be.howest.nmct.receptenapp.data.IngredientData.IngredientTable;
import be.howest.nmct.receptenapp.data.Login.AbstractGetUserTask;
import be.howest.nmct.receptenapp.data.Login.GetUserInForeground;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategory;
import be.howest.nmct.receptenapp.data.UnitData.Unit;
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
import be.howest.nmct.receptenapp.fragments.ReceptProfileFragment;
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
    private Context context;
    private Boolean isSmall;

    //NAVIGATION
    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private View navigationView;

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

        //Author.loadAllAuthors(MainActivity.this);

        /*Cursor test = MainActivity.this.getContentResolver().query(ReceptenAppContentProvider.CONTENT_URI_AUTHOR,null,null,null,null);
        int testsize = test.getCount();

        test.moveToFirst();
        String id1 = test.getString(test.getColumnIndex(AuthorTable.COLUMN_ID));
        test.moveToNext();
        String id2 = test.getString(test.getColumnIndex(AuthorTable.COLUMN_ID));
        test.moveToNext();*/


       /* //Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_AUTHOR);
        Cursor test = MainActivity.this.getContentResolver().query(ReceptenAppContentProvider.CONTENT_URI_AUTHOR,null,null,null,null);
        int testsize = test.getCount();

        AuthorDatabaseHelper arthData = new AuthorDatabaseHelper(MainActivity.this);
        SQLiteDatabase auDb = arthData.getReadableDatabase();

        Cursor testcount = auDb.rawQuery("select count(*) from author", null);
        testcount.moveToFirst();
        int counsdqgt= testcount.getInt(0);

        test.moveToFirst();
        String id1 = test.getString(test.getColumnIndex(AuthorTable.COLUMN_ID));
        test.moveToNext();
        String id2 = test.getString(test.getColumnIndex(AuthorTable.COLUMN_ID));
        test.moveToNext();
        String id3 = test.getString(test.getColumnIndex(AuthorTable.COLUMN_ID));

*/
        isSmall = IsSmallDevice();

        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        //navigation
        arrNavigation = getResources().getStringArray(R.array.MenuBasic);
        navigationView = (View) findViewById(R.id.navigation);

        if(isSmall){
            mTitle = mDrawerTitle = getTitle();
            navigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerToggle = new ActionBarDrawerToggle(this, navigationDrawer,
                    R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    //getActionBar().setTitle(titleClosed);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    //getActionBar().setTitle(R.string.app_name);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            navigationDrawer.setDrawerListener(mDrawerToggle);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            //check sqlite database
            CategoryDatabaseHelper catdatabase = new CategoryDatabaseHelper(MainActivity.this);
            SQLiteDatabase catDb = catdatabase.getReadableDatabase();
            Cursor mCount= catDb.rawQuery("select count(*) from category", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            mCount.close();

            if(count == 0){
                //load all data
                LoadData();

            } else {
                // continue mainactivity
                setCategories();
            }
        }



    }

    private void LoadData() {
        if(isDeviceOnline()){
            clearBackstack();
            LoadDataTask taskCat = new LoadDataTask();
            taskCat.execute();
        } else {
            Toast.makeText(MainActivity.this, "Geen netwerkverbinding", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean IsSmallDevice() {
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }


    class LoadDataTask extends AsyncTask<String, Integer, Void> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Data downloaden");
            pDialog.setProgressStyle(pDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            //The maximum number of items is 100
            pDialog.setMax(6);
            //Set the current progress to zero
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            Context context = MainActivity.this;
            Category.LoadAllCategories(context);
            publishProgress(1);
            Recept.LoadAllRecipesCURSOR(context);
            publishProgress(2);
            RecipesByCategory.getAllRecipesByCategoryCURSOR(context);
            publishProgress(3);
            Ingredient.LoadAllIngredientsCURSOR(context);
            publishProgress(4);
            Unit.LoadAllUnitsCURSOR(context);
            publishProgress(5);
            Author.loadAllAuthors(context);
            publishProgress(6);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            pDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            setCategories();

        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(isSmall){mDrawerToggle.syncState();}

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(isSmall){ mDrawerToggle.onConfigurationChanged(newConfig);}

    }

    //LOADING DATA ON START
    private void setCategories(){
        ReceptCategoriesFragment catFrag = new ReceptCategoriesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, catFrag).commit();
    }

        //ON MENU OR ITEM SELECTED


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isSmall){
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }

        int id = item.getItemId();

        switch (id){
            case R.id.menu_item_refresh:
                LoadData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //onNavigationSelect
    @Override
    public void onNavigationSelected(int position, boolean isLogin) {
        //case 0-2 (blijft hetzelfde)
        if(isSmall){
            if(navigationDrawer.isDrawerOpen(navigationView)){
                navigationDrawer.closeDrawer(navigationView);}
        }


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
                    //Gebruikt voorlopig de Globale variabele LOGGEDINUSER
                    ReceptProfileFragment receptProfileFragment = new ReceptProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptProfileFragment).addToBackStack(null).commit();
                    break;

                case 5:
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
                Toast.makeText(MainActivity.this, "Selecteer account", Toast.LENGTH_SHORT).show();
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

    public void user(final JSONObject profile, final String email) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = getSupportFragmentManager();
                ReceptNavigationFragment fragment = (ReceptNavigationFragment) fm.findFragmentById(R.id.fragment_navigation);
                fragment.ShowNavigation();
            }
        });
        try {
            LOGGEDINUSER = new Author();
            LOGGEDINUSER.setAuthorID(profile.getString("id"));
            LOGGEDINUSER.setFirstname(profile.getString("given_name"));
            LOGGEDINUSER.setLastname(profile.getString("family_name"));
            LOGGEDINUSER.setEmail(email);
            LOGGEDINUSER.setImage(profile.getString("picture"));

            Author.saveAuthor(LOGGEDINUSER);
            Author.loadAllAuthors(MainActivity.this);

        } catch (JSONException e) {
            Log.d("JSONException:", e.getMessage());
        }


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

    //ON CATEGORIE SELECTED
    @Override
    public void OnCategorieSelectedListener(int categoryID) {
        //LOAD RECIPES
        showReceptsOfCategory(categoryID);


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
            Toast.makeText(this, "Druk nogmaals om te sluiten", Toast.LENGTH_SHORT).show();

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
            if(recCreateRecipe.getIngredients() != null && recCreateRecipe.getIngredients().size() != 0){
                //dan recept = goed, next
                ReceptCreateBereidingFragment receptCreateBereidingFragment = new ReceptCreateBereidingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
                receptCreateBereidingFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateBereidingFragment).addToBackStack(null).commit();
            } else { Toast.makeText(MainActivity.this, "Geen ingredienten toegevoegd.", Toast.LENGTH_SHORT).show();}
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


                                String catIDs = "";
                                for(Integer cat : recCreateRecipe.getCategoryIDs()){
                                    catIDs += cat + ";";
                                }
                                catIDs = catIDs.substring(0, catIDs.length() -1);
                                String cost = recCreateRecipe.getCost();
                                cost = cost.replaceAll(">","").replaceAll("<","").replaceAll("€","").replaceAll(" ", "");

                                recCreateRecipe.setAuthorID(LOGGEDINUSER.getAuthorID());



                                //RECEPT TOEVOEGEN DATABASE ONLINE
                                new CreateRecipe(recCreateRecipe, cost, catIDs).execute();


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
            if(recCreateRecipe.getCategoryIDs() != null && recCreateRecipe.getCategoryIDs().size() !=0){
                ReceptCreateIngredientFragment receptCreateIngredientFragment = new ReceptCreateIngredientFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
                receptCreateIngredientFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateIngredientFragment).addToBackStack(null).commit();

            } else {
                Toast.makeText(MainActivity.this, "Selecteer minstens 1 categorie.", Toast.LENGTH_SHORT).show();
            }

            } else {
            ReceptCreateInfoFragment receptCreateInfoFragment = new ReceptCreateInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CREATERECIPEVALUES", this.recCreateRecipe);
            receptCreateInfoFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment, receptCreateInfoFragment).addToBackStack(null).commit();
        }
    }

    private class CreateRecipe extends AsyncTask<Void,Void, Integer>{
        Recept recipe;
        String ingredients;
        String cost;
        String catIDs;

        public CreateRecipe(Recept recept, String cost, String catIDs){
            this.recipe = recept;
            this.cost = cost;
            this.catIDs = catIDs;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            String ingredients = Ingredient.createIngredients(recipe.getIngredients());

            //CREATE RECIPE ONLINE
            int id = Recept.createRecipe(recipe.getName(), recipe.getAuthorID(), recipe.getDuration(),
                    cost, recipe.getNumberOfPersons(), recipe.getDifficultyID() +1, recipe.getPicture(),
                    ingredients, recipe.getRecipeText(), catIDs);
            //CREATE INGREDIENTS ONLINE

            //RECEPT TOEVOEGEN SQLITE
            ContentValues values = new ContentValues();
            values.put(ReceptTable.COLUMN_ID, id);
            values.put(ReceptTable.COLUMN_NAME, recCreateRecipe.getName());
            values.put(ReceptTable.COLUMN_AUTHORID, recCreateRecipe.getAuthorID());
            values.put(ReceptTable.COLUMN_DURATION, recCreateRecipe.getDuration());
            values.put(ReceptTable.COLUMN_COST, cost);
            values.put(ReceptTable.COLUMN_NUMBEROFPERSONS, recCreateRecipe.getNumberOfPersons());
            values.put(ReceptTable.COLUMN_DIFFICULTYID, recCreateRecipe.getDifficultyID());
            values.put(ReceptTable.COLUMN_PICTURE, recCreateRecipe.getPicture());
            values.put(ReceptTable.COLUMN_INGREDIENTS, ingredients);
            values.put(ReceptTable.COLUMN_RECIPETEXT, recCreateRecipe.getRecipeText());
            MainActivity.this.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_REC, values);

            //UPDATE REC BY CAT TABLE
            RecipesByCategory.getAllRecipesByCategoryCURSOR(MainActivity.this);
            Ingredient.LoadAllIngredientsCURSOR(MainActivity.this);
            RecipesByCategory.getAllRecipesByCategoryCURSOR(MainActivity.this);

            return id;
        }

        @Override
        protected void onPostExecute(Integer id) {
            super.onPostExecute(id);

            ReceptDetailFragment fragment = new ReceptDetailFragment();
            Bundle bundle = new Bundle();
            Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/" + id);
            bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
            fragment.setArguments(bundle);

            clearBackstack();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();



        }
    }
}
