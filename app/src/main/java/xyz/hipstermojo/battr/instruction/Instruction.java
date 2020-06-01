package xyz.hipstermojo.battr.instruction;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

public class Instruction {
    public List<Step> steps;

    @Entity(tableName = "steps")
    public static class Step {
        public String step;
        public int number;
        public int recipeId;
        @PrimaryKey(autoGenerate = true)
        int stepId;
    }
}
