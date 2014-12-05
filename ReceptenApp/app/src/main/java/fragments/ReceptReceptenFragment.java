package fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Category;
import data.Recept;
import data.RecipeView;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptReceptenFragment extends ListFragment {
    final Context context = getActivity();
    ReceptenAdapter receptenAdapter;
    private TextView txvTitle;

    //global date here:
    Category category;
    ArrayList<Recept> arrRecipes;
    RecipeView data;

    //KEYS
    public static final String SELECTED_CATEGORIE = "";
    public static final String ARR_RECIPES = "";
    public static final String RECIPE_VIEW = "";

    public ReceptReceptenFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        data = bundle.getParcelable(RECIPE_VIEW);
        category = data.getCategorie();
        arrRecipes = data.getArrRecipes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recepten, container, false);
        ((TextView) view.findViewById(R.id.Title)).setText("");
        txvTitle = (TextView) view.findViewById(R.id.Title);
        return view;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        txvTitle.setText("Recepten (" + category.getName() + "))");
        //ShowReceptenTask task = new ShowReceptenTask();
        //task.execute();
        receptenAdapter = new ReceptenAdapter();
        setListAdapter(receptenAdapter);
    }




    //TEMP
    private ArrayList<ArrayList<Recept>> getRecepten(){
        ArrayList<ArrayList<Recept>> allRecepis = new ArrayList<ArrayList<Recept>>();

        ArrayList<Recept> vlees = new ArrayList<Recept>();
        ArrayList<Recept> vis = new ArrayList<Recept>();
        ArrayList<Recept> dessert = new ArrayList<Recept>();

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

        //Vis
        Recept rec3 = new Recept();
        rec3.setName("Zalmrolletjes");
        rec3.setPicture("" + R.drawable.rec_zalmrolletjes);

        Recept rec4 = new Recept();
        rec4.setName("Gebakken scampi's");
        rec4.setPicture("" + R.drawable.rec_scampi);

        vis.add(rec3);
        vis.add(rec4);

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

        allRecepis.add(dessert);
        allRecepis.add(vlees);
        allRecepis.add(vis);


        return allRecepis;
    }

    //1. Asynctask
    private ProgressDialog pDialog;
    class ShowReceptenTask extends AsyncTask<String, Void, ArrayList<Recept>> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Showing Recepten...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Recept> doInBackground(String... params)
        {

            return arrRecipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recept> result){
            if(pDialog.isShowing()){ pDialog.dismiss();}
            super.onPostExecute(result);
            receptenAdapter = new ReceptenAdapter();
            setListAdapter(receptenAdapter);

            //Toast.makeText(getActivity(), "Recepten ready.", Toast.LENGTH_SHORT).show();
        }
    }

    class ReceptenAdapter extends ArrayAdapter<Recept>
    {
        public ReceptenAdapter()
        {
            super(getActivity(), R.layout.row_recept, R.id.recept_naam, arrRecipes);
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            Recept rec = arrRecipes.get(position);

            TextView naam = (TextView) row.findViewById(R.id.recept_naam);
            naam.setText(rec.getName());

            ImageView image = (ImageView) row.findViewById(R.id.receptImage);
            int img = Integer.parseInt(rec.getPicture());
            image.setImageResource(img);

            return row;
        }

    }

    //CLICK ON LIST
    OnReceptenSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnReceptenSelectedListener {
        public void OnReceptenSelectedListener(Recept recept);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnReceptenSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReceptenSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.OnReceptenSelectedListener(arrRecipes.get(position));
    }


}
