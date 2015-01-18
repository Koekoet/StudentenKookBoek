package be.howest.nmct.receptenapp.data.ReceptData;

import android.content.ContentValues;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.CategoryData.Category;
import be.howest.nmct.receptenapp.data.IngredientData.Ingredient;
import be.howest.nmct.receptenapp.data.Difficulty;
import be.howest.nmct.receptenapp.data.helpers.onlineData;


/**
 * Created by Toine on 5/11/2014.
 */
public class Recept implements Parcelable {
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
    private ArrayList<Category> Categories;


    private Recept(Parcel in) {
        setID(in.readInt());
        setName(in.readString());
        setAuthorID(in.readInt());
        //setAuthor((Author) in.readParcelable(Author.class.getClassLoader()));
        setIngredients(in.readArrayList(Ingredient.class.getClassLoader()));
        setDuration(in.readString());
        setCost(in.readString());
        setNumberOfPersons(in.readInt());
        setDifficultyID(in.readInt());
        setDifficulty((be.howest.nmct.receptenapp.data.Difficulty) in.readParcelable(be.howest.nmct.receptenapp.data.Difficulty.class.getClassLoader()));
        setPicture(in.readString());
        setRecipeText(in.readString());
        setCategories(in.readArrayList(Category.class.getClassLoader()));
    }

    public Recept() {
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
        //out.writeParcelable(getAuthor(), 0);
        out.writeList(getIngredients());
        out.writeString(getDuration());
        out.writeString(getCost());
        out.writeInt(getNumberOfPersons());
        out.writeInt(getDifficultyID());
        out.writeParcelable(getDifficulty(), flags);
        out.writeString(getPicture());
        out.writeString(getRecipeText());
        out.writeList(getCategories());
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

    public ArrayList<Category> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        Categories = categories;
    }
    //endregion

    public class IngredientList extends ArrayList<Ingredient> implements Parcelable {

        public IngredientList() {
        }

        public IngredientList(Parcel in) {
            this();
            readFromParcel(in);
        }

        private void readFromParcel(Parcel in) {
            this.clear();

            //lezen van de list size
            int size = in.readInt();

            for (int i = 0; i < size; i++) {
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

            for (int i = 0; i < size; i++) {
                Ingredient ingredient = this.get(i);

                parcel.writeInt(ingredient.getID());
                parcel.writeString(ingredient.getName());
            }
        }
    }

    public static ArrayList<Recept> getAllRecipes() {
        ArrayList<Recept> list = new ArrayList<Recept>();
        JSONArray recipes = onlineData.selectAllData("ap_recipe");
        if (recipes != null) {
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
                    be.howest.nmct.receptenapp.data.Difficulty dif = be.howest.nmct.receptenapp.data.Difficulty.getDifficultyById(difficultyId);
                    String picture = c.getString("Picture");
                    //ArrayList<Ingredient> ingredients = makeIngredientsList(c.getString("Ingredients"));
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
                    //rec.Ingredients = ingredients;
                    rec.RecipeText = recipeText;

                    list.add(rec);
                } catch (Exception e) {
                }
            }

        } else {
            return null;
        }
        return list;
    }
    public static MatrixCursor getAllRecipesCursor(){
        String[] columnNames = {"ID", "Name", "AuthorId","Duration","Cost","NumberOfPersons","DifficultyId","Difficulty","Picture","Ingredient","RecipeText"};
        MatrixCursor matrixCursor = new MatrixCursor(columnNames);

        JSONArray recipes = onlineData.selectAllData("ap_recipe");
        if (recipes != null) {
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
                    be.howest.nmct.receptenapp.data.Difficulty dif = be.howest.nmct.receptenapp.data.Difficulty.getDifficultyById(difficultyId);
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

                    //matrixCursor.addRow(new String[]{"value1", "value2"});
                    matrixCursor.addRow(new Recept[]{rec});
                } catch (Exception e) {
                }
            }

        } else {
            return null;
        }
        return matrixCursor;
    }


    public static Recept getRecipeById(int id) {
        Recept newRec = new Recept();
        JSONArray recipes = onlineData.selectDataById("ap_recipe", id);
        if (recipes != null && recipes.length() == 1) {
            try {
                JSONObject c = recipes.getJSONObject(0);
                //ophalen data
                int _id = c.getInt("ID");
                String name = c.getString("Recipename");
                int authorId = c.getInt("AuthorId");
                String duration = c.getString("Duration");
                String cost = c.getString("Cost");
                int numberOfPersons = c.getInt("NumberOfPersons");
                int difficultyId = c.getInt("DifficultyId");
                be.howest.nmct.receptenapp.data.Difficulty dif = be.howest.nmct.receptenapp.data.Difficulty.getDifficultyById(difficultyId);
                String picture = c.getString("Picture");
                ArrayList<Ingredient> ingredients = makeIngredientsList(c.getString("Ingredients"));
                String recipeText = c.getString("RecipeText");

                //invullen in nieuw recept
                Recept rec = new Recept();
                rec.ID = _id;
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
                newRec = rec;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return newRec;
    }

    private static ArrayList<Ingredient> makeIngredientsList(String ingredients) {
        ArrayList<Ingredient> ingrList = new ArrayList<Ingredient>();
        String[] sDelen = ingredients.split(";");
        for (int i = 0; i < sDelen.length; i++) {
            try {
                Ingredient ingr = Ingredient.getIngredientById(Integer.parseInt(sDelen[i]));
                ingrList.add(ingr);
            } catch (Exception ex) {
            }
        }
        return ingrList;
    }

    public static int createRecipe(String _name, int _author, String _duration, String _cost, int _persons, int _difficultyId, String _picture, String _ingredients, String _recipeText) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recipe"));
        params.add(new BasicNameValuePair("Name", "" + _name));
        params.add(new BasicNameValuePair("Author", "" + _author));
        params.add(new BasicNameValuePair("Duration", "" + _duration));
        params.add(new BasicNameValuePair("Cost", "" + _cost));
        params.add(new BasicNameValuePair("Persons", "" + _persons));
        params.add(new BasicNameValuePair("Difficulty", "" + _difficultyId));
        params.add(new BasicNameValuePair("Picture", "" + _picture));
        params.add(new BasicNameValuePair("Ingredients", "" + _ingredients));
        params.add(new BasicNameValuePair("RecipeText", "" + _recipeText));
        int newId = onlineData.create(params);
        return newId;
    }
    public static int createRecipe(Recept recept, String ingredients){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recipe"));
        params.add(new BasicNameValuePair("Name", "" + recept.getName()));
        params.add(new BasicNameValuePair("Author", "" + recept.getAuthorID()));
        params.add(new BasicNameValuePair("Duration", "" + recept.getDuration()));
        params.add(new BasicNameValuePair("Cost", "" + recept.getCost()));
        params.add(new BasicNameValuePair("Persons", "" + recept.getNumberOfPersons()));
        params.add(new BasicNameValuePair("Difficulty", "" + recept.getDifficultyID()));
        params.add(new BasicNameValuePair("Picture", "" + recept.getPicture()));
        params.add(new BasicNameValuePair("Ingredients", "" + ingredients));
        params.add(new BasicNameValuePair("RecipeText", "" + recept.getRecipeText()));
        int newId = onlineData.create(params);
        return newId;
    }

    public static void delete(int _id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recipe"));
        params.add(new BasicNameValuePair("id", ""+_id));
        onlineData.delete(params);
    }

    //CURSOR METHODES
    public static Boolean LoadAllRecipesCURSOR(Context context){
        JSONArray recipes = onlineData.selectAllData("ap_recipe");
        if (recipes != null) {
            for (int i = 0; i < recipes.length(); i++) {
                try {
                    JSONObject c = recipes.getJSONObject(i);
                    //ophalen data
                    ContentValues values = new ContentValues();
                    values.put(ReceptTable.COLUMN_ID, c.getInt("ID"));
                    values.put(ReceptTable.COLUMN_NAME, c.getString("Recipename"));
                    values.put(ReceptTable.COLUMN_AUTHORID, c.getInt("AuthorId"));
                    values.put(ReceptTable.COLUMN_DURATION, c.getString("Duration"));
                    values.put(ReceptTable.COLUMN_COST, c.getString("Cost"));
                    values.put(ReceptTable.COLUMN_NUMBEROFPERSONS, c.getInt("NumberOfPersons"));
                    values.put(ReceptTable.COLUMN_DIFFICULTYID, c.getInt("DifficultyId"));
                    values.put(ReceptTable.COLUMN_PICTURE, c.getString("Picture"));
                    values.put(ReceptTable.COLUMN_INGREDIENTS, c.getString("Ingredients"));
                    values.put(ReceptTable.COLUMN_RECIPETEXT, c.getString("RecipeText"));
                    context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_REC, values);

                } catch (Exception e) {
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
