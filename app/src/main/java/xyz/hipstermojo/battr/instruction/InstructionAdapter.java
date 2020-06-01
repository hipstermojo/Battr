package xyz.hipstermojo.battr.instruction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.hipstermojo.battr.R;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>{
    private List<Instruction.Step> steps;
    private Context context;
    public InstructionAdapter(Context context){
        this.context = context;
        this.steps = new ArrayList<>();
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.instruction_item,parent,false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        Instruction.Step step = steps.get(position);
        holder.textView.setText(step.step);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class InstructionViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.instruction_item_step_content);
        }
    }

    public void setSteps(List<Instruction.Step> steps) {
        this.steps = steps;
    }
}
