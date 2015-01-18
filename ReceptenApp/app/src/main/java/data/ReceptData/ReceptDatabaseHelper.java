package data.ReceptData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mattias on 16/01/2015.
 */
public class ReceptDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recepttable.db";
    private static final int DATABASE_VERSION = 1;

    public ReceptDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ReceptTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ReceptTable.onUpgrade(database, oldVersion, newVersion);
    }

}