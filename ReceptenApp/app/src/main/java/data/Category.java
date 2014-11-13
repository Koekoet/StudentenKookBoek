package data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toine on 5/11/2014.
 */
public class Category implements Parcelable{
    private int ID;
    private String Picture;
    private String Name;


    public Category(){

    }
    private Category(Parcel in){
        setID(in.readInt());
        setPicture(in.readString());
        setName(in.readString());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(getID());
        out.writeString(getPicture());
        out.writeString(getName());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel parcel) {
            return new Category(parcel);
        }

        @Override
        public Category[] newArray(int i) {
            return new Category[i];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
