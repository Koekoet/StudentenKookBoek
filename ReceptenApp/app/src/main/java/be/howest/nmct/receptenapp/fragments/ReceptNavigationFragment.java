package be.howest.nmct.receptenapp.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import be.howest.nmct.receptenapp.R;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptNavigationFragment extends ListFragment {
    private String[] arrNavigation;
    TypedArray navImg;
    public NavigationAdapter navigationAdapter;

    //temp
    private boolean isLogin = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ShowNavigation();
    }

    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    OnNavigationSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnNavigationSelectedListener {
        public void onNavigationSelected(int position, boolean isLogin);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnNavigationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onNavigationSelected(position, isLogin);
    }


    // Toon Navigation
    public void ShowNavigation()
    {
        if(isLogin){
            arrNavigation = getResources().getStringArray(R.array.MenuBasic);
            navImg = getResources().obtainTypedArray(R.array.MenuDrawableBasic);
            isLogin = false;
        } else {
            arrNavigation = getResources().getStringArray(R.array.MenuUser);
            navImg = getResources().obtainTypedArray(R.array.MenuDrawableUser);
            isLogin = true;
        }

        navigationAdapter = new NavigationAdapter();
        setListAdapter(navigationAdapter);


    }

    // NavigationAdapter
    class NavigationAdapter extends ArrayAdapter<String>
    {
        public NavigationAdapter()
        {
            super(getActivity(), R.layout.navigation_list_item, R.id.menuItem, arrNavigation);
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            String navigationItem = arrNavigation[position];

            TextView naam = (TextView) row.findViewById(R.id.menuItem);
            naam.setText(navigationItem);

            ImageView img = (ImageView) row.findViewById(R.id.navimg);
            img.setImageResource(navImg.getResourceId(position, -1));

            return row;
        }

    }
}
