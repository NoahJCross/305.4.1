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

    // Constructor to initialize the adapter with tasks, context, and click listener
    public RecyclerViewAdapter(List<Task> tasks, Context context, clickListener listener){
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
    }

    // Interface for click listener
    public interface clickListener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each task item
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_title, parent, false);
        // Creating a new ViewHolder instance
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Binding data to the ViewHolder
        holder.taskTextView.setText(tasks.get(position).getTitle());
        holder.taskDescriptionTextView.setText(tasks.get(position).getDescription());
        holder.taskDateTextView.setText(tasks.get(position).getDueDate());

        // Displaying additional details for the selected task
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

    // Method to set the current selected task position
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

    // Method to get the current selected task position
    public int getCurrentTaskPosition() {
        return currentTaskPosition;
    }

    // ViewHolder class to hold and manage the task item views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView taskTextView;
        private TextView taskDescriptionTextView;
        private TextView taskDateTextView;
        private clickListener clickListener;

        // Constructor to initialize the ViewHolder
        public ViewHolder(@NonNull View itemView, clickListener listener) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTitleTextView);
            taskDescriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            taskDateTextView = itemView.findViewById(R.id.taskDateTextView);
            this.clickListener = listener;
            // Setting click listener on the item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Getting the position of the clicked item and invoking the click listener
            int position = getAdapterPosition();
            clickListener.onClick(position);
        }
    }
}
