package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;
import data.CategoryData.Category;
import data.ReceptData.Recept;

/**
 * Created by Toine on 17/12/2014.
 */
public class ReceptCreateCategoryFragment extends Fragment{

    private Recept recCreateRecipe;
    private ArrayList<Category> selectedCategories = new ArrayList<Category>();
    private ListView lvCategories;
    private CategoryAdapter mAdapter;
    private OnNextCreateCategorySelectedListener mCallback;


    public ReceptCreateCategoryFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");
        if(recCreateRecipe.getCategories() != null){
            selectedCategories = recCreateRecipe.getCategories();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_create_categories,container, false);

        Button btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateCategorySelectedListener(recCreateRecipe, "previous");
            }
        });
        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateCategorySelectedListener(recCreateRecipe, "next");
            }
        });

        lvCategories = (ListView) view.findViewById(R.id.lvCategories);

        mAdapter = new CategoryAdapter();
        lvCategories.setAdapter(mAdapter);

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category selectedCategory = MainActivity.arrCategories.get(i);
                if(selectedCategories.contains(selectedCategory)){
                    //zit er al in, dus ongedaan maken
                    selectedCategories.remove(selectedCategories.indexOf(selectedCategory));
                } else {
                    selectedCategories.add(selectedCategory);
                }
                recCreateRecipe.setCategories(selectedCategories);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public interface OnNextCreateCategorySelectedListener{
        public void onNextCreateCategorySelectedListener(Recept recept, String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnNextCreateCategorySelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextCreateCategorySelectedListener");
        }
    }

    public class CategoryAdapter extends ArrayAdapter<Category>{
        public CategoryAdapter(){
            super(getActivity(),R.layout.row_ingredients, R.id.tv_ingredient_name, MainActivity.arrCategories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            Category thisCat = MainActivity.arrCategories.get(position);

            TextView txtName = (TextView) row.findViewById(R.id.tv_ingredient_name);
            txtName.setText(thisCat.getName());

            final ImageButton imageButton = (ImageButton) row.findViewById(R.id.riAddBasket);
            imageButton.setVisibility(View.GONE);

            if(selectedCategories.contains(thisCat)){
                row.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                row.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }

            return row;
        }
    }
}
