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
        ID = in.readInt();
        Name = in.readString();
        AuthorID = in.readInt();
        Duration = in.readString();
        Cost = in.readString();
        NumberOfPersons = in.readInt();
        DifficultyID = in.readInt();
        Picture = in.readString();
        RecipeText = in.readString();
        CategoryID = in.readInt();
    }

    public Recept(){
        ID = 0;
        Name = "Name";
        AuthorID = 0;
        Duration = "30";
        Cost = "15";
        NumberOfPersons = 4;
        DifficultyID = 3;
        Picture = "test";
        RecipeText = "Bereiding gaat azo:";
        CategoryID = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ID);
        out.writeString(Name);
        out.writeInt(AuthorID);
        //out.write
        out.writeString(Duration);
        out.writeString(Cost);
        out.writeInt(NumberOfPersons);
        out.writeInt(DifficultyID);
        //out.write
        out.writeString(Picture);
        //out.writeStringArray(Ingredients);
        out.writeString(RecipeText);
        out.writeInt(CategoryID);
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
}
