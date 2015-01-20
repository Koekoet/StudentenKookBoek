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

import be.howest.nmct.receptenapp.data.AuthorData.AuthorDatabaseHelper;
import be.howest.nmct.receptenapp.data.AuthorData.AuthorTable;
import be.howest.nmct.receptenapp.data.BoodschappenData.BasketDatabaseHelper;
import be.howest.nmct.receptenapp.data.BoodschappenData.BasketTable;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryDatabaseHelper;
import be.howest.nmct.receptenapp.data.CategoryData.CategoryTable;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteDatabaseHelper;
import be.howest.nmct.receptenapp.data.FavoriteData.FavoriteTable;
import be.howest.nmct.receptenapp.data.IngredientData.IngredientDatabaseHelper;
import be.howest.nmct.receptenapp.data.IngredientData.IngredientTable;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptDatabaseHelper;
import be.howest.nmct.receptenapp.data.ReceptData.ReceptTable;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategoryDatabaseHelper;
import be.howest.nmct.receptenapp.data.RecipesByCategory.RecipesByCategoryTable;
import be.howest.nmct.receptenapp.data.UnitData.UnitDatabaseHelper;
import be.howest.nmct.receptenapp.data.UnitData.UnitTable;


/**
 * Created by Mattias on 16/01/2015.
 */
public class ReceptenAppContentProvider extends ContentProvider {

    // databases
    private CategoryDatabaseHelper CatDatabase;
    private ReceptDatabaseHelper RecDatabase;
    private RecipesByCategoryDatabaseHelper RecByCatDatabase;
    private FavoriteDatabaseHelper FavDatabase;
    private IngredientDatabaseHelper IngrDatabase;
    private UnitDatabaseHelper UnitDatabase;
    private BasketDatabaseHelper BasketDatabase;
    private AuthorDatabaseHelper AuthorDatabase;

    // used for the UriMacher
    //Category
    private static final int CATEGORYS = 10;
    private static final int CATEGORY_ID = 11;
    //Recept
    private static final int RECEPTS = 20;
    private static final int RECEPTS_ID = 21;
    private static final int RECEPTS_BY_CAT_ID = 22;
    private static final int RECEPTS_BY_QUERY = 23;
    //ReceptByCategoryID
    private static final int RECBYCAT = 30;
    //Favorite
    private static final int FAVORITE = 40;
    private static final int FAVORITE_ID = 41;
    //Ingredient
    private static final int INGREDIENT = 50;
    private static final int INGREDIENT_ID = 51;
    //Unit
    private static final int UNIT = 60;
    private static final int UNIT_ID = 61;
    //Boodschappen
    private static final int BASKET = 70;
    private static final int BASKET_ID = 71;
    //Author
    private static final int AUTHOR = 80;
    private static final int AUTHOR_ID = 81;


    //Auth
    private static final String AUTHORITY = "be.howest.nmct.receptenapp.contentprovider";



    @Override
    public boolean onCreate() {
        CatDatabase = new CategoryDatabaseHelper(getContext());
        RecDatabase = new ReceptDatabaseHelper(getContext());
        RecByCatDatabase = new RecipesByCategoryDatabaseHelper(getContext());
        FavDatabase = new FavoriteDatabaseHelper(getContext());
        IngrDatabase = new IngredientDatabaseHelper(getContext());
        UnitDatabase = new UnitDatabaseHelper(getContext());
        BasketDatabase = new BasketDatabaseHelper(getContext());
        AuthorDatabase = new AuthorDatabaseHelper(getContext());
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)  {
        //Preperation
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //checkColumns(projection);
        SQLiteDatabase db =  null;
        //0 -> Cat, 1 -> Rec, 2-> RECBYCAT, 3->FAV, 4->ING, 5->Unit, 6-> Basket, 7->Author
        int selectedDatabase = 0;

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            //-------------------CATEGORY------------//
            case CATEGORYS:
                queryBuilder.setTables(CategoryTable.TABLE_CATEGORY);
                break;
            case CATEGORY_ID:
                queryBuilder.setTables(CategoryTable.TABLE_CATEGORY);
                queryBuilder.appendWhere(CategoryTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //-------------------RECEPTS------------//
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
            case RECEPTS_BY_QUERY:

                String[] args = new String[1];
                args[0] = "%"+ uri.getLastPathSegment() +"%";
                db = RecDatabase.getReadableDatabase();
                return db.rawQuery("SELECT * FROM " + ReceptTable.TABLE_RECEPI + " WHERE " + ReceptTable.COLUMN_NAME + " like ?", args);

            //-------------------RECEPTS BY CATEGORY------------//
            case RECBYCAT:
                //GET ALL REC BY CAT
                selectedDatabase = 2;
                queryBuilder.setTables(RecipesByCategoryTable.TABLE_RECEPIBYCATEGORY);
                break;

            //-------------------FAVORITE---------------//
            case FAVORITE:
                return LoadFavorites(uri);
            case FAVORITE_ID:
                selectedDatabase = 3;
                queryBuilder.setTables(FavoriteTable.TABLE_FAVORITE);
                queryBuilder.appendWhere(FavoriteTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //-------------------INGREDIENT---------------//
            case INGREDIENT:
                selectedDatabase = 4;
                queryBuilder.setTables(IngredientTable.TABLE_INGREDIENT);
                break;
            case INGREDIENT_ID:
                selectedDatabase = 4;
                queryBuilder.setTables(IngredientTable.TABLE_INGREDIENT);
                queryBuilder.appendWhere(IngredientTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //-------------------UNIT---------------------//
            case UNIT:
                selectedDatabase = 5;
                queryBuilder.setTables(UnitTable.TABLE_UNIT);
                break;
            case UNIT_ID:
                selectedDatabase = 5;
                queryBuilder.setTables(UnitTable.TABLE_UNIT);
                queryBuilder.appendWhere(UnitTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //-------------------BASKET---------------------//
            case BASKET:
                selectedDatabase = 6;
                queryBuilder.setTables(BasketTable.TABLE_BASKET);
                break;
            case BASKET_ID:
                selectedDatabase = 6;
                queryBuilder.setTables(BasketTable.TABLE_BASKET);
                queryBuilder.appendWhere(BasketTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            //-------------------AUTHOR---------------------//
            case AUTHOR:
                selectedDatabase = 7;
                queryBuilder.setTables(AuthorTable.TABLE_AUTHOR);
                break;
            case AUTHOR_ID:
                selectedDatabase = 7;
                queryBuilder.setTables(AuthorTable.TABLE_AUTHOR);
                queryBuilder.appendWhere(AuthorTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(selectedDatabase == 0){ db = CatDatabase.getWritableDatabase();}
        else if(selectedDatabase == 1){ db = RecDatabase.getWritableDatabase();}
        else if (selectedDatabase == 2) {db = RecByCatDatabase.getWritableDatabase();}
        else if(selectedDatabase == 3) {db = FavDatabase.getWritableDatabase();}
        else if(selectedDatabase == 4){db = IngrDatabase.getWritableDatabase();}
        else if(selectedDatabase == 5){db = UnitDatabase.getWritableDatabase();}
        else if(selectedDatabase == 6) {db = BasketDatabase.getWritableDatabase();}
        else if(selectedDatabase == 7) {db = AuthorDatabase.getWritableDatabase();}

        Cursor cursor = queryBuilder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

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
            case INGREDIENT:
                sqlDB = IngrDatabase.getWritableDatabase();
                sqlDB.insert(IngredientTable.TABLE_INGREDIENT,null,values);
                break;
            case UNIT:
                sqlDB = UnitDatabase.getWritableDatabase();
                sqlDB.insert(UnitTable.TABLE_UNIT,null,values);
                break;
            case BASKET:
                sqlDB = BasketDatabase.getWritableDatabase();
                sqlDB.insert(BasketTable.TABLE_BASKET, null,values);
                break;
            case AUTHOR:
                sqlDB = AuthorDatabase.getWritableDatabase();
                sqlDB.insert(AuthorTable.TABLE_AUTHOR, null,values);
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
            case RECEPTS:
                sqlDB = RecDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(ReceptTable.TABLE_RECEPI,null,null);
                break;
            case RECBYCAT:
                sqlDB = RecByCatDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(RecipesByCategoryTable.TABLE_RECEPIBYCATEGORY,null,null);
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
            case INGREDIENT:
                sqlDB = IngrDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(IngredientTable.TABLE_INGREDIENT,null,null);
                break;
            case UNIT:
                sqlDB = UnitDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(UnitTable.TABLE_UNIT,null,null);
                break;
            case BASKET:
                sqlDB = BasketDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(BasketTable.TABLE_BASKET,null,null);
                break;
            case BASKET_ID:
                sqlDB = BasketDatabase.getWritableDatabase();
                id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(BasketTable.TABLE_BASKET, BasketTable.COLUMN_ID + "=" + id, null);
                break;
            case AUTHOR:
                sqlDB = AuthorDatabase.getWritableDatabase();
                rowsDeleted = sqlDB.delete(AuthorTable.TABLE_AUTHOR, null,null);
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
            case INGREDIENT_ID:
                String ingrID = uri.getLastPathSegment();
                sqlDB = IngrDatabase.getWritableDatabase();
                rowsUpdated = sqlDB.update(IngredientTable.TABLE_INGREDIENT,values,IngredientTable.COLUMN_ID + "=" + ingrID,null);
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

    //------------------CATEGORIES METHODES------------//
    //-----CATEGORY CONNECTION-------------------------//
    private static final String BASE_PATH_CAT = "category";
    public static final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CAT);
    public static final String CONTENT_CAT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/categorys";
    public static final String CONTENT_ITEM_CAT = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/category";
    //-----QUERY---------------------------------------//

    //------------------RECEPT METHODES----------------//
    //-----RECEPTS CONNECTION--------------------------//
    private static final String BASE_PATH_REC = "recept";
    public static final Uri CONTENT_URI_REC = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_REC);
    public static final String CONTENT_REC = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recipes";
    public static final String CONTENT_ITEM_REC = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/recept";
    //-----QUERY---------------------------------------//
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

    //------------------RECIPES BY CATEGORY METHODES---//
    //-----RECIPES BY CATEGORY CONNECTION--------------//
    private static final String BASE_PATH_RECBYCAT = "recipebycategory";
    public static final Uri CONTENT_URI_RECBYCAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_RECBYCAT);
    public static final String CONTENT_RECBYCAT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/recipebycategorys";
    public static final String CONTENT_ITEM_RECBYCAT = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/recipebycategory";
    //-----QUERY---------------------------------------//

    //-------------------FAVORITES METHODES------------//
    //-----FAVORITE CONNECTION-------------------------//
    private static final String BASE_PATH_FAV = "favorite";
    public static final Uri CONTENT_URI_FAV = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_FAV);
    public static final String CONTENT_FAV = ContentResolver.CURSOR_DIR_BASE_TYPE + "/favorites";
    public static final String CONTENT_ITEM_FAV = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/favorite";
    //-----QUERY---------------------------------------//
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

    //-------------------INGREDIENTS METHODES----------//
    //-----INGREDIENT CONNECTION-----------------------//
    private static final String BASE_PATH_INGR = "ingredient";
    public static final Uri CONTENT_URI_INGR = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_INGR);

    //-------------------UNITS METHODES----------------//
    //-----UNITS CONNECTION----------------------------//
    private static final String BASE_PATH_UNIT = "unit";
    public static final Uri CONTENT_URI_UNIT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_UNIT);

    //-------------------BASKET METHODES---------------//
    //-----BASKET CONNECTION---------------------------//
    private static final String BASE_PATH_BASKET = "basket";
    public static final Uri CONTENT_URI_BASKET = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_BASKET);

    //-------------------AUTHOR METHODES---------------//
    //-----AUTHOR CONNECTION---------------------------//
    private static final String BASE_PATH_AUTHOR = "author";
    public static final Uri CONTENT_URI_AUTHOR = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_AUTHOR);

    //-------------------URIMATCHER--------------------//
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        //CAT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CAT, CATEGORYS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CAT + "/#", CATEGORY_ID);

        //RECEPT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC, RECEPTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC + "/#", RECEPTS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC + "/RecByCatId/#", RECEPTS_BY_CAT_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_REC + "/RecByQUERY/*", RECEPTS_BY_QUERY);

        //RECIPE BY CAT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_RECBYCAT, RECBYCAT);

        //FAVORITE
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV, FAVORITE);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_FAV + "/#", FAVORITE_ID);

        //INGREDIENT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_INGR, INGREDIENT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_INGR + "/#", INGREDIENT_ID);

        //UNIT
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_UNIT, UNIT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_UNIT + "/#", UNIT_ID);


        //BASKET
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_BASKET, BASKET);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_BASKET + "/#", BASKET_ID);

        //AUTHOR
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_AUTHOR, AUTHOR);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_AUTHOR + "/#", AUTHOR_ID);

    }
}
