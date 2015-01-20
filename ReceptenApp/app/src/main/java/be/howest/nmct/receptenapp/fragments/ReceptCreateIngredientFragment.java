package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.IngredientData.Ingredient;
import be.howest.nmct.receptenapp.data.IngredientData.IngredientTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.UnitData.UnitTable;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

public class ReceptCreateIngredientFragment extends ListFragment {
    ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
    private Recept recCreateRecipe;
    OnNextCreateIngredientSelectedListener mCallback;
    IngredientAdapter mAdapter;
    ListView lvIngredients;
    private Cursor unitCursor;
    private int clickedOnAdd = 0;

    public ReceptCreateIngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");


        String[] projection = {UnitTable.COLUMN_ID ,UnitTable.COLUMN_NAME, UnitTable.COLUMN_ABBREVIATION };

        //get data from cursor
        Context c = getActivity();
        unitCursor = c.getContentResolver().query(ReceptenAppContentProvider.CONTENT_URI_UNIT, projection, null, null, null);



        if(recCreateRecipe.getIngredients() != null && recCreateRecipe.getIngredients().size()>0){
            //Dan zitten er al ingredients in...
            allIngredients = recCreateRecipe.getIngredients();
        } else {
            allIngredients = new ArrayList<Ingredient>();
            allIngredients.add(new Ingredient(-1,""));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvIngredients = getListView();

        mAdapter = new IngredientAdapter();
        lvIngredients.setAdapter(mAdapter);
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
                saveIngredients();
                recCreateRecipe.setIngredients(allIngredients);
                mCallback.onNextCreateIngredientSelectedListener(recCreateRecipe, "previous");
            }
        });
        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIngredients();
                recCreateRecipe.setIngredients(allIngredients);
                mCallback.onNextCreateIngredientSelectedListener(recCreateRecipe, "next");
            }
        });

        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.btnAddNewIng);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIngredients();
                allIngredients.add(new Ingredient(-1, ""));
                mAdapter = new IngredientAdapter();
                lvIngredients.setAdapter(mAdapter);
            }
        });

        return view;
    }
    //MATTIS CODE
    private void saveIngredients(){
        ArrayList<Ingredient> saveValidIngr = new ArrayList<Ingredient>();
        for(int i = 0; i < allIngredients.size(); i++){
            View row = lvIngredients.getChildAt(i);
            EditText txtName = (EditText) row.findViewById(R.id.ingName);

            EditText ingQuan = (EditText) row.findViewById(R.id.ingQuan);

            if(!txtName.getText().toString().equals("") && !ingQuan.getText().toString().equals("0"))
            {
                Spinner spUnit = (Spinner) row.findViewById(R.id.ingUnit);
                Ingredient ing = new Ingredient();
                ing.setID(i);
                ing.setName(txtName.getText().toString());
                if(!ingQuan.getText().toString().equals("")){
                    ing.setAmount(Integer.parseInt(ingQuan.getText().toString()));
                } else {
                    ing.setAmount(0);
                }
                ing.setUnitID(spUnit.getSelectedItemPosition());
                saveValidIngr.add(ing);
            }


        }
        allIngredients = saveValidIngr;
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
            super(getActivity(),R.layout.row_create_ingredient, R.id.ingName, allIngredients);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            EditText ingName = (EditText) row.findViewById(R.id.ingName);
            EditText ingQuan = (EditText) row.findViewById(R.id.ingQuan);
            Spinner spUnit = (Spinner) row.findViewById(R.id.ingUnit);

            //SPINER
            String[] columns = new String[] { UnitTable.COLUMN_ABBREVIATION };
            int[] to = new int[] { android.R.id.text1 };

            SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, unitCursor, columns, to);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spUnit.setAdapter(mAdapter);

            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,units);
            spUnit.setAdapter(adapter);
*/
            int thisID = allIngredients.get(position).getID();
            if(thisID == -1){
                //Dan is hij 'leeg'
                ingName.setText("");
                ingQuan.setText("");
                spUnit.setSelection(0);
            } else {
                ingName.setText(allIngredients.get(position).getName());
                ingQuan.setText(""+allIngredients.get(position).getAmount());
                spUnit.setSelection(allIngredients.get(position).getUnitID());
            }

            return row;
        }
    }
}
