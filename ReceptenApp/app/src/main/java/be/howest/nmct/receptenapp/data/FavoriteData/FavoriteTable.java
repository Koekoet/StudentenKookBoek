package be.howest.nmct.receptenapp.data.FavoriteData;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 17/01/2015.
 */
public class FavoriteTable {
    // Database table
    public static final String TABLE_FAVORITE = "favorite";
    public static final String COLUMN_ID = "_id";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITE + "("
            + COLUMN_ID + " integer"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(FavoriteTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(database);
    }
}
