package be.howest.nmct.receptenapp.data.AuthorData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.howest.nmct.receptenapp.data.BoodschappenData.BasketTable;


/**
 * Created by Mattias on 17/01/2015.
 */
public class AuthorDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "author.db";
    private static final int DATABASE_VERSION = 1;

    public AuthorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        AuthorTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        AuthorTable.onUpgrade(database, oldVersion, newVersion);
    }

}