package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptBereidingFragment extends Fragment {
    //CURSOR
    Context context = getActivity();
    private Cursor mCursor;

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

        mCursor.moveToFirst();

        TextView bereiding = (TextView) v.findViewById(R.id.riBereiding);
        bereiding.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_RECIPETEXT)));

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();

        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        mCursor = context.getContentResolver().query(uri, null, null, null, null);
    }
}
