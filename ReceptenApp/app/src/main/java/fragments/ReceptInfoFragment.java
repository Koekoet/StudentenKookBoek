package fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import data.ReceptData.Recept;
import data.ReceptData.ReceptTable;

/**
 * Created by Toine on 5/11/2014.
 */
public class ReceptInfoFragment extends Fragment {
    onReceptInfoSelectedListener mCallback;
    private static Recept selectedRecipe = null;
    Context context = getActivity();
    private Cursor mCursor;

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
        mCursor.moveToFirst();

        TextView riName = (TextView) v.findViewById(R.id.riNameRecipe);
        TextView tvDuration = (TextView) v.findViewById(R.id.tvDurationRecipe);
        TextView tvCostRecipe = (TextView) v.findViewById(R.id.tvCostRecipe);
        TextView tvNumPersons = (TextView) v.findViewById(R.id.tvNumRecipe);
        TextView tvUploadedRecipe = (TextView) v.findViewById(R.id.tvUploadedRecipe);
        ImageView ivImageRecipe = (ImageView) v.findViewById(R.id.ivPictureRecipe);

        //Set text
        riName.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NAME)));
        tvDuration.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_DURATION)) + " min");
        tvCostRecipe.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_COST)) + " €");
        tvNumPersons.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NUMBEROFPERSONS)));
        tvUploadedRecipe.setText("user not defined yet"/*+selectedRecipe.getAuthor().getName()*/);

        String picture = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_PICTURE));
        if (picture != null && !picture.isEmpty()) {
            Bitmap bm = data.helpers.ImageConverter.StringToBitmap(picture);
            ivImageRecipe.setImageBitmap(bm);
        } else {
            ivImageRecipe.setImageResource(R.drawable.ic_noimage);
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        Bundle bundle = this.getArguments();
        Uri uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        mCursor = context.getContentResolver().query(uri, null, null, null, null);
    }


}
