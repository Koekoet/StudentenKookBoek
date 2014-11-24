package data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

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
        setAuthor((Author) in.readParcelable(Author.class.getClassLoader()));
        setIngredients(in.readArrayList(Ingredient.class.getClassLoader()));
        setDuration(in.readString());
        setCost(in.readString());
        setNumberOfPersons(in.readInt());
        setDifficultyID(in.readInt());
        setDifficulty((data.Difficulty) in.readParcelable(data.Difficulty.class.getClassLoader()));
        //setPicture(in.readString());
        setRecipeText(in.readString());
        setCategoryID(in.readInt());
        setCategory((data.Category) in.readParcelable(data.Category.class.getClassLoader()));
    }

    public Recept(){
        //Dummy-data
        //ArrayList<Difficulty> lijst = Difficulty.getAllDifficulties();
        setID(0);
        setName("Name");
        setAuthorID(0);
        setDuration("30");
        setCost("15");
        setNumberOfPersons(4);
        setDifficultyID(3);
        //setPicture("test");
        setRecipeText("Bereiding gaat azo:");
        setCategoryID(0);
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient(0,"Ing1"));
        ingredients.add(new Ingredient(1,"Ing2"));
        ingredients.add(new Ingredient(2,"Ing3"));
        setIngredients(ingredients);
        data.Author author = new Author();
        author.setID(0);
        author.setName("Koekoet");
        author.setEmail("toinekoekoet@gmail.com");
        author.setPassword("nope");
        setAuthor(author);
        data.Difficulty diff = new Difficulty();
        diff.setID(3);
        diff.setDescription("Moeilijk");
        setDifficulty(diff);
        data.Category cat = new Category();
        cat.setID(0);
        cat.setName("Categorie 0");
        cat.setPicture("iets");
        setCategory(cat);
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
        out.writeParcelable(getAuthor(), 0);
        out.writeList(getIngredients());
        out.writeString(getDuration());
        out.writeString(getCost());
        out.writeInt(getNumberOfPersons());
        out.writeInt(getDifficultyID());
        out.writeParcelable(getDifficulty(),flags);
        //out.writeString(getPicture());
        out.writeString(getRecipeText());
        out.writeInt(getCategoryID());
        out.writeParcelable(getCategory(), flags);
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

    public Author getAuthor() {
        return Author;
    }

    public void setAuthor(Author author) {
        Author = author;
    }

    public Difficulty getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        Difficulty = difficulty;
    }

    public Category getCategory() {
        return Category;
    }

    public void setCategory(Category category) {
        Category = category;
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
