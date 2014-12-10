package data;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Toine on 3/12/2014.
 */
public class Rating {
    private String recipeName = "";
    private float points = 0;
    private String review = "";


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Rating(String recipeName, float points, String review){
        super();
        this.recipeName = recipeName;
        this.points = points;
        this.review = review;
    }

    public static ArrayList<Rating> readRatingsByRecipeID(Context c, int recipeID){
        ArrayList<Rating> ratings = new ArrayList<Rating>();

        //Volgende regel moet uit comments worden gehaald...
        //Recept recept = Recept.getRecipeByID(recipeID);

        //ratings ophalen...


        return ratings;
    }
}
