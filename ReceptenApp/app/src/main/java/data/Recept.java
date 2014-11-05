package data;

import java.util.ArrayList;

/**
 * Created by Toine on 5/11/2014.
 */
public class Recept {
    private int ID;
    private String Name;
    private int AuthorID;
    private Author Author;
    private String Duration;
    private String Cost;
    private int NumberOfPersons;
    private int DifficultyID;
    private Difficulty Difficulty;
    private String Picture;
    private ArrayList<Ingredient> Ingredients;
    private String RecipeText;
    private int CategoryID;
    private Category Category;
}
