package fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
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
        ImageView ivImageRecipe = (ImageView) v.findViewById(R.id.ivPictureRecipe);


        //Set text
        riName.setText(selectedRecipe.getName());
        tvDuration.setText(selectedRecipe.getDuration()+"min");
        tvCostRecipe.setText(selectedRecipe.getCost()+"€");
        tvNumPersons.setText(""+selectedRecipe.getNumberOfPersons());
        tvUploadedRecipe.setText(""+selectedRecipe.getAuthor().getName());
        if(selectedRecipe.getPicture() != null){

        } else {
            ivImageRecipe.setImageResource(R.drawable.ic_noimage);
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selectedRecipe = args.getParcelable("MYSELECTEDRECIPE");
    }

    public byte[] ConvertImageToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public Bitmap ConvertByteArrayToImage(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

}
