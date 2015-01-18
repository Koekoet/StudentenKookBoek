package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceptCreateBereidingFragment extends Fragment {

    Recept recCreateRecipe;
    OnNextCreateBereidingSelectedListener mCallback;

    public ReceptCreateBereidingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recept_create_bereiding, container, false);

        Bundle args = getArguments();
        recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");

        final EditText txtBereiding = (EditText) view.findViewById(R.id.recept_bereiding);
        if(recCreateRecipe.getRecipeText() != null && !recCreateRecipe.getRecipeText().equals("") ){
            txtBereiding.setText(recCreateRecipe.getRecipeText());
        }

        Button btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recCreateRecipe.setRecipeText(txtBereiding.getText().toString());
                mCallback.onNextCreateBereidingSelectedListener(recCreateRecipe, "previous");
            }
        });

        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recCreateRecipe.setRecipeText(txtBereiding.getText().toString());
                mCallback.onNextCreateBereidingSelectedListener(recCreateRecipe, "next");
            }
        });
        return view;
    }

    public interface OnNextCreateBereidingSelectedListener{
        public void onNextCreateBereidingSelectedListener(Recept recept, String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnNextCreateBereidingSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextCreateBereidingSelectedListener");
        }
    }
}
