package data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toine on 5/11/2014.
 */
public class Difficulty implements Parcelable{
    private int ID;
    private String Description;

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
}
