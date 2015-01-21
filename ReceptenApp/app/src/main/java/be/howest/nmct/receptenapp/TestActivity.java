package be.howest.nmct.receptenapp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new getDifficulties().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getDifficulties extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<Recept> recepts = Recept.getAllRecipes();
            //CreateIngredientsDatabase();
            //CreateFruitTaart(Boolean.TRUE);
            getReceptById(2);
            return null;
        }
    }

    private void getReceptById(int id){
        Recept recept = Recept.getRecipeById(id);
        Log.e("TEST",recept.getName());

    }
}
