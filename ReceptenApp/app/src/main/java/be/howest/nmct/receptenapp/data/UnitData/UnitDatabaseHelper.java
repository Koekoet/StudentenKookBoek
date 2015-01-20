package be.howest.nmct.receptenapp.data.UnitData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.howest.nmct.receptenapp.data.IngredientData.IngredientTable;


/**
 * Created by Mattias on 17/01/2015.
 */
public class UnitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "unit.db";
    private static final int DATABASE_VERSION = 1;

    public UnitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        UnitTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        UnitTable.onUpgrade(database, oldVersion, newVersion);
    }

}