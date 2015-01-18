package be.howest.nmct.receptenapp.data.IngredientData;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.Unit;
import be.howest.nmct.receptenapp.data.helpers.onlineData;


/**
 * Created by Toine on 5/11/2014.
 */
public class Ingredient implements Parcelable{
    public int ID;
    public String Name;
    private int Amount;
    private int UnitID;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(Name);
        parcel.writeInt(Amount);
        parcel.writeInt(UnitID);
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


    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public Ingredient(){}
    public Ingredient(Parcel in){
        this.ID = in.readInt();
        this.Name = in.readString();
        this.Amount = in.readInt();
        this.UnitID = in.readInt();
    }
    public Ingredient(int id, String Name){
        this.ID = id;
        this.Name = Name;
    }
    public Ingredient(int id, String Name, int amount, int unitID){
        this.ID = id;
        this.Name = Name;
        this.Amount = amount;
        this.UnitID = unitID;

    }


    public static Ingredient getIngredientById(int id) {
        Ingredient i = new Ingredient();
        JSONArray ingredient = onlineData.selectDataById("ap_ingredient", id);
        if (ingredient != null && ingredient.length() == 1) {
            try {
                JSONObject c = ingredient.getJSONObject(0);
                int _id = c.getInt("ID");
                String name = c.getString("Name");
                ArrayList<Unit> allowedUnits = makeArrayUnits(c.getString("AllowedUnits"));
                //Ingredient newIngr = new Ingredient(_id, name, allowedUnits);
                //i = newIngr;
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

    @Override
    public boolean equals(Object o) {
        boolean sameSame = false;
        if (o != null && o instanceof Ingredient) {
            sameSame = this.Name == ((Ingredient) o).Name;
        }
        return sameSame;
    }
    public static void createIngredient(String _name, String _allowedUnits){
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_ingredient"));
        params.add(new BasicNameValuePair("Name", _name));
        params.add(new BasicNameValuePair("AllowedUnits",_allowedUnits));
        onlineData.create(params);
        */
    }
    public static void deleteIngredient(int _id){
        /*
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_ingredient"));
        params.add(new BasicNameValuePair("id", ""+_id));
        onlineData.delete(params);
        */
    }


    //CURSOR METHODES
    /*
    public static Boolean LoadAllCategories(Context context){
        JSONArray categories = onlineData.selectAllData("ap_recept_category");
        if(categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                try {
                    JSONObject c = categories.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    int id = c.getInt("ID");
                    values.put(IngredientTable.COLUMN_ID, c.getInt("ID"));
                    values.put(IngredientTable.COLUMN_NAME, c.getString("Name"));
                    values.put(IngredientTable.COLUMN_AMOUNT, c.getString("Amount"));
                    values.put(IngredientTable.COLUMN_UNITID, c.getString("UnitId"));
                    context.getContentResolver().insert(ReceptenAppContentProvider., values);
                } catch (Exception e) {
                }
            }
            return true;
        }else{
            return false;
        }
    }

    public static ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        JSONArray ingredients = onlineData.selectAllData("ap_ingredient");
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
    */

}
