package data.ReceptData;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 30/12/2014.
 */
public class ReceptTable {
    // Database table
    public static final String TABLE_RECEPI = "recepi";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AUTHORID = "authorid";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_NUMBEROFPERSONS = "numberofpersons";
    public static final String COLUMN_DIFFICULTYID = "difficultyid";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPETEXT = "recepetext";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECEPI + "("
            + COLUMN_ID + " integer, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_AUTHORID + " integer,"
            + COLUMN_DURATION + " text not null,"
            + COLUMN_COST + " text not null,"
            + COLUMN_NUMBEROFPERSONS + " integer,"
            + COLUMN_DIFFICULTYID + " integer,"
            + COLUMN_PICTURE + " text not null,"
            + COLUMN_INGREDIENTS + " text not null,"
            + COLUMN_RECIPETEXT + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ReceptTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEPI);
        onCreate(database);
    }

    public static String[] getAllCollums() {
        String[] projection = {ReceptTable.COLUMN_ID,
                                ReceptTable.COLUMN_NAME,
                                ReceptTable.COLUMN_AUTHORID,
                                ReceptTable.COLUMN_DURATION,
                                ReceptTable.COLUMN_COST,
                                ReceptTable.COLUMN_NUMBEROFPERSONS,
                                ReceptTable.COLUMN_DIFFICULTYID,
                                ReceptTable.COLUMN_PICTURE,
                                ReceptTable.COLUMN_INGREDIENTS,
                                ReceptTable.COLUMN_RECIPETEXT};
        return projection;
    }
}
