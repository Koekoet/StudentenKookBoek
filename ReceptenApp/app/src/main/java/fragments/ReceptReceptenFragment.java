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
import data.helpers.ImageConverter;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptReceptenFragment extends ListFragment
       {

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
        txvTitle.setText(category.getName());
        //ShowReceptenTask task = new ShowReceptenTask();
        //task.execute();
        receptenAdapter = new ReceptenAdapter();
        setListAdapter(receptenAdapter);
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
            image.setImageBitmap(ImageConverter.StringToBitmap(rec.getPicture()));

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
