package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.RecipeView;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

/**
 * Created by Mattias on 18/01/2015.
 */
public class ReceptSearchFragment extends ListFragment {

    Context context = getActivity();
    //ReceptenAdapter receptenAdapter;
    private TextView txvTitle;
    private ListView listView;

    //CURSOR
    private Cursor mCursor;
    MyCursorAdapter receptenAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTitle(category.getName());
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        listView = getListView();

        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        TextView query = (TextView) v.findViewById(R.id.search_query);
        query.setText("Resultaten voor: " + uri.getLastPathSegment());
        mCursor = context.getContentResolver().query(uri, null, null, null, null);


        if(mCursor != null && mCursor.getCount() != 0){
            //display
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    receptenAdapter = new MyCursorAdapter(getActivity(), mCursor,0);
                    listView.setAdapter(receptenAdapter);
                }

            });
        }

        getActivity().getActionBar().setSubtitle("zoekresultaten");

    }


    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;
        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView naam = (TextView) view.findViewById(R.id.search_recept_name);
            naam.setText(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_NAME)));

            ImageView image = (ImageView) view.findViewById(R.id.search_recept_img);
            image.setImageBitmap(ImageConverter.StringToBitmap(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_PICTURE))));
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_search, parent, false);
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCursor.moveToPosition(position);
        String recID = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_ID));
        ReceptDetailFragment fragment = new ReceptDetailFragment();
        Bundle bundle = new Bundle();
        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/" + recID);
        bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null){
            MenuItem refresh = menu.findItem(R.id.menu_item_refresh);
            refresh.setVisible(false);
        }
        inflater.inflate(R.menu.search, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setOnQueryTextListener(queryListener);


    }
    private String grid_currentQuery = null; // holds the current query...
    private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {

                grid_currentQuery = null;
            } else {
               grid_currentQuery = newText;

            }
            //getLoaderManager().restartLoader(0, null, MyListFragment.this);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            ReceptSearchFragment recSFrag = new ReceptSearchFragment();
            Bundle bundle = new Bundle();
            Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/RecByQUERY/" + query);
            bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
            recSFrag.setArguments(bundle);
            getFragmentManager().popBackStack("SEARCH", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getFragmentManager().beginTransaction().replace(R.id.mainfragment, recSFrag).addToBackStack("SEARCH").commit();
            return false;
        }
    };



}