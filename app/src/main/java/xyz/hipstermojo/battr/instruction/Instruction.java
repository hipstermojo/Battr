package xyz.hipstermojo.battr.instruction;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Instruction {
    public List<Step> steps;

    public static List<Instruction> fromHashMapToList(List<Object> objectMap) {
        Map<String, Object> holder = (Map<String, Object>) objectMap.get(0);
        List<Map<String, Object>> objectSteps = (List<Map<String, Object>>) holder.get("steps");
        List<Step> steps = new ArrayList<>();
        for (Map<String, Object> objectStep : objectSteps) {
            steps.add(Step.fromHashMap(objectStep));
        }

        Instruction instruction = new Instruction();
        instruction.steps = steps;
        List<Instruction> result = new ArrayList();
        result.add(instruction);
        return result;
    }

    public static class Step {
        public String step;
        public int number;
        public int recipeId;


        public Step(int number, int recipeId, String step) {
            this.number = number;
            this.recipeId = recipeId;
            this.step = step;
        }

        public static Step fromHashMap(Map<String, Object> objectMap) {
            return new Step(
                    ((Long) objectMap.get("number")).intValue(),
                    ((Long) objectMap.get("recipeId")).intValue(),
                    (String) objectMap.get("step"));
        }
    }
}
