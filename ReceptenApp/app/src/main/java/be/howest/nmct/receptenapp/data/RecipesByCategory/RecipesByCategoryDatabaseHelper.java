package be.howest.nmct.receptenapp.data.RecipesByCategory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mattias on 16/01/2015.
 */
public class RecipesByCategoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipesbycategorytable.db";
    private static final int DATABASE_VERSION = 1;

    public RecipesByCategoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        RecipesByCategoryTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        RecipesByCategoryTable.onUpgrade(database, oldVersion, newVersion);
    }

}