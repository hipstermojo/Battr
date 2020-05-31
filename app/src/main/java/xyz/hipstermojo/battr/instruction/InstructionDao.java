package xyz.hipstermojo.battr.instruction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InstructionDao {
    @Insert
    void insertAll(List<Instruction.Step> steps);

    @Query("DELETE FROM steps WHERE recipeId = :recipeId")
    void deleteRecipeInstructions(int recipeId);

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY number")
    LiveData<List<Instruction.Step>> getRecipeInstructions(int recipeId);
}
