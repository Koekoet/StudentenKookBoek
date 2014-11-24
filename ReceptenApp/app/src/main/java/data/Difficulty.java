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
public class Difficulty implements Parcelable{
    private int ID;
    private String Description;

    public Difficulty(int id, String description){
        this.ID = id;
        this.Description = description;
    }
    public Difficulty(){

    }
    private Difficulty(Parcel in){
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

    public static ArrayList<Difficulty> getAllDifficulties (){
        ArrayList<Difficulty> list = new ArrayList<Difficulty>();
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_difficulty_recept"));
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("http://student.howest.be/tijs.de.lameillieu1/AndroidRecipeApp/index.php", ServiceHandler.POST, params);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Iterator<String> iterator = jsonObj.keys();
                //String test = "result";
                String name = iterator.next();
                if(name.toString().equals(String.valueOf("result"))) {
                    JSONArray difficulties = jsonObj.getJSONArray("result");
                    for (int i = 0; i < difficulties.length(); i++) {
                        JSONObject c = difficulties.getJSONObject(i);
                        int id = c.getInt("ID");
                        String description = c.getString("Description");
                        Difficulty diff = new Difficulty(id, description);
                        list.add(diff);
                    }
                }else if(name.toString().equals(String.valueOf("error"))){
                    String error = jsonObj.getString("error");
                    Log.d("Error: ",error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return list;
    }
}
