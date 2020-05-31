package xyz.hipstermojo.battr.instruction;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.hipstermojo.battr.recipe.RecipeDatabase;

public class InstructionRepository {
    private InstructionDao instructionDao;

    public InstructionRepository(Application application) {
        instructionDao = RecipeDatabase.getInstance(application).instructionDao();
    }

    public void insertAll(List<Instruction.Step> instructionSteps) {
        new InsertAllInstructionsAsyncTask(instructionDao).execute(instructionSteps);
    }

    public void deleteRecipeInstructions(int recipeId) {
        new DeleteRecipeInstructionsAsyncTask(instructionDao).execute(recipeId);
    }

    public LiveData<List<Instruction.Step>> getRecipeInstructions(int recipeId){
        return instructionDao.getRecipeInstructions(recipeId);
    }

    private static class InsertAllInstructionsAsyncTask extends AsyncTask<List<Instruction.Step>, Void, Void> {
        private InstructionDao instructionDao;

        private InsertAllInstructionsAsyncTask(InstructionDao instructionDao) {
            this.instructionDao = instructionDao;
        }


        @Override
        protected Void doInBackground(List<Instruction.Step>... steps) {
            instructionDao.insertAll(steps[0]);
            return null;
        }
    }

    private static class DeleteRecipeInstructionsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private InstructionDao instructionDao;

        private DeleteRecipeInstructionsAsyncTask(InstructionDao instructionDao) {
            this.instructionDao = instructionDao;
        }

        @Override
        protected Void doInBackground(Integer... recipeIds) {
            instructionDao.deleteRecipeInstructions(recipeIds[0]);
            return null;
        }
    }
}
