package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.howest.nmct.receptenapp.R;
import data.Recept;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptBereidingFragment extends Fragment {
    onReceptBereidingSelectedListener mCallback;
    private static Recept selectedRecipe = null;

    public interface onReceptBereidingSelectedListener {
        public void onReceptBereidingSelected(String tekst); //dit moet nog changen
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (onReceptBereidingSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "\" must implement onReceptBereidingSelectedListener\"");
        }
    }

    public ReceptBereidingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recept_bereiding, container, false);

        TextView bereiding = (TextView) v.findViewById(R.id.riBereiding);
        bereiding.setText(selectedRecipe.getRecipeText());

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selectedRecipe = args.getParcelable("MYSELECTEDRECIPE");
    }
}
