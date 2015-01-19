package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.helpers.ImageConverter;

/**
 * Created by Toine on 17/12/2014.
 */
public class ReceptCreateCategoryFragment extends ListFragment {

    private Recept recCreateRecipe;
    private ArrayList<Category> selectedCategories = new ArrayList<Category>();
    private ListView lvCategories;
    private OnNextCreateCategorySelectedListener mCallback;
    private Cursor mCursor;
    Context context = getActivity();
    private MyCursorAdapter customAdapter;
    private ArrayList<Integer> selectedIDs = new ArrayList<Integer>();

    public ReceptCreateCategoryFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        recCreateRecipe = args.getParcelable("CREATERECIPEVALUES");
        if(recCreateRecipe.getCategoryIDs() != null){
            selectedIDs = recCreateRecipe.getCategoryIDs();
        }

        context = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Ophalen categorieën...
        lvCategories = getListView();

        String[] projection = { CategoryTable.COLUMN_ID ,CategoryTable.COLUMN_NAME, CategoryTable.COLUMN_IMAGE };
        mCursor =  context.getContentResolver().query(
                ReceptenAppContentProvider.CONTENT_URI_CAT,
                projection,
                null,
                null,
                null);

        //adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_multiple_choice, mCursor,new String[]{"name"},new int[]{android.R.id.text1});
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                customAdapter = new MyCursorAdapter(
                        getActivity(),
                        mCursor,
                        0);

                lvCategories.setAdapter(customAdapter);
            }

        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = ((MyCursorAdapter)l.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        int catID = cursor.getInt(cursor.getColumnIndex(CategoryTable.COLUMN_ID));
        if(selectedIDs.contains(catID)){
            selectedIDs.remove(selectedIDs.indexOf(catID));
        } else {
            selectedIDs.add(catID);
        }
        recCreateRecipe.setCategoryIDs(selectedIDs);
        customAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_create_categories,container, false);

        Button btnPrevious = (Button) view.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateCategorySelectedListener(recCreateRecipe, "previous");
            }
        });
        Button btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onNextCreateCategorySelectedListener(recCreateRecipe, "next");
            }
        });

        return view;
    }

    public interface OnNextCreateCategorySelectedListener{
        public void onNextCreateCategorySelectedListener(Recept recept, String button);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (OnNextCreateCategorySelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextCreateCategorySelectedListener");
        }
    }

    public class MyCursorAdapter extends CursorAdapter {
        private LayoutInflater mInflater;

        // Default constructor
        public MyCursorAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            Context test = getActivity();
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void bindView(View view, Context context, Cursor cursor) {
            TextView content = (TextView) view.findViewById(R.id.txtCategoryCreate);
            content.setText(cursor.getString(cursor.getColumnIndex(CategoryTable.COLUMN_NAME)));

            String catImage = cursor.getString(cursor.getColumnIndex(CategoryTable.COLUMN_IMAGE));

            ImageView image = (ImageView) view.findViewById(R.id.ivCategoryImage);
            Bitmap bmp;

            //controleer of er een afbeelding werd geplaatst in db
            if(catImage.isEmpty()){
                //geen afbeelding opgegeven --> no_image weergeven
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noimage); //zit standaard in de app, dus hoeft niet als string worden geconverteerd
            }else{
                //afbeelding zit in database (als string)
                bmp = ImageConverter.StringToBitmap(catImage);
            }
            image.setImageBitmap(bmp);

            int thisCatID = cursor.getInt(cursor.getColumnIndex(CategoryTable.COLUMN_ID));
            if(selectedIDs.contains(thisCatID)){
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            } else {
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            }
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mInflater.inflate(R.layout.row_create_category, parent, false);
        }
    }
}
