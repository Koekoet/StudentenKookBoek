package be.howest.nmct.receptenapp.data;

import android.content.Context;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import be.howest.nmct.receptenapp.data.ReceptData.Recept;
import be.howest.nmct.receptenapp.data.helpers.onlineData;

/**
 * Created by Toine on 3/12/2014.
 */
public class Rating {
    private int ID;
    private int AccountId;
    private int RecipeId;
    private Recept Recipe;
    private double Rating;
    private String Review;


    public static ArrayList<Rating> readRatingsByRecipeID(Context c, int recipeID) {
        ArrayList<Rating> ratings = new ArrayList<Rating>();

        //Volgende regel moet uit comments worden gehaald...
        //Recept recept = Recept.getRecipeByID(recipeID);

        //ratings ophalen...


        return ratings;

    }
    public Rating(){}

    public Rating(int ID, int accountId, int recipeId, Recept recipe, double rating, String review) {
        this.setID(ID);
        this.setAccountId(accountId);
        this.setRecipeId(recipeId);
        this.setRecipe(recipe);
        this.setRating(rating);
        this.setReview(review);
    }

    public static ArrayList<Rating> getAllRatings() {
        ArrayList<Rating> list = new ArrayList<Rating>();
        JSONArray ratings = onlineData.selectAllData("ap_rating");
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
        JSONArray rating = onlineData.selectDataById("ap_rating", id);
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
        JSONArray ratings = onlineData.selectRatingsByRecipeId("ap_rating", _recipeId);
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
        onlineData.create(params);
    }

    public static void deleteUnit(int _id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_rating"));
        params.add(new BasicNameValuePair("id", "" + _id));
        onlineData.delete(params);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int accountId) {
        AccountId = accountId;
    }

    public int getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(int recipeId) {
        RecipeId = recipeId;
    }

    public Recept getRecipe() {
        return Recipe;
    }

    public void setRecipe(Recept recipe) {
        Recipe = recipe;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }
}
