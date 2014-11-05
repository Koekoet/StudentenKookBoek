package fragments;

import android.app.Activity;
import android.app.Fragment;

import data.Recept;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptBereidingFragment extends Fragment {
    onReceptBereidingSelectedListener mCallback;

    public interface onReceptBereidingSelectedListener {
        public void onReceptBereidingSelected(Recept Recept);
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

}
