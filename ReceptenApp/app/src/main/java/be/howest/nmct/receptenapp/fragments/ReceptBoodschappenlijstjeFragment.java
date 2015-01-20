package be.howest.nmct.receptenapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.BoodschappenData.BasketTable;
import be.howest.nmct.receptenapp.data.UnitData.UnitTable;
import be.howest.nmct.receptenapp.data.helpers.SwipeDismissListViewTouchListener;

/**
 * Created by Mattias on 9/12/2014.
 */
public class ReceptBoodschappenlijstjeFragment extends ListFragment {

    //CURSOR
    private Cursor mCursor;
    private MyCursorAdapter mAdapter;
    private ListView listView;
    private Context context;

    private ArrayList<String> arrBoodschappenlijstje;
    private static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        getActivity().setTitle("Mijn boodschappenlijstje");
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setSubtitle("Boodschappenlijstje");
        listView = getListView();
        mCursor = context.getContentResolver().query(
                ReceptenAppContentProvider.CONTENT_URI_BASKET,
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
                    mAdapter = new MyCursorAdapter(
                            getActivity(),
                            mCursor,
                            0);

                    listView.setAdapter(mAdapter);
                }

            });
        }
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {return true; }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for(int position : reverseSortedPositions){
                            mCursor.moveToPosition(position);
                            int ID = mCursor.getInt(mCursor.getColumnIndex(BasketTable.COLUMN_ID));
                            Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_BASKET + "/" + ID);
                            context.getContentResolver().delete(uri, null, null);

                            mCursor = context.getContentResolver().query( ReceptenAppContentProvider.CONTENT_URI_BASKET,
                                    null,
                                    null,
                                    null,
                                    null);
                            mAdapter.swapCursor(mCursor);
                        }
                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String boodschapDialog = "";
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), arrBoodschappenlijstje.get(i), Toast.LENGTH_SHORT).show();
                //boodschapDialog = arrBoodschappenlijstje.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Wijzig");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(boodschapDialog);
                builder.setView(input);



// Set up the buttons

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boodschapDialog =  input.getText().toString();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                //arrBoodschappenlijstje.set(i, boodschapDialog);
                //mAdapter = new BoodschappenlijstjeAdapter();
                setListAdapter(mAdapter);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_boodschappenlijstje, container, false);
        } catch(InflateException e){

        }
        return view;

    }

    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;
        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView naam = (TextView) view.findViewById(R.id.txvBoodschap);
            naam.setText(createBooschapText(cursor));

        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_boodschappenlijstje, parent, false);
        }
    }

    private String createBooschapText(Cursor cursor){
        String text = "";
        text += cursor.getString(cursor.getColumnIndex(BasketTable.COLUMN_AMOUNT));
        text += getAbbreviation(cursor.getInt(cursor.getColumnIndex(BasketTable.COLUMN_UNITID))) + " ";
        text += cursor.getString(cursor.getColumnIndex(BasketTable.COLUMN_NAME)) + " ";
        return text;
    }
    private String getAbbreviation(int id){
        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_UNIT + "/" + id);
        Cursor unitCursor = context.getContentResolver().query(uri, null, null, null, null);

        //Text
        Cursor test = context.getContentResolver().query(ReceptenAppContentProvider.CONTENT_URI_UNIT, null, null, null, null);
        int countTest = unitCursor.getCount();

        unitCursor.moveToFirst();
        return unitCursor.getString(unitCursor.getColumnIndex(UnitTable.COLUMN_ABBREVIATION));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null){
            MenuItem refresh = menu.findItem(R.id.menu_item_refresh);
            refresh.setVisible(false);
        }
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alles verwijderen");
                builder.setMessage("Bent u zeker dat u alle boodschappen wilt verwijderen?");
                builder.setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_BASKET + "");
                                context.getContentResolver().delete(uri, null, null);

                                mCursor = context.getContentResolver().query( ReceptenAppContentProvider.CONTENT_URI_BASKET,
                                        null,
                                        null,
                                        null,
                                        null);
                                mAdapter.swapCursor(mCursor);

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



}
