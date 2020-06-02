package xyz.hipstermojo.battr.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;

import xyz.hipstermojo.battr.ingredient.Ingredient;
import xyz.hipstermojo.battr.instruction.Instruction;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
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
    @Ignore
    private static final String baseImageUrl = "https://spoonacular.com/recipeImages/";
    @PrimaryKey
    private int id;
    @SerializedName("readyInMinutes")
    private int duration;
    private String image;
    @Ignore
    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;
    @Ignore
    @SerializedName("analyzedInstructions")
    private List<Instruction> instructions;
    private int servings;
    @Ignore
    @SerializedName("sourceName")
    private String source;
    private String sourceUrl;
    private String title;
    private Date createdAt;

    public Recipe(int id, int duration, String image, int servings, String sourceUrl, String title) {
        this.id = id;
        this.duration = duration;
        this.image = image;
        this.servings = servings;
        this.sourceUrl = sourceUrl;
        this.title = title;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        duration = in.readInt();
        image = in.readString();
        servings = in.readInt();
        sourceUrl = in.readString();
        title = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        if (!this.image.startsWith("http")) {
            this.image = String.format("%s%s-480x360.%s", baseImageUrl, id, this.image.split("\\.")[1]);
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public static Recipe fromHashMap(Map<String, Object> map) {
        Recipe recipe = new Recipe(
                ((Long) map.get("id")).intValue(), ((Long) map.get("duration")).intValue(),
                (String) map.get("image"), ((Long) map.get("servings")).intValue(),
                (String) map.get("sourceUrl"), (String) map.get("title"));
        recipe.instructions = Instruction.fromHashMapToList((List<Object>) map.get("instructions"));
        recipe.ingredients = Ingredient.fromHashMapToList((List<Object>) map.get("ingredients"));
        recipe.source = (String) map.get("source");
        return recipe;
    }
}
