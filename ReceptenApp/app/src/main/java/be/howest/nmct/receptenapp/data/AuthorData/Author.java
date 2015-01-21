package be.howest.nmct.receptenapp.data.AuthorData;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.helpers.onlineData;

/**
 * Created by Toine on 5/11/2014.
 */
public class Author implements Parcelable {
    private String AuthorID;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Image;

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
        out.writeString(getImage());

    }

    private Author(Parcel in) {
        setAuthorID(in.readString());
        setFirstname(in.readString());
        setLastname(in.readString());
        setEmail(in.readString());
        setImage(in.readString());
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    //DATABASE
    public static Integer saveAuthor(Author author){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_users"));
        params.add(new BasicNameValuePair("AuthorId", "" + author.getAuthorID()));
        params.add(new BasicNameValuePair("Firstname", "" + author.getFirstname()));
        params.add(new BasicNameValuePair("Lastname", "" + author.getLastname()));
        params.add(new BasicNameValuePair("Email", "" + author.getEmail()));
        params.add(new BasicNameValuePair("Image", "" + author.getImage()));
        int newId = onlineData.create(params);

        return newId;

    }
    //CURSOR
    public static boolean loadAllAuthors(Context context){

        context.getContentResolver().delete(ReceptenAppContentProvider.CONTENT_URI_AUTHOR,null,null);

        JSONArray categories = onlineData.selectAllData("ap_users");
        if(categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                try {
                    JSONObject c = categories.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(AuthorTable.COLUMN_ID, c.getString("AuthorId"));

                    values.put(AuthorTable.COLUMN_FNAME, c.getString("FirstName"));
                    values.put(AuthorTable.COLUMN_LNAME, c.getString("LastName"));
                    context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_AUTHOR, values);
                } catch (Exception e) {
                }
            }
            return true;
        }else{
            return false;
        }
    }

}
