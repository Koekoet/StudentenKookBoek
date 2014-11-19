package fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import be.howest.nmct.receptenapp.R;

/**
 * Created by Mattias on 17/11/2014.
 */
public class ReceptNavigationFragment extends ListFragment {
    private String[] arrNavigation;
    public NavigationAdapter navigationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        arrNavigation = getResources().getStringArray(R.array.MenuBasic);
        ShowNavigation();
    }

    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    OnNavigationSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnNavigationSelectedListener {
        public void onNavigationSelected(int position);
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
        mCallback.onNavigationSelected(position);
    }


    // Toon Navigation
    private void ShowNavigation()
    {

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

            return row;
        }

    }
}
