package be.howest.nmct.receptenapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;
import be.howest.nmct.receptenapp.data.helpers.SwipeDismissListViewTouchListener;

/**
 * Created by Toine on 25/11/2014.
 */
public class ReceptFavoriteFragment extends ListFragment {
    Context context = getActivity();
    Cursor mCursor;
    ListView listView;
    MyCursorAdapter favoriteAdapter;


    //GLOBAL
    public static final String ARR_FAVORITE_RECIPES = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            /*case R.id.action_TestRecepi:
                return false;
            case R.id.action_TestFavorite:
                return false;*/
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alles verwijderen");
                builder.setMessage("Bent u zeker dat u alle items in favorieten wilt verwijderen?");
                builder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_FAV + "");
                                context.getContentResolver().delete(uri, null, null);

                                mCursor = context.getContentResolver().query( ReceptenAppContentProvider.CONTENT_URI_FAV,
                                        null,
                                        null,
                                        null,
                                        null);
                                favoriteAdapter.swapCursor(mCursor);

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setSubtitle("Favorieten");
        listView = getListView();
       showFavorites();


        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {return true; }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for(int position : reverseSortedPositions){
                            //favoriteAdapter.remove(favoriteAdapter.getItem(position));
                            mCursor.moveToPosition(position);
                            String id = mCursor.getString(mCursor.getColumnIndex(FavoriteTable.COLUMN_ID));
                            Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_FAV + "/" + id);
                            context.getContentResolver().delete(uri, null, null);

                            mCursor = context.getContentResolver().query( ReceptenAppContentProvider.CONTENT_URI_FAV,
                                    null,
                                    null,
                                    null,
                                    null);
                            favoriteAdapter.swapCursor(mCursor);
                        }
                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), favorietenLijst.get(i).getName(), Toast.LENGTH_SHORT).show();
                mCursor.moveToPosition(i);
                String id = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_ID));
                mCursor.close();
                ReceptDetailFragment fragment = new ReceptDetailFragment();
                Bundle bundle = new Bundle();
                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_REC + "/" + id);
                bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.mainfragment,fragment).addToBackStack(null).commit();
            }
        });
    }

    private void showFavorites() {
        listView = getListView();
        mCursor = context.getContentResolver().query(
                ReceptenAppContentProvider.CONTENT_URI_FAV,
                null,
                null,
                null,
                null);

        if(mCursor != null){
            int aantal = mCursor.getCount();
            //display
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    favoriteAdapter = new MyCursorAdapter(
                            getActivity(),
                            mCursor,
                            0);

                    listView.setAdapter(favoriteAdapter);
                }

            });
        }
    }

    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;
        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView naam = (TextView) view.findViewById(R.id.recept_naam);
            naam.setText(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_NAME)));

            ImageView image = (ImageView) view.findViewById(R.id.receptImage);
            image.setImageBitmap(ImageConverter.StringToBitmap(cursor.getString(cursor.getColumnIndex(ReceptTable.COLUMN_PICTURE))));
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_favorites, parent, false);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, null, false);
    }
}
