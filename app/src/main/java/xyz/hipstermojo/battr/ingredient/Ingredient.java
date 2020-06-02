package xyz.hipstermojo.battr.ingredient;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public long ingredientId;
    public int recipeId;
    public String name;
    public double amount;
    public String aisle;
    public String unit;

    public static List<Ingredient> fromHashMapToList(List<Object> objectList) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Object object: objectList){
            Map<String, Object> objectMap = (Map<String, Object>) object;
            Ingredient ingredient = new Ingredient();
            ingredient.recipeId = ((Long) objectMap.get("recipeId")).intValue();
            ingredient.name = (String) objectMap.get("name");
            ingredient.aisle = (String) objectMap.get("aisle");
            ingredient.unit = (String) objectMap.get("unit");
            ingredient.amount = (double) objectMap.get("amount");
            ingredients.add(ingredient);
        }
        return ingredients;
    }
}
