package be.howest.nmct.receptenapp.data.BoodschappenData;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 17/01/2015.
 */
public class BoodschappenTable {
    // Database table
    public static final String TABLE_BOODSCHAPPEN = "boodschappen";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_UNITID = "unitid";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BOODSCHAPPEN + "("
            + COLUMN_ID + " integer, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_AMOUNT + " integer, "
            + COLUMN_UNITID + " integer"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(BoodschappenTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOODSCHAPPEN);
        onCreate(database);
    }
}
