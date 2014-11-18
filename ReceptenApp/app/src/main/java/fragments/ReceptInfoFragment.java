package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Ingredient;
import data.Recept;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptInfoFragment extends Fragment {
    onReceptInfoSelectedListener mCallback;
    private static Recept selectedRecipe = null;

    public interface onReceptInfoSelectedListener {
        public void onReceptInfoSelected(String tekst); //dit moet nog changen
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (onReceptInfoSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "\" must implement onReceptInfoSelectedListener\"");
        }
    }

    public ReceptInfoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recept_info, container, false);

        TextView riName = (TextView) v.findViewById(R.id.riNameRecipe);
        TextView tvDuration = (TextView) v.findViewById(R.id.tvDurationRecipe);
        TextView tvCostRecipe = (TextView) v.findViewById(R.id.tvCostRecipe);
        TextView tvNumPersons = (TextView) v.findViewById(R.id.tvNumRecipe);
        TextView tvUploadedRecipe = (TextView) v.findViewById(R.id.tvUploadedRecipe);

        //Set text
        riName.setText(selectedRecipe.getName());
        tvDuration.setText(selectedRecipe.getDuration()+"min");
        tvCostRecipe.setText(selectedRecipe.getCost()+"€");
        tvNumPersons.setText(""+selectedRecipe.getNumberOfPersons());
        tvUploadedRecipe.setText(""+selectedRecipe.getAuthor().getName());


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selectedRecipe = args.getParcelable("MYSELECTEDRECIPE");
    }
}
