package be.howest.nmct.receptenapp.fragments;

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
import be.howest.nmct.receptenapp.data.AuthorData.AuthorTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

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

        String test = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_AUTHORID));
        //byte[] test2 = mCursor.getBlob(mCursor.getColumnIndex(ReceptTable.COLUMN_AUTHORID));
        //String testID = String.format("%.12f",test);


        //Set text
        riName.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NAME)));
        tvDuration.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_DURATION)) + " min");
        tvCostRecipe.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_COST)) + " €");
        tvNumPersons.setText(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NUMBEROFPERSONS)));
        tvUploadedRecipe.setText(findAuthorByID(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_AUTHORID))));

        String picture = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_PICTURE));
        if (picture != null && !picture.isEmpty()) {
            Bitmap bm = ImageConverter.StringToBitmap(picture);
            ivImageRecipe.setImageBitmap(bm);
        } else {
            ivImageRecipe.setImageResource(R.drawable.ic_noimage);
        }
        return v;
    }

    private String findAuthorByID(String authorID) {
        Uri uri = Uri.parse((ReceptenAppContentProvider.CONTENT_URI_AUTHOR + ""));
        Cursor authors = context.getContentResolver().query(uri,null,null,null,null);
        authors.moveToFirst();
        String name ="";
        for (int i = 0; i < authors.getCount(); i++) {
            String authorAll = authors.getString(authors.getColumnIndex(AuthorTable.COLUMN_ID));
            if(authorID.equals(authorAll)){
                name = authors.getString(authors.getColumnIndex(AuthorTable.COLUMN_FNAME)) + " ";
                name += authors.getString(authors.getColumnIndex(AuthorTable.COLUMN_LNAME));
                return name;
            }
            authors.moveToNext();
        }
        return "";
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
