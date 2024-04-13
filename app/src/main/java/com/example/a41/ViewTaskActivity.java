package com.example.a41;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewTaskActivity extends BaseNavigationActivity implements RecyclerViewAdapter.clickListener  {

    private TaskDBHandler taskDbHandler;
    private RecyclerView taskRecyclerView;
    private RecyclerViewAdapter adapter;

    private List<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_view_task);
        taskDbHandler = new TaskDBHandler(ViewTaskActivity.this);
        tasks = taskDbHandler.getAllTasks();

        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getDueDate().compareTo(task2.getDueDate());
            }
        });

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        adapter = new RecyclerViewAdapter(tasks, this, this);
        taskRecyclerView.setAdapter(adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(int position) {
        adapter.setCurrentTaskPosition(position);
    }
}