package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Category;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptCategoriesFragment extends ListFragment {
    final Context context = getActivity();
    ArrayList<Category> cats;
    CategorieAdapter categorieAdapter;
    private TextView txvTitle;

    public ReceptCategoriesFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_categorie, container, false);
        ((TextView) view.findViewById(R.id.Title)).setText("text");
        txvTitle = (TextView) view.findViewById(R.id.Title);

        updateCategorie();
        return view;



    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        ShowCategoriesTask task = new ShowCategoriesTask();
        task.execute();

    }


    public void updateCategorie(){
        txvTitle.setText("Categorie");
        cats = new ArrayList<Category>();

        Category cat0 = new Category();
        cat0.setID(0);
        cat0.setName("VleesGerechten");
        cat0.setPicture("" + R.drawable.cat_vleesgerechten);

        Category cat1 = new Category();
        cat1.setID(1);
        cat1.setName("Visgerechten");
        cat1.setPicture("" + R.drawable.cat_visgerechten);

        Category cat2 = new Category();
        cat2.setID(2);
        cat2.setName("Desserts");
        cat2.setPicture("" + R.drawable.cat_dessert);


        cats.add(cat0);
        cats.add(cat1);
        cats.add(cat2);


    }

    //1. Asynctask
    private ProgressDialog pDialog;
    class ShowCategoriesTask extends AsyncTask<String, Void, ArrayList<Category>> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Categories...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Category> doInBackground(String... params)
        {

            return cats;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> result){
            if(pDialog.isShowing()){ pDialog.dismiss();}
            super.onPostExecute(result);
            categorieAdapter = new CategorieAdapter();
            setListAdapter(categorieAdapter);

            Toast.makeText(getActivity(), "Categories ready.", Toast.LENGTH_SHORT).show();
        }
    }

    class CategorieAdapter extends ArrayAdapter<Category>
    {
        public CategorieAdapter()
        {
            super(getActivity(), R.layout.row_categories, R.id.txvCategorieNaam, cats);
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            Category cat = cats.get(position);

            TextView naam = (TextView) row.findViewById(R.id.txvCategorieNaam);
            naam.setText(cat.getName());

            //ImageView image = (ImageView) row.findViewById(R.id.CategorieImage);
            //int img = Integer.parseInt(cat.getPicture());
            //image.setImageResource(img);

            return row;
        }

    }







}
