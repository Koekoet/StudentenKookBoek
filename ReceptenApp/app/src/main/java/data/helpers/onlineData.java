package data.helpers;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by tijs1 on 26-11-2014.
 */
public class onlineData {

    public static JSONArray selectAllData(String tableName) {
        JSONArray array = new JSONArray();
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", tableName));
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("http://student.howest.be/tijs.de.lameillieu1/AndroidRecipeApp/index.php", ServiceHandler.POST, params);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Iterator<String> iterator = jsonObj.keys();
                //String test = "result";
                String name = iterator.next();
                if (name.toString().equals(String.valueOf("result"))) {
                    array =  jsonObj.getJSONArray("result");
                } else if (name.toString().equals(String.valueOf("error"))) {
                    String error = jsonObj.getString("error");
                    Log.d("Error: ", error);
                    array = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else

        {
            Log.e("ServiceHandler", "Couldn't get any data from the url");

        }
        return array;
    }
    public static JSONArray selectDataById(String tableName, int Id){
        JSONArray array = new JSONArray();
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", tableName));
        params.add(new BasicNameValuePair("id", ""+Id));
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall("http://student.howest.be/tijs.de.lameillieu1/AndroidRecipeApp/index.php", ServiceHandler.POST, params);

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Iterator<String> iterator = jsonObj.keys();
                //String test = "result";
                String name = iterator.next();
                if (name.toString().equals(String.valueOf("result"))) {
                    array =  jsonObj.getJSONArray("result");
                } else if (name.toString().equals(String.valueOf("error"))) {
                    String error = jsonObj.getString("error");
                    Log.d("Error: ", error);
                    array = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else

        {
            Log.e("ServiceHandler", "Couldn't get any data from the url");

        }
        return array;
    }
}
