package be.howest.nmct.receptenapp;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import data.Recept;
import data.helpers.ImageConverter;


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

    private void CreateIngredientsDatabase(){
        ArrayList<Recept> dessert = new ArrayList<Recept>();
        String ingr = "";

        Recept rec8 = new Recept();
        rec8.setName("Fruittaart");
        rec8.setAuthorID(1);
        rec8.setDifficultyID(3);
        rec8.setDuration("80");
        rec8.setNumberOfPersons(4);
        rec8.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec8.setCost("12");
        rec8.setRecipeText("1. Plaats het bladerdeeg in de oven voor 20min op 200°. 2. Leg al het fruit erop en laat afkoelen. ");
        ingr = "10;14;15;16";
        Recept.createRecipe(rec8,ingr);

        ArrayList<Recept> vlees = new ArrayList<Recept>();

        //VLEES
        Recept rec0 = new Recept();
        rec0.setName("Hamburger op oma's wijze");
        rec0.setAuthorID(1);
        rec0.setDifficultyID(2);
        rec0.setDuration("30");
        rec0.setNumberOfPersons(2);
        rec0.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec0.setCost("9");
        rec0.setRecipeText("1. Maak hamburgers van het gehakt. 2. Smijt het in de pan. 3. Leg de hamburgers tussen een broodje en plaats Samuraisaus erop. 4. Leg er nog tenslotte sla en tomaat op");
        ingr = "2;17;18;6;19";
        Recept.createRecipe(rec0, ingr);


        Recept rec1 = new Recept();
        rec1.setName("Steak met frietjes");
        rec1.setAuthorID(1);
        rec1.setDifficultyID(2);
        rec1.setDuration("35");
        rec1.setNumberOfPersons(2);
        rec1.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec1.setCost("14.25");
        rec1.setRecipeText("1. Bak de steak 2. Smijt frieten in de frietpot 3. Versier met sla en wortels.");
        ingr = "20;21;18;22";
        Recept.createRecipe(rec1,ingr);

        Recept rec2 = new Recept();
        rec2.setName("Kip met rijst");
        rec2.setAuthorID(1);
        rec2.setDifficultyID(3);
        rec2.setDuration("25");
        rec2.setNumberOfPersons(1);
        rec2.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec2.setCost("7.5");
        rec2.setRecipeText("1. Snij de kip in stukjes en begin met bakken op een zacht vuur. 2. Kook de rijst voor 8 minuten. 3.Kook currysaus en breng alles samen.");
        ingr = "23;24;25";
        Recept.createRecipe(rec2,ingr);

        ArrayList<Recept> vis = new ArrayList<Recept>();
        Recept rec3 = new Recept();
        rec3.setName("Zalmrolletjes");
        rec3.setAuthorID(1);
        rec3.setDifficultyID(2);
        rec3.setDuration("30");
        rec3.setNumberOfPersons(2);
        rec3.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec3.setCost("5.5");
        rec3.setRecipeText("1. Vul de zalm met iets wat je lekker vindt. 2. Rol de zalm. 3.Smakelijk");
        ingr = "26";
        Recept.createRecipe(rec3, ingr);

        Recept rec4 = new Recept();
        rec4.setName("Gebakken scampi's");
        rec4.setAuthorID(1);
        rec4.setDifficultyID(2);
        rec4.setDuration("22");
        rec4.setNumberOfPersons(2);
        rec4.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec4.setCost("16.5");
        rec4.setRecipeText("1. Pel de scampi's. 2. Bak de scampi's. 3. Versier met groentjes.");
        ingr = "27;18;22;6;28";
        Recept.createRecipe(rec4,ingr);


    }
    private void CreateFruitTaart(Boolean IsTijsEenBloatn){
        Recept rec8 = new Recept();
        rec8.setName("Fruittaart");
        rec8.setAuthorID(1);
        rec8.setDifficultyID(3);
        rec8.setDuration("80");
        rec8.setNumberOfPersons(4);
        rec8.setPicture(ImageConverter.BitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.cat_vleesgerechten)));
        rec8.setCost("12");
        rec8.setRecipeText("1. Plaats het bladerdeeg in de oven voor 20min op 200°. 2. Leg al het fruit erop en laat afkoelen. ");
        String ingr = "10;14;15;16";
        Recept.createRecipe(rec8,ingr);

    }
    private void getReceptById(int id){
        Recept recept = Recept.getRecipeById(id);
        Log.e("TEST",recept.getName());

    }
}
