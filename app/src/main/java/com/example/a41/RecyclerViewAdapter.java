package com.example.a41;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;
    private clickListener listener;
    private int currentTaskPosition = -1;
    public RecyclerViewAdapter(List<Task> tasks, Context context, clickListener listener){
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
    }

    public interface clickListener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) parent).getLayoutManager();
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_title, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.taskTextView.setText(tasks.get(position).getTitle());
        holder.taskDescriptionTextView.setText(tasks.get(position).getDescription());
        holder.taskDateTextView.setText(tasks.get(position).getDueDate());
        if (position == currentTaskPosition) {
            holder.taskTextView.setBackgroundResource(R.drawable.text_underline);
            holder.taskDateTextView.setVisibility(View.VISIBLE);
            holder.taskDescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            holder.taskTextView.setBackgroundResource(0);
            holder.taskDateTextView.setVisibility(View.GONE);
            holder.taskDescriptionTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    public void setCurrentTaskPosition(int position) {
        int previousPosition = currentTaskPosition;
        if(position != currentTaskPosition){
            currentTaskPosition = position;
            notifyItemChanged(currentTaskPosition);
        }else {
            currentTaskPosition = -1;
        }
        notifyItemChanged(previousPosition);
    }

    public int getCurrentTaskPosition() {
        return currentTaskPosition;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView taskTextView;
        private TextView taskDescriptionTextView;
        private TextView taskDateTextView;
        private clickListener clickListener;

        public ViewHolder(@NonNull View itemView, clickListener listener) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTitleTextView);
            taskDescriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            taskDateTextView = itemView.findViewById(R.id.taskDateTextView);
            this.clickListener = listener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onClick(position);
        }
    }
}
