package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.R;
import data.helpers.SwipeDismissListViewTouchListener;

/**
 * Created by Mattias on 9/12/2014.
 */
public class ReceptBoodschappenlijstjeFragment extends ListFragment {
    private ArrayList<String> arrBoodschappenlijstje;
    private BoodschappenlijstjeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Mijn boodschappenlijstje");

        //TODO
        arrBoodschappenlijstje = new ArrayList<String>();
        arrBoodschappenlijstje.add("2 eieren");
        arrBoodschappenlijstje.add("1 kg bananen");
        arrBoodschappenlijstje.add("2 appels");
        arrBoodschappenlijstje.add("1 kg suiker");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new BoodschappenlijstjeAdapter();
        setListAdapter(mAdapter);

        ListView listView = getListView();
        listView.setAdapter(mAdapter);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {return true; }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for(int position : reverseSortedPositions){
                            mAdapter.remove(mAdapter.getItem(position));
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String boodschapDialog = "";
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), arrBoodschappenlijstje.get(i), Toast.LENGTH_SHORT).show();
                boodschapDialog = arrBoodschappenlijstje.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(boodschapDialog);
                builder.setView(input);



// Set up the buttons

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boodschapDialog =  input.getText().toString();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                arrBoodschappenlijstje.set(i, boodschapDialog);
                mAdapter = new BoodschappenlijstjeAdapter();
                setListAdapter(mAdapter);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boodschappenlijstje, null, false);
    }

    public class BoodschappenlijstjeAdapter extends ArrayAdapter<String> {

        public BoodschappenlijstjeAdapter(){
            super(getActivity(), R.layout.row_boodschappenlijstje, R.id.txvBoodschap, arrBoodschappenlijstje);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String boodschap = arrBoodschappenlijstje.get(position);
            View row = super.getView(position, convertView, parent);

            TextView textView = (TextView) row.findViewById(R.id.txvBoodschap);
            textView.setText(boodschap);

            return row;
        }

        public void clearAdapter(){
            arrBoodschappenlijstje.clear();
            notifyDataSetChanged();
        }
        public void NotifyAdapter(){
            notifyDataSetChanged();
        }
    }


}
