package com.example.a41;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditTaskActivity extends BaseNavigationActivity implements RecyclerViewAdapter.clickListener {

    // Member variables
    private TaskDBHandler taskDbHandler; // Database handler for tasks
    private RecyclerView editRecyclerView; // RecyclerView for displaying tasks
    private RecyclerViewAdapter adapter; // Adapter for the RecyclerView
    private Button editButton; // Button for editing a task
    private Button deleteButton; // Button for deleting a task
    private ConstraintLayout editConstraintLayout; // ConstraintLayout for editing task details
    private TextView editTitleTextView; // TextView for editing task title
    private TextView editDescriptionTextView; // TextView for editing task description
    private TextView editDueDateTextView; // TextView for editing task due date
    private Button confirmEditButton; // Button for confirming task edits
    private Button backButton; // Button for going back
    private int selectedPosition; // Position of the selected task
    private List<Task> tasks; // List of tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_edit_task);
        bottomNav.getMenu().findItem(R.id.editTask).setChecked(true); // Set the "Edit Task" item as checked in the bottom navigation view

        taskDbHandler = new TaskDBHandler(EditTaskActivity.this); // Initialize task database handler
        tasks = taskDbHandler.getAllTasks(); // Retrieve all tasks from the database

        // Initialize UI elements
        editConstraintLayout = findViewById(R.id.editContstrainLayout);
        editTitleTextView = findViewById(R.id.editTitleTextView);
        editDescriptionTextView = findViewById(R.id.editDescriptionTextView);
        editDueDateTextView = findViewById(R.id.editDueDateTextView);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        confirmEditButton = findViewById(R.id.confirmEditButton);
        backButton = findViewById(R.id.backButton);

        // Initialize RecyclerView and adapter
        editRecyclerView = findViewById(R.id.editRecyclerView);
        adapter = new RecyclerViewAdapter(tasks, this, this);
        editRecyclerView.setAdapter(adapter);
        editRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Click listener for the "Edit" button
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedPosition = adapter.getCurrentTaskPosition();
                // If a task is selected, populate the edit fields with its details
                if (selectedPosition != -1) {
                    Task selectedTask = tasks.get(selectedPosition);
                    editTitleTextView.setText(selectedTask.getTitle());
                    editDescriptionTextView.setText(selectedTask.getDescription());
                    editDueDateTextView.setText(selectedTask.getDueDate());
                    editRecyclerView.setVisibility(View.GONE);
                    backButton.setVisibility(View.VISIBLE);
                    editConstraintLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(EditTaskActivity.this, "Please select a task to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Click listener for the "Confirm Edit" button
        confirmEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedPosition = adapter.getCurrentTaskPosition();
                String title = editTitleTextView.getText().toString().trim();
                String description = editDescriptionTextView.getText().toString().trim();
                String date = editDueDateTextView.getText().toString().trim();

                // If all fields are filled, update the task details in the database and UI
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(date)) {
                    Task selectedTask = tasks.get(selectedPosition);
                    selectedTask.setTitle(title);
                    selectedTask.setDescription(description);
                    selectedTask.setDueDate(date);
                    taskDbHandler.updateTask(selectedTask);
                    tasks.set(selectedPosition, selectedTask);
                    adapter.notifyItemChanged(selectedPosition);
                    editConstraintLayout.setVisibility(View.GONE);
                    editRecyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(EditTaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Click listener for the "Delete" button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = adapter.getCurrentTaskPosition();
                Task selectedTask = tasks.get(selectedPosition);
                taskDbHandler.deleteTask(selectedTask.getId());
                tasks.remove(selectedPosition);
                adapter.notifyItemRemoved(selectedPosition);
            }
        });

        // Click listener for the "Back" button
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editConstraintLayout.setVisibility(View.GONE);
                editRecyclerView.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.GONE);
            }
        });

        // Click listener for the "Due Date" text view
        editDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Show date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Format selected date and display it in the TextView
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        editDueDateTextView.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    // Listener for RecyclerView item click
    @Override
    public void onClick(int position) {
        adapter.setCurrentTaskPosition(position);
    }
}
