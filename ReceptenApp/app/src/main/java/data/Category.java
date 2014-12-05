package data;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toine on 5/11/2014.
 */
public class Category implements Parcelable{
    private int ID;
    private String Picture;
    private String Name;

    public Category(int id, String picture, String name){
        this.ID = id;
        this.Picture = picture;
        this.Name = name;
    }
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

    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<Category>();
        JSONArray categories = data.helpers.onlineData.selectAllData("ap_recept_category");
        if(categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                try {
                    JSONObject c = categories.getJSONObject(i);
                    int id = c.getInt("ID");
                    String name = c.getString("Name");
                    Category diff = new Category(id, "", name);
                    list.add(diff);
                } catch (Exception e) {
                }
            }
            return list;
        }else{
            return null;
        }
    }
    public static Category getCategoryById(int id) {
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
    }
    public static void createCategory(String _picture, String _name){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_recept_category"));
        params.add(new BasicNameValuePair("Picture", _picture));
        params.add(new BasicNameValuePair("Name", _name));
        data.helpers.onlineData.create(params);
    }
}
