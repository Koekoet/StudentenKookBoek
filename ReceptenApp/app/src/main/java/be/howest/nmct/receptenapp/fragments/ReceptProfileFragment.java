package be.howest.nmct.receptenapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import be.howest.nmct.receptenapp.MainActivity;
import be.howest.nmct.receptenapp.R;

/**
 * Created by Toine on 20/01/2015.
 */
public class ReceptProfileFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_profile, container, false);

        ImageView ivImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        TextView txtProfileName = (TextView) view.findViewById(R.id.txbProfileName);
        TextView txtFirstName = (TextView) view.findViewById(R.id.txtFirstName);
        TextView txtFamilyName = (TextView) view.findViewById(R.id.txtFamilyName);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtEmail);

        //settext:
        if(MainActivity.LOGGEDINUSER != null){
            if(!MainActivity.LOGGEDINUSER.getImage().equals("")){
                new ImageDownloader(ivImage).execute(MainActivity.LOGGEDINUSER.getImage());
            }
            txtProfileName.setText(MainActivity.LOGGEDINUSER.getFirstname() + " " + MainActivity.LOGGEDINUSER.getLastname());
            txtFirstName.setText(MainActivity.LOGGEDINUSER.getFirstname());
            txtFamilyName.setText(MainActivity.LOGGEDINUSER.getLastname());
            txtEmail.setText(MainActivity.LOGGEDINUSER.getEmail());
        }

        return view;
    }
}

class ImageDownloader extends AsyncTask<String, Void, Bitmap>{
    ImageView bmImage;
    public ImageDownloader(ImageView bmImage){
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon = null;
        try{
            InputStream in = new URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
