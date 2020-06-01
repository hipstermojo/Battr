package xyz.hipstermojo.battr;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import xyz.hipstermojo.battr.recipe.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private ArrayList<Recipe> recipes;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe current = recipes.get(position);
        holder.textView.setText(current.getTitle());
        Picasso.get()
                .load(current.getImage()).fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recipe_card_image);
            textView = itemView.findViewById(R.id.recipe_card_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position, imageView, textView);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ImageView imageView, TextView textView);
    }
}
