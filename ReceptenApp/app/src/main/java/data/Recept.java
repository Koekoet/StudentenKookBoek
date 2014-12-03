package data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Toine on 5/11/2014.
 */
public class Recept implements Parcelable{
    private int ID;
    private String Name;
    private int AuthorID;
    //private Author Author;
    private String Duration;
    private String Cost;
    private int NumberOfPersons;
    private int DifficultyID;
    private Difficulty Difficulty;
    private String Picture;
    private ArrayList<Ingredient> Ingredients;
    private String RecipeText;


    private Recept(Parcel in){
        setID(in.readInt());
        setName(in.readString());
        setAuthorID(in.readInt());
        //setAuthor((Author) in.readParcelable(Author.class.getClassLoader()));
        setIngredients(in.readArrayList(Ingredient.class.getClassLoader()));
        setDuration(in.readString());
        setCost(in.readString());
        setNumberOfPersons(in.readInt());
        setDifficultyID(in.readInt());
        setDifficulty((data.Difficulty) in.readParcelable(data.Difficulty.class.getClassLoader()));
        setPicture(in.readString());
        setRecipeText(in.readString());
    }
    public Recept(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getID());
        out.writeString(getName());
        out.writeInt(getAuthorID());
        //out.writeParcelable(getAuthor(), 0);
        out.writeList(getIngredients());
        out.writeString(getDuration());
        out.writeString(getCost());
        out.writeInt(getNumberOfPersons());
        out.writeInt(getDifficultyID());
        out.writeParcelable(getDifficulty(), flags);
        out.writeString(getPicture());
        out.writeString(getRecipeText());
    }

    public static final Creator<Recept> CREATOR = new Creator<Recept>() {
        @Override
        public Recept createFromParcel(Parcel parcel) {
            return new Recept(parcel);
        }

        @Override
        public Recept[] newArray(int i) {
            return new Recept[i];
        }
    };

    //region getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(int authorID) {
        AuthorID = authorID;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public int getNumberOfPersons() {
        return NumberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        NumberOfPersons = numberOfPersons;
    }

    public int getDifficultyID() {
        return DifficultyID;
    }

    public void setDifficultyID(int difficultyID) {
        DifficultyID = difficultyID;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getRecipeText() {
        return RecipeText;
    }

    public void setRecipeText(String recipeText) {
        RecipeText = recipeText;
    }

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    /*public Author getAuthor() {
        return Author;
    }

    public void setAuthor(Author author) {
        Author = author;
    }*/

    public Difficulty getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        Difficulty = difficulty;
    }
    //endregion

    public class IngredientList extends ArrayList<Ingredient> implements Parcelable{

        public IngredientList(){}
        public IngredientList(Parcel in){
            this();
            readFromParcel(in);
        }

        private void readFromParcel(Parcel in) {
            this.clear();

            //lezen van de list size
            int size = in.readInt();

            for(int i = 0; i<size; i++){
                Ingredient ingredient = new Ingredient(in.readInt(), in.readString());
                this.add(ingredient);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public final Creator<IngredientList> CREATOR = new Creator<IngredientList>() {
            @Override
            public IngredientList createFromParcel(Parcel parcel) {
                return new IngredientList(parcel);
            }

            @Override
            public IngredientList[] newArray(int i) {
                return new IngredientList[i];
            }
        };

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            int size = this.size();

            parcel.writeInt(size);

            for(int i =0; i<size;i++){
                Ingredient ingredient = this.get(i);

                parcel.writeInt(ingredient.getID());
                parcel.writeString(ingredient.getName());
            }
        }
    }

    public static ArrayList<Recept> getAllRecipes() {
        ArrayList<Recept> list = new ArrayList<Recept>();
        JSONArray recipes = data.helpers.onlineData.selectAllData("ap_recipe");
        if(recipes != null) {
            for (int i = 0; i < recipes.length(); i++) {
                try {
                    JSONObject c = recipes.getJSONObject(i);
                    //ophalen data
                    int id = c.getInt("ID");
                    String name = c.getString("Recipename");
                    int authorId = c.getInt("AuthorId");
                    String duration = c.getString("Duration");
                    String cost = c.getString("Cost");
                    int numberOfPersons = c.getInt("NumberOfPersons");
                    int difficultyId = c.getInt("DifficultyId");
                    data.Difficulty dif = data.Difficulty.getDifficultyById(difficultyId);
                    String picture = c.getString("Picture");
                    ArrayList<Ingredient> ingredients = makeIngredientsList(c.getString("Ingredients"));
                    String recipeText = c.getString("RecipeText");

                    //invullen in nieuw recept
                    Recept rec = new Recept();
                    rec.ID = id;
                    rec.Name = name;
                    rec.AuthorID = authorId;
                    rec.Duration = duration;
                    rec.Cost = cost;
                    rec.NumberOfPersons = numberOfPersons;
                    rec.DifficultyID = difficultyId;
                    rec.Difficulty = dif;
                    rec.Picture = picture;
                    rec.Ingredients = ingredients;
                    rec.RecipeText = recipeText;

                    list.add(rec);
                } catch (Exception e) {
                }
            }

        }else{
            return null;
        }
        return list;
    }

    /*public static Category getCategoryById(int id) {
        Category cat = new Category();
        JSONArray category = data.helpers.onlineData.selectDataById("ap_recept_category", id);
        if (category != null && category.length() == 1) {
            try {
                JSONObject c = category.getJSONObject(0);
                int _id = c.getInt("ID");
                String name = c.getString("Name");
                String picture = c.getString("Picture");
                Category newCat = new Category(_id, picture, name);
                cat = newCat;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return cat;
    }*/

    private static ArrayList<Ingredient> makeIngredientsList(String ingredients) {
        ArrayList<Ingredient> ingrList = new ArrayList<Ingredient>();
        String[] sDelen = ingredients.split(";");
        for(int i = 0; i < sDelen.length; i++){
            try{
                Ingredient ingr = Ingredient.getIngredientById(Integer.parseInt(sDelen[i]));
                ingrList.add(ingr);
            }catch (Exception ex){}
        }
        return ingrList;
    }

}
