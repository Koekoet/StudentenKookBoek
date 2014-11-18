package data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toine on 5/11/2014.
 */
public class Author implements Parcelable{
    private int ID;
    private String Name;
    private String Email;
    private String Password;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getID());
        out.writeString(getName());
        out.writeString(getEmail());
        out.writeString(getPassword());

    }

    private Author(Parcel in){
        setID(in.readInt());
        setName(in.readString());
        setEmail(in.readString());
        setPassword(in.readString());
    }

    public Author(){

    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel parcel) {
            return new Author(parcel);
        }

        @Override
        public Author[] newArray(int i) {
            return new Author[i];
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
