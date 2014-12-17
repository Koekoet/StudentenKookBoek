package fragments;

<<<<<<< HEAD
=======
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
>>>>>>> a0a22b82b204e587c4d8e86119bf71663d749005
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
<<<<<<< HEAD
=======
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
>>>>>>> a0a22b82b204e587c4d8e86119bf71663d749005
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.receptenapp.R;
import data.Recept;

/**
 * Created by Toine on 19/11/2014.
 */
public class ReceptDetailFragment extends Fragment{

    private FragmentTabHost mTabHost;
    public static Recept selectedRecipe = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        selectedRecipe = args.getParcelable("MYSELECTEDRECIPE");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_detail,container,false);

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_recept_detail);

        Bundle bundle = new Bundle();
        bundle.putParcelable("MYSELECTEDRECIPE", selectedRecipe);

        mTabHost.addTab(mTabHost.newTabSpec("receptInfo").setIndicator("Info"),ReceptInfoFragment.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("receptIngredienten").setIndicator("Ingredienten"),ReceptIngredientenFragment.class, bundle);
        mTabHost.addTab(mTabHost.newTabSpec("receptBereiding").setIndicator("Bereiding"),ReceptBereidingFragment.class, bundle);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recept_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_item_share:
                ShareIntent();
                return true;
            case R.id.menu_item_rate:
                View ratingView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rating, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(ratingView);
                builder.setTitle(selectedRecipe.getName());
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
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, selectedRecipe.getName());
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
