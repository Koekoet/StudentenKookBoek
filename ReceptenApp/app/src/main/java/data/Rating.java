package data;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tijs1 on 9-12-2014.
 */
public class Rating {
    private int ID;
    private int AccountId;
    //private Account Account;
    private int RecipeId;
    private Recept Recipe;
    private double Rating;
    private String Review;

    public Rating() {
    }

    public Rating(int ID, int accountId, int recipeId, Recept recipe, double rating, String review) {
        this.ID = ID;
        this.AccountId = accountId;
        this.RecipeId = recipeId;
        this.Recipe = recipe;
        this.Rating = rating;
        this.Review = review;
    }

    public static ArrayList<Rating> getAllRatings() {
        ArrayList<Rating> list = new ArrayList<Rating>();
        JSONArray ratings = data.helpers.onlineData.selectAllData("ap_rating");
        if (ratings != null) {
            for (int i = 0; i < ratings.length(); i++) {
                try {
                    JSONObject c = ratings.getJSONObject(i);
                    int id = c.getInt("ID");
                    int accountId = c.getInt("AccountId");
                    int recipeId = c.getInt("RecipeId");
                    Recept recipe = Recept.getRecipeById(recipeId);
                    double rating = c.getDouble("Rating");
                    String review = c.getString("Review");
                    Rating r = new Rating(id,accountId,recipeId,recipe,rating,review);
                    list.add(r);
                } catch (Exception e) {
                }
            }
            return list;
        } else {
            return null;
        }
    }
    public static Rating getRatingById(int id) {
        Rating rat  = new Rating();
        JSONArray rating = data.helpers.onlineData.selectDataById("ap_rating", id);
        if (rating != null && rating.length() == 1) {
            try {
                JSONObject c = rating.getJSONObject(0);
                int _id = c.getInt("ID");
                int accountId = c.getInt("AccountId");
                int recipeId = c.getInt("RecipeId");
                Recept recipe = Recept.getRecipeById(recipeId);
                double ratingPoint = c.getDouble("Rating");
                String review = c.getString("Review");
                Rating r = new Rating(_id,accountId,recipeId,recipe,ratingPoint,review);
                rat = r;
                //return newUnit;
            } catch (Exception e) {
            }
            //return u;
        } else {
            return null;
        }
        return rat;
    }
    public static ArrayList<Rating> getRatingsByRecipeId(int _recipeId){
        ArrayList<Rating> list = new ArrayList<Rating>();
        JSONArray ratings = data.helpers.onlineData.selectRatingsByRecipeId("ap_rating", _recipeId);
        if (ratings != null) {
            for (int i = 0; i < ratings.length(); i++) {
                try {
                    JSONObject c = ratings.getJSONObject(i);
                    int id = c.getInt("ID");
                    int accountId = c.getInt("AccountId");
                    int recipeId = c.getInt("RecipeId");
                    Recept recipe = Recept.getRecipeById(recipeId);
                    double rating = c.getDouble("Rating");
                    String review = c.getString("Review");
                    Rating r = new Rating(id,accountId,recipeId,recipe,rating,review);
                    list.add(r);
                } catch (Exception e) {
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public static void createUnit(int _accountId, int _recipeId, double _rating, String _review) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_rating"));
        params.add(new BasicNameValuePair("AccountId", "" + _accountId));
        params.add(new BasicNameValuePair("RecipeId", "" + _recipeId));
        params.add(new BasicNameValuePair("Rating", "" + _rating));
        params.add(new BasicNameValuePair("Review", _review));
        data.helpers.onlineData.create(params);
    }

    public static void deleteUnit(int _id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_rating"));
        params.add(new BasicNameValuePair("id", "" + _id));
        data.helpers.onlineData.delete(params);
    }
}
