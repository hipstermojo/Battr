package xyz.hipstermojo.battr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable {
    private static final String baseImageUrl = "https://spoonacular.com/recipeImages/";
    private int id;
    @SerializedName("readyInMinutes")
    private int duration;
    private String image;
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;
    @SerializedName("analyzedInstructions")
    private List<Instruction> instructions;
    private int servings;
    @SerializedName("sourceName")
    private String source;
    private String sourceUrl;
    private String title;


    protected Recipe(Parcel in) {
        id = in.readInt();
        duration = in.readInt();
        image = in.readString();
        servings = in.readInt();
        sourceUrl = in.readString();
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

    public int getId() {
        return id;
    }

    public String getImage() {
        if (!this.image.startsWith("http")){
            this.image =  String.format("%s%s-480x360.%s", baseImageUrl, id, this.image.split("\\.")[1]);
        }
        return this.image;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getServings() {
        return servings;
    }

    public String getSource() {
        return source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.duration);
        dest.writeString(this.image);
        dest.writeInt(this.servings);
        dest.writeString(this.sourceUrl);
        dest.writeString(this.title);
    }

    class Instruction {
        public List<Step> steps;
    }

    class Step {
        String step;
        int number;
    }

    class Ingredient {
        String name;
        double amount;
    }
}
