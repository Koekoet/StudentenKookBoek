package data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import data.CategoryData.Category;
import data.ReceptData.Recept;

/**
 * Created by Mattias on 3/12/2014.
 */
public class RecipeView implements Parcelable {
    private ArrayList<Recept> arrRecipes;
    private Category category;

    private RecipeView(Parcel in){
        setArrRecipes(in.readArrayList(Recept.class.getClassLoader()));
        setCategory((Category) in.readParcelable(Category.class.getClassLoader()));
    }
    public RecipeView(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(getCategorie(), 0);
        out.writeList(getArrRecipes());
    }

    public static final Creator<RecipeView> CREATOR = new Creator<RecipeView>() {
        @Override
        public RecipeView createFromParcel(Parcel parcel) {
            return new RecipeView(parcel);
        }

        @Override
        public RecipeView[] newArray(int i) {
            return new RecipeView[i];
        }
    };

    public ArrayList<Recept> getArrRecipes() {
        return arrRecipes;
    }
    public void setArrRecipes(ArrayList<Recept> arrRecipes) {
        this.arrRecipes = arrRecipes;
    }

    public Category getCategorie() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

}
