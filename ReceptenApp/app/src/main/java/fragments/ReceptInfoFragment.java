package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.howest.nmct.receptenapp.R;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptInfoFragment extends Fragment {
    onReceptInfoSelectedListener mCallback;

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
        return inflater.inflate(R.layout.fragment_recept_info, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
