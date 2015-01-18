package fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import data.IngredientData.Ingredient;
import data.ReceptData.Recept;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptIngredientenFragment extends ListFragment {
    onReceptIngredientSelectedListener mCallback;

    //CURSOR
    Cursor mCursor;
    Context context = getActivity();

    private static Recept selectedRecipe = null;
    private ListAdapter mAdapter;
    private static final String ADDEDINGREDIENTS = "added-ingredients";
    private ArrayList<Ingredient> TOEGEVOEGDEINGREDIENTEN = new ArrayList<Ingredient>();

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            //get fragments state
            TOEGEVOEGDEINGREDIENTEN = savedInstanceState.getParcelableArrayList(ADDEDINGREDIENTS);
            Ingredient test = new Ingredient(0,"Ing1");
            TOEGEVOEGDEINGREDIENTEN.add(test);
            MainActivity.BOODSCHAPPENLIJSTJE.add(test);
        }
    }

    public ReceptIngredientenFragment(){

    }

    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recept_ingredienten, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            //get fragments state
            TOEGEVOEGDEINGREDIENTEN = savedInstanceState.getParcelableArrayList(ADDEDINGREDIENTS);
            Ingredient test = new Ingredient(0,"Ing1");
            TOEGEVOEGDEINGREDIENTEN.add(test);
            MainActivity.BOODSCHAPPENLIJSTJE.add(test);
        }


        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        mCursor = context.getContentResolver().query(uri, null, null, null, null);



        mAdapter = new IngredientAdapter();
        setListAdapter(mAdapter);
    }

    public class IngredientAdapter extends ArrayAdapter<Ingredient>{
        public IngredientAdapter(){
            super(getActivity(),R.layout.row_ingredients, R.id.tv_ingredient_name, selectedRecipe.getIngredients());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Ingredient ingredient = selectedRecipe.getIngredients().get(position);
            View row = super.getView(position, convertView, parent);

            TextView tvName = (TextView) row.findViewById(R.id.tv_ingredient_name);
            tvName.setText(selectedRecipe.getIngredients().get(position).getName());

            final ImageButton imageButton = (ImageButton) row.findViewById(R.id.riAddBasket);
            if(TOEGEVOEGDEINGREDIENTEN.contains(ingredient)){
                imageButton.setImageResource(R.drawable.ic_tick);
                imageButton.setBackgroundResource(0);
            } else {
                imageButton.setImageResource(R.drawable.ic_action_new);
            }
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!MainActivity.BOODSCHAPPENLIJSTJE.contains(ingredient)){
                        MainActivity.BOODSCHAPPENLIJSTJE.add(ingredient);
                        TOEGEVOEGDEINGREDIENTEN.add(ingredient);
                        AddToBasket(selectedRecipe.getIngredients().get(position));
                        imageButton.setImageResource(R.drawable.ic_tick);
                        imageButton.setBackgroundResource(0);
                    }
                }
            });

            return row;
        }
    }

    private void AddToBasket(Ingredient ingredient) {
        //CODE OM TOE TE VOEGEN AAN WINKELMANDJE

        Toast.makeText(getActivity(), "Toegevoegd aan mandje: " + ingredient.getID(), Toast.LENGTH_SHORT).show();
    }

}
