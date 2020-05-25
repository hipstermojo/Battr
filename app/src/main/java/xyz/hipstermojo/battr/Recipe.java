package xyz.hipstermojo.battr;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    private static final String baseImageUrl = "https://spoonacular.com/recipeImages/";
    private String id;
    @SerializedName("image")
    private String imageName;
    private String title;

    public String getImage() {

        return String.format("%s%s-480x360.%s", baseImageUrl, id, imageName.split("\\.")[1]);
    }

    public String getTitle() {
        return title;
    }
}
