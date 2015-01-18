package be.howest.nmct.receptenapp.data.RecipesByCategory;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Mattias on 30/12/2014.
 */
public class RecipesByCategoryTable {
    // Database table
    public static final String TABLE_RECEPIBYCATEGORY = "recepiByCategory";
    public static final String COLUMN_CATID = "_catId";
    public static final String COLUMN_RECIDS = "recIDs";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECEPIBYCATEGORY + "("
            + COLUMN_CATID + " integer, "
            + COLUMN_RECIDS + " text not null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(RecipesByCategoryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEPIBYCATEGORY);
        onCreate(database);
    }
}
