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
public class Ingredient implements Parcelable{
    public int ID;
    public String Name;
    public ArrayList<Unit> AllowedUnits;

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

    public Ingredient(){}
    public Ingredient(Parcel in){
        this.ID = in.readInt();
        this.Name = in.readString();
    }
    public Ingredient(int id, String Name){
        this.ID = id;
        this.Name = Name;
    }
    public Ingredient(int id, String Name, ArrayList<Unit> allowedUnits){
        this.ID = id;
        this.Name = Name;
        this.AllowedUnits = allowedUnits;
    }

    public static ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        JSONArray ingredients = data.helpers.onlineData.selectAllData("ap_ingredient");
        if(ingredients != null) {
            for (int i = 0; i < ingredients.length(); i++) {
                try {
                    JSONObject c = ingredients.getJSONObject(i);
                    int id = c.getInt("ID");
                    String name = c.getString("Name");
                    ArrayList<Unit> allowedUnits = makeArrayUnits(c.getString("AllowedUnits"));
                    Ingredient ingr = new Ingredient(id, name, allowedUnits);
                    list.add(ingr);
                } catch (Exception e) {
                }
            }
            return list;
        }else{
            return null;
        }
    }
    public static Ingredient getIngredientById(int id) {
        Ingredient i = new Ingredient();
        JSONArray ingredient = data.helpers.onlineData.selectDataById("ap_ingredient", id);
        if (ingredient != null && ingredient.length() == 1) {
            try {
                JSONObject c = ingredient.getJSONObject(0);
                int _id = c.getInt("ID");
                String name = c.getString("Name");
                ArrayList<Unit> allowedUnits = makeArrayUnits(c.getString("AllowedUnits"));
                Ingredient newIngr = new Ingredient(_id, name, allowedUnits);
                i = newIngr;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return i;
    }
    private static ArrayList<Unit> makeArrayUnits(String allowedUnits) {
        ArrayList<Unit> units = new ArrayList<Unit>();
        String[] sDelen = allowedUnits.split(";");
        for(int i = 0; i < sDelen.length; i++){
            Unit u = Unit.getUnitById(Integer.parseInt(sDelen[i]));
            units.add(u);
        }
        return units;
    }

    public static void createIngredient(String _name, String _allowedUnits){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_ingredient"));
        params.add(new BasicNameValuePair("Name", _name));
        params.add(new BasicNameValuePair("AllowedUnits",_allowedUnits));
        data.helpers.onlineData.create(params);
    }

    public static void deleteIngredient(int _id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_ingredient"));
        params.add(new BasicNameValuePair("id", ""+_id));
        data.helpers.onlineData.delete(params);
    }
}
