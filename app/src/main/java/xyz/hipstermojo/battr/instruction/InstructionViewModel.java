package xyz.hipstermojo.battr.instruction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class InstructionViewModel extends AndroidViewModel {
    private InstructionRepository instructionRepository;

    public InstructionViewModel(@NonNull Application application) {
        super(application);
        instructionRepository = new InstructionRepository(application);
    }

    public void insertAll(List<Instruction.Step> steps) {
        instructionRepository.insertAll(steps);
    }

    public void deleteRecipeInstructions(int recipeId) {
        instructionRepository.deleteRecipeInstructions(recipeId);
    }

    public LiveData<List<Instruction.Step>> getRecipeInstructions(int recipeId) {
        return instructionRepository.getRecipeInstructions(recipeId);
    }
}
