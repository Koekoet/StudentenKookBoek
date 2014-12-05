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
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Category;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptCategoriesFragment extends ListFragment {
    final Context context = getActivity();
    //public ArrayList<Category> categoryList = new ArrayList<Category>();

    CategorieAdapter categorieAdapter;
    private TextView txvTitle;

    //global here:
    public ArrayList<Category> arrCategories;
    public static final String ARR_CATEGORIE = "";



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        arrCategories = bundle.getParcelableArrayList(ARR_CATEGORIE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorie, container, false);
        ((TextView) view.findViewById(R.id.Title)).setText("");
        txvTitle = (TextView) view.findViewById(R.id.Title);
        categorieAdapter = new CategorieAdapter();
        setListAdapter(categorieAdapter);
        return view;

    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        //ShowCategoriesTask task = new ShowCategoriesTask();
        //task.execute();

    }

    //1. Asynctask


    class ShowCategoriesTask extends AsyncTask<String, Void, ArrayList<Category>> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading data");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected ArrayList<Category> doInBackground(String... params) {

            ArrayList<Category> categories = data.Category.getAllCategories();
            for (Category cat : categories){
                cat.setPicture("" + R.drawable.cat_vleesgerechten);
            }
            return categories;
        }
        @Override
        protected void onPostExecute(ArrayList<Category> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            arrCategories = result;
            categorieAdapter = new CategorieAdapter();
            setListAdapter(categorieAdapter);
            Toast.makeText(getActivity(), "Categories ready.", Toast.LENGTH_SHORT).show();
        }
    }

    class CategorieAdapter extends ArrayAdapter<Category> {
        public CategorieAdapter() {
            super(getActivity(), R.layout.row_categories, R.id.txvCategorieNaam, arrCategories);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            Category cat = arrCategories.get(position);

            TextView naam = (TextView) row.findViewById(R.id.txvCategorieNaam);
            naam.setText(cat.getName());

            ImageView image = (ImageView) row.findViewById(R.id.CategorieImage);
            int img = Integer.parseInt(cat.getPicture());
            image.setImageResource(img);

            return row;
        }

    }

    //CLICK ON LIST
    OnCategorieSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnCategorieSelectedListener {
        public void OnCategorieSelectedListener(Category category);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCategorieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategorieSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.OnCategorieSelectedListener(arrCategories.get(position));
    }


}
