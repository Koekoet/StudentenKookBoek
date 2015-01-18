package be.howest.nmct.receptenapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toine on 5/11/2014.
 */
public class Author implements Parcelable {
    private String AuthorID;
    private String Firstname;
    private String Lastname;
    private String Email;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getAuthorID());
        out.writeString(getFirstname());
        out.writeString(getLastname());
        out.writeString(getEmail());

    }

    private Author(Parcel in) {
        setAuthorID(in.readString());
        setFirstname(in.readString());
        setLastname(in.readString());
        setEmail(in.readString());
    }

    public Author() {

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

    public String getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(String authorID) {
        AuthorID = authorID;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
