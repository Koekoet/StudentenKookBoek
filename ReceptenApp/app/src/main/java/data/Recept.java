package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Toine on 5/11/2014.
 */
public class Recept implements Parcelable{
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

    private Recept(Parcel in){
        setID(in.readInt());
        setName(in.readString());
        setAuthorID(in.readInt());
        setDuration(in.readString());
        setCost(in.readString());
        setNumberOfPersons(in.readInt());
        setDifficultyID(in.readInt());
        setPicture(in.readString());
        setRecipeText(in.readString());
        setCategoryID(in.readInt());
    }

    public Recept(){
        setID(0);
        setName("Name");
        setAuthorID(0);
        setDuration("30");
        setCost("15");
        setNumberOfPersons(4);
        setDifficultyID(3);
        setPicture("test");
        setRecipeText("Bereiding gaat azo:");
        setCategoryID(0);
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient(0,"Ing1"));
        ingredients.add(new Ingredient(1,"Ing2"));
        ingredients.add(new Ingredient(2,"Ing3"));
        setIngredients(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getID());
        out.writeString(getName());
        out.writeInt(getAuthorID());
        //out.write
        out.writeString(getDuration());
        out.writeString(getCost());
        out.writeInt(getNumberOfPersons());
        out.writeInt(getDifficultyID());
        //out.write
        out.writeString(getPicture());
        //out.writeStringArray(Ingredients);
        out.writeString(getRecipeText());
        out.writeInt(getCategoryID());
        //out.write
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

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

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
}
