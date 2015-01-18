package be.howest.nmct.receptenapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import be.howest.nmct.receptenapp.data.CategoryData.CategoryDatabaseHelper;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteDatabaseHelper;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteTable;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptDatabaseHelper;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategoryDatabaseHelper;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategoryTable;


/**
 * Created by Mattias on 16/01/2015.
 */
public class ReceptenAppContentProvider extends ContentProvider {

    // databases
    private CategoryDatabaseHelper CatDatabase;
    private ReceptDatabaseHelper RecDatabase;
    private RecipesByCategoryDatabaseHelper RecByCatDatabase;
    private FavoriteDatabaseHelper FavDatabase;

    // used for the UriMacher
    //Category
    private static final int CATEGORYS = 10;
    private static final int CATEGORY_ID = 11;
    //Recept
    private static final int RECEPTS = 20;
    private static final int RECEPTS_ID = 21;
    private static final int RECEPTS_BY_CAT_ID = 22;
    //ReceptByCategoryID
    private static final int RECBYCAT = 30;
    //Favorite
    private static final int FAVORITE = 40;
    private static final int FAVORITE_ID = 41;
    //Auth
    private static final String AUTHORITY = "be.howest.nmct.receptenapp.contentprovider";

    //CATEGORY CONNECTION
    private static final String BASE_PATH_CAT = "category";
    public static final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CAT);
    public static final String CONTENT_CAT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categorys";
    public static final String CONTENT_ITEM_CAT = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/category";

    //CATEGORY CONNECTION
    private static final String BASE_PATH_REC = "recept";
    public static final Uri CONTENT_URI_REC = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_REC);
    public static final String CONTENT_REC = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recipes";
    public static final String CONTENT_ITEM_REC = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/recept";

    //RECIPES BY CATEGORY
    private static final String BASE_PATH_RECBYCAT = "recipebycategory";
    public static final Uri CONTENT_URI_RECBYCAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_RECBYCAT);
    public static final String CONTENT_RECBYCAT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recipebycategorys";
    public static final String CONTENT_ITEM_RECBYCAT = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/recipebycategory";

    //FAVORITE
    private static final String BASE_PATH_FAV = "favorite";
    public static final Uri CONTENT_URI_FAV = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_FAV);
    public static final String CONTENT_FAV = ContentResolver.CURSOR_DIR_BASE_TYPE + "/favorites";
    public static final String CONTENT_ITEM_FAV = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/favorite";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //CAT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CAT, CATEGORYS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CAT + "/#", CATEGORY_ID);

        //RECEPT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC, RECEPTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC + "/#", RECEPTS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC + "/RecByCatId/#", RECEPTS_BY_CAT_ID);

        //RECIPE BY CAT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_RECBYCAT, RECBYCAT);

        //FAVORITE
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV, FAVORITE);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV + "/#", FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        CatDatabase = new CategoryDatabaseHelper(getContext());
        RecDatabase = new ReceptDatabaseHelper(getContext());
        RecByCatDatabase = new RecipesByCategoryDatabaseHelper(getContext());
        FavDatabase = new FavoriteDatabaseHelper(getContext());
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)  {
        //Preperation
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        SQLiteDatabase db =  null;
        //0 -> Cat, 1 -> Rec, 2-> RECBYCAT, 3->FAV
        int selectedDatabase = 0;

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            //Category
            case CATEGORYS:
                queryBuilder.setTables(CategoryTable.TABLE_CATEGORY);
                break;
            case CATEGORY_ID:
                queryBuilder.setTables(CategoryTable.TABLE_CATEGORY);
                queryBuilder.appendWhere(CategoryTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //Recepts
            case RECEPTS:
                selectedDatabase = 1;
                queryBuilder.setTables(ReceptTable.TABLE_RECEPI);
                break;
            case RECEPTS_ID:
                selectedDatabase = 1;
                queryBuilder.setTables(ReceptTable.TABLE_RECEPI);
                queryBuilder.appendWhere(ReceptTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case RECEPTS_BY_CAT_ID:
                return LoadRecipesByCategory(uri);
            //RECEPTS BY CAT
            case RECBYCAT:
                //GET ALL REC BY CAT
                selectedDatabase = 2;
                queryBuilder.setTables(RecipesByCategoryTable.TABLE_RECEPIBYCATEGORY);
                break;
            case FAVORITE:
                return LoadFavorites(uri);
            case FAVORITE_ID:
                selectedDatabase = 3;
                queryBuilder.setTables(FavoriteTable.TABLE_FAVORITE);
                queryBuilder.appendWhere(FavoriteTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(selectedDatabase == 0){ db = CatDatabase.getWritableDatabase();}
        else if(selectedDatabase == 1){ db = RecDatabase.getWritableDatabase();}
        else if (selectedDatabase == 2) {db = RecByCatDatabase.getWritableDatabase();}
        else if(selectedDatabase == 3) {db = FavDatabase.getWritableDatabase();}

        Cursor cursor = queryBuilder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    private Cursor LoadFavorites(Uri uri) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FavoriteTable.TABLE_FAVORITE);
        String[] projection = { FavoriteTable.COLUMN_ID };
        SQLiteDatabase db = FavDatabase.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, null,null, null, null, null);

        MergeCursor recipes = null;
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            if(cursor.getCount() == 1){
                return getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
            } else if(cursor.getCount() == 2){
                Cursor rec1 = getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
                cursor.moveToNext();
                Cursor rec2 = getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
                recipes = new MergeCursor(new Cursor[] {rec1, rec2});
            } else {
                Cursor rec1 = getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
                cursor.moveToNext();
                Cursor rec2 = getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
                recipes = new MergeCursor(new Cursor[] {rec1, rec2});
                while (cursor.moveToNext()) {
                    Cursor recipe = getRecById(cursor.getString(cursor.getColumnIndex(FavoriteTable.COLUMN_ID)));
                    recipes =  new MergeCursor(new Cursor[] { recipes, recipe });
                }
            }
        } else {return null;}
        recipes.setNotificationUri(getContext().getContentResolver(), uri);
        int count = recipes.getCount();
        return recipes;
    }

    private Cursor LoadRecipesByCategory(Uri uri){
        //Eerst ophalen Rec IDs
        String[] RecByCatIDs = getRecByCatId(uri.getLastPathSegment());
        MergeCursor recipes = null;
        //methode getReceptById en toevoegen aan cursor
        if(RecByCatIDs == null){ return null;}
        else if(RecByCatIDs.length == 1){
            return getRecById(RecByCatIDs[0]);
        } else if(RecByCatIDs.length == 2){
            Cursor rec1 = getRecById(RecByCatIDs[0]);
            Cursor rec2 = getRecById(RecByCatIDs[1]);
            recipes = new MergeCursor(new Cursor[] {rec1, rec2});
            int recCount = recipes.getCount();
            int rec1Count = rec1.getCount();
            int rec2Co = rec2.getCount();
        } else {
            Cursor rec1 = getRecById(RecByCatIDs[0]);
            Cursor rec2 = getRecById(RecByCatIDs[1]);
            recipes = new MergeCursor(new Cursor[] {rec1, rec2});

            for (int i = 2; i < RecByCatIDs.length; i++) {
                if(RecByCatIDs[i] != ""){
                    Cursor recipe = getRecById(RecByCatIDs[i]);
                    recipes =  new MergeCursor(new Cursor[] { recipes, recipe });
                }
            }

        }
        recipes.setNotificationUri(getContext().getContentResolver(), uri);
        return recipes;
    }
    private String[] getRecByCatId(String id){
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(RecipesByCategoryTable.TABLE_RECEPIBYCATEGORY);
        String[] projection = { RecipesByCategoryTable.COLUMN_CATID ,RecipesByCategoryTable.COLUMN_RECIDS };
        SQLiteDatabase db = RecByCatDatabase.getWritableDatabase();
        queryBuilder.appendWhere(RecipesByCategoryTable.COLUMN_CATID + "=" + id);
        Cursor cursor = queryBuilder.query(db, projection, null,null, null, null, null);

        cursor.moveToFirst();
        //SPlit van 2de locatie cursor.
        if(cursor.getCount() != 0){
            String recIDs = cursor.getString(cursor.getColumnIndex(RecipesByCategoryTable.COLUMN_RECIDS));
            if(recIDs != ""){
                String[] RecByCatIDs = recIDs.split(";");
                return RecByCatIDs;
            }
        }
        return null;

    }
    private Cursor  getRecById(String id){
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String[] projection = ReceptTable.getAllCollums();
        queryBuilder.setTables(ReceptTable.TABLE_RECEPI);
        queryBuilder.appendWhere(ReceptTable.COLUMN_ID + "=" + id);
        SQLiteDatabase db = RecDatabase.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, null,null, null, null, null);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB;
        long id = 0;
        switch (uriType) {
            case CATEGORYS:
                sqlDB = CatDatabase.getWritableDatabase();
                sqlDB.insert(CategoryTable.TABLE_CATEGORY, null, values);
                break;
            case RECEPTS:
                sqlDB = RecDatabase.getWritableDatabase();
                sqlDB.insert(ReceptTable.TABLE_RECEPI, null, values);
                break;
            case RECBYCAT:
                sqlDB = RecByCatDatabase.getWritableDatabase();
                sqlDB.insert(RecipesByCategoryTable.TABLE_RECEPIBYCATEGORY,null,values);
                break;
            case FAVORITE:
                sqlDB = FavDatabase.getWritableDatabase();
                sqlDB.insert(FavoriteTable.TABLE_FAVORITE,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_CAT + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB;
        int rowsDeleted = 0;
        String id = "-1";
        switch (uriType) {
            case CATEGORYS:
                sqlDB = CatDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(CategoryTable.TABLE_CATEGORY, selection,
                        selectionArgs);
                break;
            case CATEGORY_ID:
                sqlDB = CatDatabase.getWritableDatabase();
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(CategoryTable.TABLE_CATEGORY,
                            CategoryTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(CategoryTable.TABLE_CATEGORY,
                            CategoryTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case FAVORITE:
                sqlDB = FavDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(FavoriteTable.TABLE_FAVORITE,null,null);
                break;
            case FAVORITE_ID:
                sqlDB = FavDatabase.getWritableDatabase();
                id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(FavoriteTable.TABLE_FAVORITE, FavoriteTable.COLUMN_ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = CatDatabase.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case CATEGORYS:
                rowsUpdated = sqlDB.update(CategoryTable.TABLE_CATEGORY,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CATEGORY_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(CategoryTable.TABLE_CATEGORY,
                            values,
                            CategoryTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(CategoryTable.TABLE_CATEGORY,
                            values,
                            CategoryTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {CategoryTable.COLUMN_ID,
                CategoryTable.COLUMN_IMAGE, CategoryTable.COLUMN_NAME};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection: CODE ERROR");
            }
        }
    }
}
