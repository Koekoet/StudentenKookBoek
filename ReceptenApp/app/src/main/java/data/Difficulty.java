package data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import data.helpers.ServiceHandler;

/**
 * Created by Toine on 5/11/2014.
 */
public class Difficulty implements Parcelable {
    private int ID;
    private String Description;

    public Difficulty(int id, String description) {
        this.ID = id;
        this.Description = description;
    }

    public Difficulty() {

    }

    private Difficulty(Parcel in) {
        setID(in.readInt());
        setDescription(in.readString());
    }

    public static final Creator<Difficulty> CREATOR = new Creator<Difficulty>() {
        @Override
        public Difficulty createFromParcel(Parcel parcel) {
            return new Difficulty(parcel);
        }

        @Override
        public Difficulty[] newArray(int i) {
            return new Difficulty[i];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(getID());
        out.writeString(getDescription());
    }

    public static ArrayList<Difficulty> getAllDifficulties() {
        ArrayList<Difficulty> list = new ArrayList<Difficulty>();
        JSONArray difficulties = data.helpers.onlineData.selectAllData("ap_difficulty_recept");
        if(difficulties != null) {
            for (int i = 0; i < difficulties.length(); i++) {
                try {
                    JSONObject c = difficulties.getJSONObject(i);
                    int id = c.getInt("ID");
                    String description = c.getString("Description");
                    Difficulty diff = new Difficulty(id, description);
                    list.add(diff);
                } catch (Exception e) {
                }
            }
            return list;
        }else{
            return null;
        }
    }
    public static Difficulty getDifficultyById(int id) {
        Difficulty d = new Difficulty();
        JSONArray difficulty = data.helpers.onlineData.selectDataById("ap_difficulty_recept", id);
        if (difficulty != null && difficulty.length() == 1) {
            try {
                JSONObject c = difficulty.getJSONObject(0);
                int _id = c.getInt("ID");
                String name = c.getString("Description");
                Difficulty newDif = new Difficulty(_id, name);
                d = newDif;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return d;
    }

    public static void createDifficulty(String _description){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_difficulty_recept"));
        params.add(new BasicNameValuePair("Description", _description));
        data.helpers.onlineData.create(params);
    }
}
