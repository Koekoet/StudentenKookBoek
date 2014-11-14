package be.howest.nmct.receptenapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import data.Recept;
import fragments.ReceptBereidingFragment;
import fragments.ReceptInfoFragment;
import fragments.ReceptIngredientenFragment;


public class ReceptDetailActivity extends FragmentActivity implements
        ReceptInfoFragment.onReceptInfoSelectedListener,
        ReceptIngredientenFragment.onReceptIngredientSelectedListener,
        ReceptBereidingFragment.onReceptBereidingSelectedListener {

    public static Recept selectedRecipe = null;
    List<Fragment> tabFragmentList = new ArrayList();
    android.support.v4.app.Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recept_detail);

        if(savedInstanceState != null){
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        //We krijgen een Recept binnen...
        Intent intent = getIntent();
        selectedRecipe = (Recept) intent.getParcelableExtra("selectedRecipe");

        ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = actionbar.newTab().setText("Info").setTabListener(new MyTabListener<ReceptInfoFragment>(this, "Info", ReceptInfoFragment.class));
        ActionBar.Tab tab2 = actionbar.newTab().setText("Ingrediënten").setTabListener(new MyTabListener<ReceptIngredientenFragment>(this, "Ingrediënten", ReceptIngredientenFragment.class));
        ActionBar.Tab tab3 = actionbar.newTab().setText("Bereiding").setTabListener(new MyTabListener<ReceptBereidingFragment>(this, "Bereiding", ReceptBereidingFragment.class));

        actionbar.addTab(tab1);
        actionbar.addTab(tab2);
        actionbar.addTab(tab3);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mContent != null){
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recept_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceptInfoSelected(String tekst) {
        ReceptInfoFragment fragment = (ReceptInfoFragment) getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onReceptIngredientSelectedListener(String tekst) {
        ReceptIngredientenFragment fragment = (ReceptIngredientenFragment) getFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onReceptBereidingSelected(String tekst) {
        ReceptBereidingFragment fragment = (ReceptBereidingFragment) getFragmentManager().findFragmentById(R.id.fragment);
    }

    class MyTabListener<T extends Fragment> implements ActionBar.TabListener{
        private Fragment mFragment;
        private final Class<T> mClass;
        private final Activity mActivity;
        private final String mTag;


        public MyTabListener(Activity activity, String tag, Class<T> clz){
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            Fragment tf = null;
            if(tabFragmentList.size() > tab.getPosition())
                mFragment = tabFragmentList.get(tab.getPosition());

            if(mFragment == null){

                Bundle bundle = new Bundle();
                if(tab.getPosition()==0){
                    //Info Fragment
                    tf = new ReceptInfoFragment();
                } else if(tab.getPosition()==1){
                    //Ingredienten Fragment
                    tf=new ReceptIngredientenFragment();
                } else if(tab.getPosition()==2){
                    //Bereiding fragment
                    tf = new ReceptBereidingFragment();
                }

                bundle.putParcelable("MYSELECTEDRECIPE", selectedRecipe);
                //bundle.putParcelableArrayList("SELECTEDRECIPEINGREDIENTS", selectedRecipe.getIngredients());
                tf.setArguments(bundle);
                tabFragmentList.add(tf);
            } else{
                tf = mFragment;
            }
            fragmentTransaction.replace(android.R.id.content, tf);

        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if(tabFragmentList.size() > tab.getPosition()){
                fragmentTransaction.remove(tabFragmentList.get(tab.getPosition()));
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }
}


