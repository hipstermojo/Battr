package xyz.hipstermojo.battr.ingredient;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public long ingredientId;
    public int recipeId;
    public String name;
    public double amount;
    public String aisle;
    public String unit;

}
