package data.FavoriteData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mattias on 17/01/2015.
 */
public class FavoriteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritetable.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        FavoriteTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        FavoriteTable.onUpgrade(database, oldVersion, newVersion);
    }

}