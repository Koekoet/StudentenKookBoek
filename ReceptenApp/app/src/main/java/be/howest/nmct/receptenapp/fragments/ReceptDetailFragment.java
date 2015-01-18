package be.howest.nmct.receptenapp.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import be.howest.nmct.receptenapp.R;
import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteTable;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;

/**
 * Created by Toine on 19/11/2014.
 */
public class ReceptDetailFragment extends Fragment{
    Context context = getActivity();
    private FragmentTabHost mTabHost;
    public static Recept selectedRecipe = null;
    private Cursor mCursor;
    private Uri uri;
    String ReceptID;
    Boolean isFav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();

        Bundle bundle = this.getArguments();
        uri = bundle.getParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC);

        mCursor = context.getContentResolver().query(uri, null, null, null, null);
        mCursor.moveToFirst();
        ReceptID = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_detail,container,false);

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_recept_detail);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ReceptenAppContentProvider.CONTENT_ITEM_REC, uri);

        mTabHost.addTab(mTabHost.newTabSpec("receptInfo").setIndicator("Info"),ReceptInfoFragment.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("receptIngredienten").setIndicator("Ingredienten"),ReceptIngredientenFragment.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("receptBereiding").setIndicator("Bereiding"),ReceptBereidingFragment.class, bundle);
        mCursor.moveToFirst();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null){
            menu.findItem(R.id.menu_item_search).setVisible(false);
        }
        inflater.inflate(R.menu.recept_detail, menu);
        mCursor.moveToFirst();
        String id = mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_ID));
        Uri favUri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_FAV + "/" + id);
        Cursor fav = context.getContentResolver().query(favUri, null, null, null, null);

        if(fav.getCount() == 0){
           menu.findItem(R.id.menu_item_favorite).setIcon(R.drawable.ic_action_favorite);
            isFav = false;
        } else {
            menu.findItem(R.id.menu_item_favorite).setIcon(R.drawable.ic_action_isfavorite);
            isFav = true;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_item_favorite:
                if(isFav){
                    //delete
                    isFav = false;
                    Uri uri = Uri.parse(ReceptenAppContentProvider.CONTENT_URI_FAV + "/" + ReceptID);
                    context.getContentResolver().delete(uri, null, null);
                    item.setIcon(R.drawable.ic_action_favorite);
                } else {
                    isFav = true;
                    ContentValues values = new ContentValues();
                    values.put(FavoriteTable.COLUMN_ID,ReceptID);
                    context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_FAV, values);
                    item.setIcon(R.drawable.ic_action_isfavorite);
                }

                return true;
            case R.id.menu_item_share:
                ShareIntent();
                return true;
            case R.id.menu_item_rate:
                View ratingView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rating, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(ratingView);
                builder.setTitle(mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NAME)));
                final EditText review = (EditText) ratingView.findViewById(R.id.review);
                final RatingBar rating = (RatingBar) ratingView.findViewById(R.id.ratingBar);

                builder.setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //DOE IETS MET DE WAARDE
                                float selectedRating = rating.getRating();
                                String selectedReview = "" + review.getText();

                                //OPSLAAN VAN RATING.... TIJS (*ZUCHT*);

                            }
                        }).setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void ShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Ik vond een leuk receptje op de receptenapp! Bekijk het hier: www.tijs-dl.be");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mCursor.getString(mCursor.getColumnIndex(ReceptTable.COLUMN_NAME)));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
