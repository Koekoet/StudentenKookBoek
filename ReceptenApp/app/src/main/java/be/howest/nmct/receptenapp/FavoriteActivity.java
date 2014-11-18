package be.howest.nmct.receptenapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import data.Recept;
import data.helpers.SwipeDismissListViewTouchListener;


public class FavoriteActivity extends ListActivity {
    private ArrayList<Recept> favorietenLijst;
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //Hier lezen we da favorieten in
        favorietenLijst = new ArrayList<Recept>();
        favorietenLijst.add(new Recept());
        favorietenLijst.add(new Recept());
        favorietenLijst.add(new Recept());
        favorietenLijst.add(new Recept());

        mAdapter = new FavoriteAdapter();
        setListAdapter(mAdapter);


        ListView listView = getListView();
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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_delete:
                DeleteFavoritesList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddToFavorites(Recept recept){
        favorietenLijst.add(recept);
        mAdapter.NotifyAdapter();
    }

    private void DeleteFavoritesList() {
        //ShowDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
        builder.setTitle("Favorieten leegmaken");
        builder.setMessage("Bent u zeker dat u alle opgeslagen recepten wilt verwijderen?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.clear();
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class FavoriteAdapter extends ArrayAdapter<Recept> {
        public FavoriteAdapter() {
            super(FavoriteActivity.this,R.layout.row_favorites, R.id.favoriteRecipeName, favorietenLijst);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Recept recept = favorietenLijst.get(position);
            View row = super.getView(position, convertView, parent);

            ImageView imageView = (ImageView) row.findViewById(R.id.favoriteLike);

            TextView textView = (TextView) row.findViewById(R.id.favoriteRecipeName);
            textView.setText(recept.getName());

            return row;
        }

        public void clearAdapter(){
            favorietenLijst.clear();
            notifyDataSetChanged();
        }
        public void NotifyAdapter(){
            notifyDataSetChanged();
        }
    }
}
