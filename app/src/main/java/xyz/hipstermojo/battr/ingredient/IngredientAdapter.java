package xyz.hipstermojo.battr.ingredient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.R;

import static xyz.hipstermojo.battr.Utils.formatAmount;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;
    private Context context;

    public IngredientAdapter(Context context) {
        this.context = context;
        this.ingredients = new ArrayList<>();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = this.ingredients.get(position);
        holder.ingredientNameView.setText(ingredient.name);
        holder.ingredientAmountView.setText(String.format("%s %s", formatAmount(ingredient.amount), ingredient.unit));
        Drawable warningDrawable;

        if (ingredient.aisle != null) {
            if (ingredient.aisle.toLowerCase().contains("nuts")) {
                warningDrawable = context.getResources().getDrawable(R.drawable.nuts_indicator);
                holder.ingredientWarningView.setBackground(warningDrawable);
            } else if (ingredient.aisle.toLowerCase().contains("seafood")) {
                warningDrawable = context.getResources().getDrawable(R.drawable.seafood_indicator);
                holder.ingredientWarningView.setBackground(warningDrawable);
            } else if (ingredient.aisle.toLowerCase().contains("dairy") || ingredient.aisle.toLowerCase().contains("cheese")) {
                warningDrawable = context.getResources().getDrawable(R.drawable.milk_indicator);
                holder.ingredientWarningView.setBackground(warningDrawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameView;
        public TextView ingredientAmountView;
        public TextView ingredientWarningView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientNameView = itemView.findViewById(R.id.ingredient_item_name);
            ingredientAmountView = itemView.findViewById(R.id.ingredient_item_amount);
            ingredientWarningView = itemView.findViewById(R.id.ingredient_item_warning);
        }
    }
}
