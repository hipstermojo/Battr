package xyz.hipstermojo.battr;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey
    public long ingredientId;
    public int recipeId;
    public String name;
    public double amount;
}
