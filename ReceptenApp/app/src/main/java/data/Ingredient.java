package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Toine on 5/11/2014.
 */
public class Ingredient implements Parcelable{
    private int ID;
    private String Name;
    private ArrayList<Unit> AllowedUnits;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(Name);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
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

    public ArrayList<Unit> getAllowedUnits() {
        return AllowedUnits;
    }

    public void setAllowedUnits(ArrayList<Unit> allowedUnits) {
        AllowedUnits = allowedUnits;
    }

    public Ingredient(Parcel in){
        this.ID = in.readInt();
        this.Name = in.readString();
    }
    public Ingredient(int id, String Name){
        this.ID = id;
        this.Name = Name;
    }
}
