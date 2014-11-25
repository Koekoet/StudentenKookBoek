package fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;

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
    List<Fragment> tabFragmentList = new ArrayList();
    android.support.v4.app.Fragment mContent;
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selectedRecipe = args.getParcelable("MYSELECTEDRECIPE");

        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        mShareIntent.putExtra(Intent.EXTRA_TEXT, "Ik vond een leuk receptje genaamd: " + selectedRecipe.getName() + "!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recept_detail,container,false);

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_recept_detail);
        mTabHost.addTab(mTabHost.newTabSpec("receptInfo").setIndicator("Info"),ReceptInfoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("receptIngredienten").setIndicator("Ingredienten"),ReceptIngredientenFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("receptBereiding").setIndicator("Bereiding"),ReceptBereidingFragment.class, null);

        return view;
    }
}
