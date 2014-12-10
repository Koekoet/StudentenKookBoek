package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.Difficulty;
import data.Recept;

/**
 * Created by Toine on 3/12/2014.
 */
public class ReceptCreateInfoFragment extends Fragment {

    static TextView txtName;
    static TextView txtDuration;
    static TextView txtPersons;
    OnNextCreateInfoSelectedListener mCallback;
    Recept recCreateRecipe;
    private final int REQUEST_CAMERA = 2999;
    private final int SELECT_FILE = 3999;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_info, container, false);
        final Spinner spinnerCost = (Spinner) view.findViewById(R.id.ciCostRecipeSpinner);
        final Spinner spinnerDiff = (Spinner) view.findViewById(R.id.ciDiffSpinner);
        txtName = (TextView) view.findViewById(R.id.ciNameRecipe);
        txtDuration = (TextView) view.findViewById(R.id.ciDurationName);
        txtPersons = (TextView) view.findViewById(R.id.numPersons);
        Bundle args = getArguments();
        if(args != null){
            //er zit iets in
            recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");
            //waarden invullen
            txtName.setText(recCreateRecipe.getName());
            txtDuration.setText(recCreateRecipe.getDuration());
            txtPersons.setText(""+recCreateRecipe.getNumberOfPersons());
            //Cost & difficulty nog!
            //spinnerCost.setSelection(recCreateRecipe.getCost());
            spinnerDiff.setSelection(recCreateRecipe.getDifficultyID(), false);
        } else {
            recCreateRecipe = new Recept();
        }
        imageView = (ImageView) view.findViewById(R.id.recept_image_create);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "Camera", "Galerij",
                        "Annuleer" };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Selecteer afbeelding");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(items[i].equals("Camera")){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent,REQUEST_CAMERA);
                        } else if(items[i].equals("Galerij")){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"selecteer bestand"),SELECT_FILE);
                        } else if(items[i].equals("Annuleer")){
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        Button btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        Button btnNext = (Button) view.findViewById(R.id.btnNext);

        btnPrevious.setEnabled(false);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    recCreateRecipe.setName(txtName.getText().toString());
                    recCreateRecipe.setDuration(txtDuration.getText().toString());
                    recCreateRecipe.setCost(spinnerCost.getSelectedItem().toString());
                    recCreateRecipe.setNumberOfPersons(Integer.parseInt(txtPersons.getText().toString()));

                    //Nog difficulty
                    recCreateRecipe.setDifficultyID((int) spinnerDiff.getSelectedItemId());
                    //recept.setDifficulty(Difficulty.getDifficultyById((int) spinnerDiff.getSelectedItemId()));
                    mCallback.onNextCreateInfoSelectedListener(recCreateRecipe);
                }
            }
        });

        ArrayList<String> arrayCost = fillCostAdapter();
        ArrayList<String> arrayDiff = fillDiffAdapter();

        ArrayAdapter<String> adapterCost = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayCost);
        ArrayAdapter<String> adapterDiff = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayDiff);

        adapterCost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCost.setAdapter(adapterCost);
        spinnerDiff.setAdapter(adapterDiff);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for(File temp : f.listFiles()){
                    if(temp.getName().equals("temp.jpg")){
                        f = temp;
                        break;
                    }
                }
                try{
                    Bitmap bm;
                    BitmapFactory.Options btMapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                    imageView.setImageBitmap(bm);

                    String path = android.os.Environment.getExternalStorageState() + File.separator + "ReceptApp" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis())+".jpg");
                    try{
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if(requestCode == SELECT_FILE){
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btMapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btMapOptions);
                imageView.setImageBitmap(bm);
            }
        }
    }

    private String getPath(Uri uri, Activity activity) {
        String res = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null,null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public interface OnNextCreateInfoSelectedListener{
        public void onNextCreateInfoSelectedListener(Recept recept);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnNextCreateInfoSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextCreateInfoSelectedListener");
        }
    }

    private ArrayList<String> fillDiffAdapter() {
        ArrayList<String> arrayDiff = new ArrayList<String>();
        /*ArrayList<Difficulty> arrayList = Difficulty.getAllDifficulties();
        for(Difficulty diff : arrayList){
            arrayDiff.add(diff.getDescription());
        }*/
        arrayDiff.add("Beginner");
        arrayDiff.add("Gevorderde");
        arrayDiff.add("Professional");
        arrayDiff.add("Instructeur");

        return arrayDiff;
    }

    private ArrayList<String> fillCostAdapter() {
        ArrayList<String> arrayCost = new ArrayList<String>();
        //arrayCost.add("-- kies --");
        arrayCost.add("<5€");
        arrayCost.add("5€ - 10€");
        arrayCost.add("10€ - 20€");
        arrayCost.add(">20€");
        return arrayCost;
    }

    public static boolean isValid() {
        boolean isValidName = isValidName();
        boolean isValidDuration = isValidDuration();
        boolean isValidPersons = isValidPersons();

        if(isValidName && isValidDuration && isValidPersons){
            return true;
        } else {
            return false;
        }
    }

    private static boolean isValidName() {
        String name = txtName.getText().toString();
        if(name == null || name.equals("") || name.startsWith(" ")){
            txtName.setError(txtName.getHint() + " verplicht!");
            return false;
        }
        return true;
    }
    private static boolean isValidDuration() {
        String durationString = txtDuration.getText().toString();
        if(durationString.equals("") || durationString.startsWith(" ")){
            txtDuration.setError(txtDuration.getHint() + " verplicht!");
            return false;
        }
        int duration = Integer.parseInt(durationString);
        if(duration < 0){
            txtDuration.setError(txtDuration.getHint() + " verplicht!");
            return false;
        }
        return true;
    }
    private static boolean isValidPersons() {
        String personsString = txtPersons.getText().toString();
        if(personsString.equals("") || personsString.startsWith(" ")){
            txtPersons.setError(txtPersons.getHint() + " verplicht!");
            return false;
        }
        int persons = Integer.parseInt(txtPersons.getText().toString());
        if(persons < 0){
            txtPersons.setError(txtPersons.getHint() + " verplicht!");
            return false;
        }
        return true;
    }
}
