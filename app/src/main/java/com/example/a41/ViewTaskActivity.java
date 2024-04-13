package com.example.a41;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ViewTaskActivity extends BaseNavigationActivity implements RecyclerViewAdapter.clickListener {

    // Member variables
    private TaskDBHandler taskDbHandler; // Database handler for tasks
    private RecyclerView taskRecyclerView; // RecyclerView for displaying tasks
    private RecyclerViewAdapter adapter; // Adapter for the RecyclerView

    private List<Task> tasks; // List of tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_view_task);
        taskDbHandler = new TaskDBHandler(ViewTaskActivity.this); // Initialize task database handler
        tasks = taskDbHandler.getAllTasks(); // Retrieve all tasks from the database

        // Sort tasks by due date
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getDueDate().compareTo(task2.getDueDate());
            }
        });

        // Initialize RecyclerView and adapter
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        adapter = new RecyclerViewAdapter(tasks, this, this);
        taskRecyclerView.setAdapter(adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Method called when a task is clicked
    @Override
    public void onClick(int position) {
        adapter.setCurrentTaskPosition(position);
    }
}
