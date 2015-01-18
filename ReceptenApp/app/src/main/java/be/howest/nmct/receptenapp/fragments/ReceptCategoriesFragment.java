package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategory;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptCategoriesFragment extends ListFragment {
    Context context = getActivity();
    //public ArrayList<Category> categoryList = new ArrayList<Category>();

    CategorieAdapter categorieAdapter;
    private TextView txvTitle;

    //global here:
    public ArrayList<Category> arrCategories;
    public static final String ARR_CATEGORIE = "";

    //CURSOR
    MyCursorAdapter customAdapter;
    private Cursor mCursor;
    private ListView listView;
    private Uri uri;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Categorieën");
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorie, container, false);

        return view;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        getActivity().setTitle("Categorieën");

        //TEMP
        //AddFavorites();

        listView = getListView();

        String[] projection = { CategoryTable.COLUMN_ID ,CategoryTable.COLUMN_NAME, CategoryTable.COLUMN_IMAGE };

        //get data from cursor
        Context c = getActivity();
        mCursor = c.getContentResolver().query(
                ReceptenAppContentProvider.CONTENT_URI_CAT,
                projection,
                null,
                null,
                null);
        //check if not empty
        if(mCursor.getCount() < 1){
            //if empty --> load data from online
            ShowCategoriesTask taskCat = new ShowCategoriesTask();
            taskCat.execute();




        } else {

            //display
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    customAdapter = new MyCursorAdapter(
                            context,
                            mCursor,
                            0);

                    listView.setAdapter(customAdapter);
                }

            });
        }

    }

    //1. Asynctask


    class ShowCategoriesTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading data 1/3");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean isSucces = Category.LoadAllCategories(context);
            return isSucces;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }



            LoadReceptenTask taskRec = new LoadReceptenTask();
            taskRec.execute();
        }
    }
    class LoadReceptenTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog((getActivity()));
            pDialog.setMessage("Loading data 2/3");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean isSucces = Recept.LoadAllRecipesCURSOR(context);
            return isSucces;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            LoadRecByCatTask taskRecByCat = new LoadRecByCatTask();
            taskRecByCat.execute();
        }
    }
    class LoadRecByCatTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog((getActivity()));
            pDialog.setMessage("Loading data 3/3");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            Boolean succes = RecipesByCategory.getAllRecipesByCategoryCURSOR(context);
            AddFavorites();
            return succes;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            showCategories();


        }
    }

    //TEMP ADD FAVORITES
    private void AddFavorites(){
        ContentValues values = new ContentValues();
        values.put(FavoriteTable.COLUMN_ID,10);
        context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_FAV, values);

        values = new ContentValues();
        values.put(FavoriteTable.COLUMN_ID,12);
        context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_FAV, values);

        values = new ContentValues();
        values.put(FavoriteTable.COLUMN_ID,13);
        context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_FAV, values);

        values = new ContentValues();
        values.put(FavoriteTable.COLUMN_ID,15);
        context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_FAV, values);
    }

    private void showCategories() {
        String[] projection = { CategoryTable.COLUMN_ID ,CategoryTable.COLUMN_NAME, CategoryTable.COLUMN_IMAGE };
        mCursor =  context.getContentResolver().query(
                ReceptenAppContentProvider.CONTENT_URI_CAT,
                projection,
                null,
                null,
                null);


        //display
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new MyCursorAdapter(
                        getActivity(),
                        mCursor,
                        0);

                listView.setAdapter(customAdapter);
            }

        });
    }


    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;

        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            Context test = getActivity();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView content = (TextView) view.findViewById(R.id.txvCategorieNaam);
            content.setText(cursor.getString(cursor.getColumnIndex(CategoryTable.COLUMN_NAME)));

            String catImage = cursor.getString(cursor.getColumnIndex(CategoryTable.COLUMN_IMAGE));

            ImageView image = (ImageView) view.findViewById(R.id.CategorieImage);
            Bitmap bmp;
            //controleer of er een afbeelding werd geplaatst in db

            if(catImage.isEmpty()){
                //geen afbeelding opgegeven --> no_image weergeven
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noimage); //zit standaard in de app, dus hoeft niet als string worden geconverteerd
            }else{
                //afbeelding zit in database (als string)
                bmp = ImageConverter.StringToBitmap(catImage);
            }
            image.setImageBitmap(bmp);


        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_categories, parent, false);
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

            //image-stuff
            ImageView image = (ImageView) row.findViewById(R.id.CategorieImage);
            Bitmap bmp;
            //controleer of er een afbeelding werd geplaatst in db
            if(cat.getPicture().isEmpty()){
                //geen afbeelding opgegeven --> no_image weergeven
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noimage); //zit standaard in de app, dus hoeft niet als string worden geconverteerd
            }else{
                //afbeelding zit in database (als string)
                bmp = ImageConverter.StringToBitmap(cat.getPicture());
            }
            image.setImageBitmap(bmp);
            return row;
        }

    }

    //CLICK ON LIST
    OnCategorieSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnCategorieSelectedListener {
        public void OnCategorieSelectedListener(int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();

        try {
            mCallback = (OnCategorieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategorieSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCursor.moveToPosition(position);
        mCallback.OnCategorieSelectedListener(mCursor.getInt(mCursor.getColumnIndex(CategoryTable.COLUMN_ID)));
        mCursor.close();
    }



}
