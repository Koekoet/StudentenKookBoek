package be.howest.nmct.receptenapp.data.RecipesByCategory;

import android.content.ContentValues;
import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.helpers.onlineData;

/**
 * Created by tijs1 on 3-12-2014.
 */
public class RecipesByCategory {
    //region properties
    public int ID;
    public int CategoryId;
    public be.howest.nmct.receptenapp.data.CategoryData.Category Category;
    public ArrayList<Recept> Recipes;

    //endregion
    //region constructors
    public RecipesByCategory() {
    }

    public RecipesByCategory(int id, int categoryId, Category cat, ArrayList<Recept> recipes) {
        this.ID = id;
        this.CategoryId = categoryId;
        this.Category = cat;
        this.Recipes = recipes;
    }
    //endregion

    public static ArrayList<RecipesByCategory> getAllRecipesByCategory() {
        ArrayList<RecipesByCategory> list = new ArrayList<RecipesByCategory>();
        JSONArray recByCat = onlineData.selectAllData("ap_recipes_by_category");
        if (recByCat != null) {
            for (int i = 0; i < recByCat.length(); i++) {
                try {
                    JSONObject c = recByCat.getJSONObject(i);
                    int id = c.getInt("ID");
                    int catId = c.getInt("CategoryId");
                    Category cat = makeCategory(catId);
                    ArrayList<Recept> recs = makeRecipes(c.getString("RecipeIds"));
                    RecipesByCategory newRecByCat = new RecipesByCategory(id, catId, cat, recs);
                    list.add(newRecByCat);
                } catch (Exception e) {
                }
            }
        } else {
            return null;
        }
        return list;
    }
    public static RecipesByCategory getRecipeByCatId(int catId) {
        RecipesByCategory recByCat = new RecipesByCategory();
        JSONArray recipesByCategory = onlineData.selectRecipesByCatId("ap_recipes_by_category", catId);
        if (recipesByCategory != null && recipesByCategory.length() == 1) {
            try {
                JSONObject c = recipesByCategory.getJSONObject(0);
                int id = c.getInt("ID");
                int _catId = c.getInt("CategoryId");
                Category cat = makeCategory(catId);
                ArrayList<Recept> recs = makeRecipes(c.getString("RecipeIds"));
                RecipesByCategory newRecByCat = new RecipesByCategory(id, _catId, cat, recs);
                recByCat = newRecByCat;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return recByCat;
    }
    private static Category makeCategory(int catId) {
        Category cat = be.howest.nmct.receptenapp.data.CategoryData.Category.getCategoryById(catId);
        return cat;
    }
    private static ArrayList<Recept> makeRecipes(String recipeIds) {
        ArrayList<Recept> lijst = new ArrayList<Recept>();
        String[] sDelen = recipeIds.split(";");
        for(int i = 0; i < sDelen.length; i++){
            Recept rec = Recept.getRecipeById(Integer.parseInt(sDelen[i]));
            lijst.add(rec);
        }
        return lijst;
    }

    public static void createRecipesByCategory(int _categoryId, String _listOfRecipeIds){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recipes_by_category"));
        params.add(new BasicNameValuePair("CategoryId", ""+_categoryId));
        params.add(new BasicNameValuePair("RecipeIds", _listOfRecipeIds));
        onlineData.create(params);
    }
    public static void deleteRecipesByCategory(int _id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recipes_by_category"));
        params.add(new BasicNameValuePair("id", ""+_id));
        onlineData.delete(params);
    }

    //CURSOR FUNCTION
    public static Boolean getAllRecipesByCategoryCURSOR(Context context){
        context.getContentResolver().delete(ReceptenAppContentProvider.CONTENT_URI_RECBYCAT,null,null);

        JSONArray recByCat = onlineData.selectAllData("ap_recipes_by_category");
        if (recByCat != null) {
            for (int i = 0; i < recByCat.length(); i++) {
                try {
                    JSONObject c = recByCat.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(RecipesByCategoryTable.COLUMN_CATID, c.getInt("CategoryId"));
                    values.put(RecipesByCategoryTable.COLUMN_RECIDS, c.getString("RecipeIds"));
                    context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_RECBYCAT, values);
                } catch (Exception e) {
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
