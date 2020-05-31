package xyz.hipstermojo.battr;

import androidx.room.Entity;

import java.util.List;

public class Instruction {
    public List<Step> steps;
    class Step {
        String step;
        int number;
    }

}
