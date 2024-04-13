package com.example.a41;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends BaseNavigationActivity {

    // Member variables
    private TextView titleTextView; // TextView for entering task title
    private TextView descriptionTextView; // TextView for entering task description
    private TextView dateTextView; // TextView for selecting task due date
    private Button confirmButton; // Button to confirm task addition
    private TaskDBHandler taskDbHandler; // Database handler for tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_task);
        bottomNav.getMenu().findItem(R.id.addTask).setChecked(true); // Set the "Add Task" item as checked in the bottom navigation view

        // Initialize UI elements
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTextView = findViewById(R.id.dateTextView);
        taskDbHandler = new TaskDBHandler(this);

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String title = titleTextView.getText().toString().trim();
                String description = descriptionTextView.getText().toString().trim();
                String date = dateTextView.getText().toString().trim();

                // Check if all fields are filled
                if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty()) {
                    // Create new task object and add it to the database
                    Task task = new Task(title, description, date);
                    taskDbHandler.addNewTask(task);
                    // Clear input fields
                    titleTextView.setText("");
                    descriptionTextView.setText("");
                    dateTextView.setText("");
                    // Display success message
                    Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Display error message if any field is empty
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for selecting task due date
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Show date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Format selected date and display it in the TextView
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        dateTextView.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show(); // Show the dialog
            }
        });
    }
}
