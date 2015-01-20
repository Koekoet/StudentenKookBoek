package be.howest.nmct.receptenapp.data.UnitData;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 17/01/2015.
 */
public class UnitTable {
    // Database table
    public static final String TABLE_UNIT = "ingredient";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ABBREVIATION = "abbreviation";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_UNIT + "("
            + COLUMN_ID + " integer, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_ABBREVIATION + " text not null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(UnitTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIT);
        onCreate(database);
    }
}
