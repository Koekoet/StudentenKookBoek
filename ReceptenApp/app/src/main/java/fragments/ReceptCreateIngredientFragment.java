package fragments;

import android.app.Activity;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;
import data.Ingredient;
import data.Recept;

import static fragments.ReceptIngredientenFragment.*;

public class ReceptCreateIngredientFragment extends Fragment {
    ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
    private Recept recCreateRecipe;
    OnNextCreateIngredientSelectedListener mCallback;
    ArrayList<Ingredient> selectedIngredients = new ArrayList<Ingredient>();
    IngredientAdapter mAdapter;
    ListView lvIngredients;

    public ReceptCreateIngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*

        new GetAllIngredients.execute();

         */


        Bundle args = getArguments();
        recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");
        if(recCreateRecipe.getIngredients() != null){
            selectedIngredients = recCreateRecipe.getIngredients();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recept_create_ingredient, container, false);
        Button btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateIngredientSelectedListener(recCreateRecipe, "previous");
            }
        });
        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateIngredientSelectedListener(recCreateRecipe, "next");
            }
        });

        lvIngredients = (ListView) view.findViewById(R.id.lvIngredients);

        mAdapter = new IngredientAdapter();
        lvIngredients.setAdapter(mAdapter);

        //-----Temp data
        Ingredient patat = new Ingredient(0,"Aardappelen");
        Ingredient gehakt = new Ingredient(1,"Gehakt");
        Ingredient bloemkool = new Ingredient(2,"Bloemkool");
        Ingredient boter = new Ingredient(3,"Boter");
        Ingredient water = new Ingredient(4,"Water");

        allIngredients.add(patat);
        allIngredients.add(gehakt);
        allIngredients.add(bloemkool);
        allIngredients.add(boter);
        allIngredients.add(water);
        //-----End Temp data

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Ingredient selectIngredient = allIngredients.get(position);
                if(selectedIngredients.contains(selectIngredient)){
                    //zit er al in, dus ongedaan maken
                    selectedIngredients.remove(selectedIngredients.indexOf(selectIngredient));
                } else {
                    selectedIngredients.add(selectIngredient);
                }
                recCreateRecipe.setIngredients(selectedIngredients);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public interface OnNextCreateIngredientSelectedListener{
        public void onNextCreateIngredientSelectedListener(Recept recept, String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnNextCreateIngredientSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextCreateIngredientSelectedListener");
        }
    }

    public class IngredientAdapter extends ArrayAdapter<Ingredient> {
        public IngredientAdapter(){
            super(getActivity(),R.layout.row_ingredients, R.id.tv_ingredient_name, allIngredients);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            Ingredient thisIngredient = allIngredients.get(position);

            TextView txtName = (TextView) row.findViewById(R.id.tv_ingredient_name);
            txtName.setText(allIngredients.get(position).getName());

            final ImageButton imageButton = (ImageButton) row.findViewById(R.id.riAddBasket);
            imageButton.setVisibility(View.GONE);

            if(selectedIngredients.contains(thisIngredient)){
                row.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                row.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            return row;
        }
    }
    class GetAllIngredients extends AsyncTask<Void,Void,ArrayList<Ingredient>>{

        @Override
        protected ArrayList<Ingredient> doInBackground(Void... voids) {
            ArrayList<Ingredient> allIngredients = Ingredient.getAllIngredients();
            return allIngredients;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingredients) {
            super.onPostExecute(ingredients);
            allIngredients = ingredients;
            mAdapter = new IngredientAdapter();
            lvIngredients.setAdapter(mAdapter);
        }
    }
}
