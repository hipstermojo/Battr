package xyz.hipstermojo.battr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {
    private static final String baseImageUrl = "https://spoonacular.com/recipeImages/";
    private String id;
    @SerializedName("image")
    private String imageName;
    private String title;

    protected Recipe(Parcel in) {
        id = in.readString();
        imageName = in.readString();
        title = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getImage() {

        return String.format("%s%s-480x360.%s", baseImageUrl, id, imageName.split("\\.")[1]);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageName);
        dest.writeString(this.title);
    }
}
