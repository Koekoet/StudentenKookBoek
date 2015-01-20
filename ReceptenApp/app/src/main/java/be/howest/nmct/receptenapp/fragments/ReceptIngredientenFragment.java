package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.BoodschappenData.BasketTable;
import be.howest.nmct.receptenapp.data.IngredientData.Ingredient;
import be.howest.nmct.receptenapp.data.IngredientData.IngredientTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptIngredientenFragment extends ListFragment {
    onReceptIngredientSelectedListener mCallback;

    //CURSOR
    Cursor receptCursor;
    Cursor ingrCursor;
    Context context = getActivity();
    ListView listView;

    private static Recept selectedRecipe = null;
    private ListAdapter mAdapter;
    private static final String ADDEDINGREDIENTS = "added-ingredients";
    private ArrayList<Ingredient> TOEGEVOEGDEINGREDIENTEN = new ArrayList<Ingredient>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        //get recept
        receptCursor = context.getContentResolver().query(uri, null, null, null, null);
        //get IngrCursor
        ingrCursor = GetReceptIngredients();

    }

    private Cursor GetReceptIngredients() {
        receptCursor.moveToFirst();
        String sIngredientIDs = receptCursor.getString(receptCursor.getColumnIndex(ReceptTable.COLUMN_INGREDIENTS));
        if(!sIngredientIDs.equals("")){
            String[] IDs = sIngredientIDs.split(";");
            String[] projection = {IngredientTable.COLUMN_ID, IngredientTable.COLUMN_NAME, IngredientTable.COLUMN_AMOUNT, IngredientTable.COLUMN_UNITID};

            if(IDs.length == 1){
                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[0]);
                return context.getContentResolver().query(uri, projection, null, null, null);
            } else if(IDs.length == 2){
                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[0]);
                Cursor ingr1 = context.getContentResolver().query(uri, projection, null, null, null);

                uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[1]);
                Cursor ingr2 = context.getContentResolver().query(uri, projection, null, null, null);

                return new MergeCursor(new Cursor[] {ingr1, ingr2});
            } else {
                Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[0]);
                Cursor ingr1 = context.getContentResolver().query(uri, projection, null, null, null);

                uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[1]);
                Cursor ingr2 = context.getContentResolver().query(uri, projection, null, null, null);
                MergeCursor ingredients = new MergeCursor(new Cursor[] {ingr1, ingr2});

                for (int i = 2; i < IDs.length; i++) {
                    if(IDs[i] != ""){
                        uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_INGR + "/" + IDs[i]);
                        Cursor ingr = context.getContentResolver().query(uri, projection, null, null, null);
                        ingredients =  new MergeCursor(new Cursor[] { ingredients, ingr });
                    }
                }
                return ingredients;
            }

        }

        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recept_ingredienten, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        listView = getListView();


        if(ingrCursor != null && ingrCursor.getCount() != 0){
            //display
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    mAdapter = new MyCursorAdapter(
                            context,
                            ingrCursor,
                            0);

                    listView.setAdapter(mAdapter);
                }

            });
        }
    }

    public interface onReceptIngredientSelectedListener {
        public void onReceptIngredientSelectedListener(String tekst); //dit moet nog changen
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (onReceptIngredientSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "\" must implement onReceptIngredientSelectedListener\"");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save fragment
        outState.putParcelableArrayList(ADDEDINGREDIENTS, TOEGEVOEGDEINGREDIENTEN);

        super.onSaveInstanceState(outState);
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
            final int ID = cursor.getInt(cursor.getColumnIndex(IngredientTable.COLUMN_ID));
            final Context ingContext = context;
            final String name = cursor.getString(cursor.getColumnIndex(IngredientTable.COLUMN_NAME));
            final String amount = cursor.getString(cursor.getColumnIndex(IngredientTable.COLUMN_AMOUNT));
            final String unitid = cursor.getString(cursor.getColumnIndex(IngredientTable.COLUMN_UNITID));
            TextView tvName = (TextView) view.findViewById(R.id.tv_ingredient_name);
            tvName.setText(cursor.getString(cursor.getColumnIndex(IngredientTable.COLUMN_NAME)));

            final ImageButton imageButton = (ImageButton) view.findViewById(R.id.riAddBasket);
            imageButton.setImageResource(R.drawable.ic_action_new);
            imageButton.setBackgroundResource(0);
            if(isInBasket(ID)){
                imageButton.setImageResource(R.drawable.ic_tick);
            } else {
                imageButton.setImageResource(R.drawable.ic_action_new);
            }
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isInBasket(ID)){
                        //INSErt iN BOODSCHPAN
                        ContentValues values = new ContentValues();
                        values.put(BasketTable.COLUMN_ID,ID);
                        values.put(BasketTable.COLUMN_NAME,name);
                        values.put(BasketTable.COLUMN_AMOUNT,amount);
                        values.put(BasketTable.COLUMN_UNITID,unitid);
                        ingContext.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_BASKET, values);
                        imageButton.setImageResource(R.drawable.ic_tick);
                    } else {
                        //REMOVE FORM BOODSCHAP
                        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_BASKET + "/" + ID);
                        ingContext.getContentResolver().delete(uri, null, null);
                        imageButton.setImageResource(R.drawable.ic_action_new);
                    }
                }
            });
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_ingredients, parent, false);
        }
    }

    private Boolean isInBasket(int id){
        Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_BASKET + "/" + id);
        Cursor ingCursor = context.getContentResolver().query(uri,null,null,null,null);

        if(ingCursor.getCount() > 0){return true;}
        return false;
    }
    private void AddToBasket(Ingredient ingredient) {
        //CODE OM TOE TE VOEGEN AAN WINKELMANDJE

        Toast.makeText(getActivity(), "Toegevoegd aan mandje: " + ingredient.getID(), Toast.LENGTH_SHORT).show();
    }

}
