package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Toine on 5/11/2014.
 */
public class Unit {
    private int ID;
    private String Name;
    private String Abbreviation;

    public Unit() {
    }

    public Unit(int ID, String name, String abbreviation) {
        this.ID = ID;
        this.Name = name;
        this.Abbreviation = abbreviation;
    }

    public static ArrayList<Unit> getAllUnits() {
        ArrayList<Unit> list = new ArrayList<Unit>();
        JSONArray units = data.helpers.onlineData.selectAllData("ap_unit");
        if (units != null) {
            for (int i = 0; i < units.length(); i++) {
                try {
                    JSONObject c = units.getJSONObject(i);
                    int id = c.getInt("ID");
                    String name = c.getString("Name");
                    String abbr = c.getString("Abbreviation");
                    Unit unit = new Unit(id, name, abbr);
                    list.add(unit);
                } catch (Exception e) {
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public static Unit getUnitById(int id) {
        Unit u = new Unit();
        JSONArray unit = data.helpers.onlineData.selectDataById("ap_unit", id);
        if (unit != null && unit.length() == 1) {
            try {
                JSONObject c = unit.getJSONObject(0);

                int ID = c.getInt("ID");
                String Abbreviation = c.getString("Abbreviation");
                String Name = c.getString("Name");
                Unit newUnit = new Unit(ID, Name, Abbreviation);
                u = newUnit;
                //return newUnit;
            } catch (Exception e) {
            }
            //return u;
            } else {
            return null;
        }
        return u;
    }
}
