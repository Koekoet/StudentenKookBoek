package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.receptenapp.R;
import data.Recept;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptBereidingFragment extends Fragment {
    onReceptBereidingSelectedListener mCallback;

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
        return inflater.inflate(R.layout.fragment_recept_bereiding, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
