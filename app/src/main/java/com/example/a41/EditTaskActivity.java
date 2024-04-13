package com.example.a41;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class EditTaskActivity extends BaseNavigationActivity implements RecyclerViewAdapter.clickListener  {

    private TaskDBHandler taskDbHandler;
    private RecyclerView editRecyclerView;
    private RecyclerViewAdapter adapter;
    private Button editButton;
    private Button deleteButton;
    private ConstraintLayout editConstraintLayout;
    private TextView editTitleTextView;
    private TextView editDescriptionTextView;
    private TextView editDueDateTextView;
    private Button confirmEditButton;
    private Button backButton;
    private int selectedPosition;

    private List<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_edit_task);
        bottomNav.getMenu().findItem(R.id.editTask).setChecked(true);

        taskDbHandler = new TaskDBHandler(EditTaskActivity.this);
        tasks = taskDbHandler.getAllTasks();

        editConstraintLayout = findViewById(R.id.editContstrainLayout);
        editTitleTextView = findViewById(R.id.editTitleTextView);
        editDescriptionTextView = findViewById(R.id.editDescriptionTextView);
        editDueDateTextView = findViewById(R.id.editDueDateTextView);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        confirmEditButton = findViewById(R.id.confirmEditButton);
        backButton = findViewById(R.id.backButton);



        editRecyclerView = findViewById(R.id.editRecyclerView);
        adapter = new RecyclerViewAdapter(tasks, this, this);
        editRecyclerView.setAdapter(adapter);
        editRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedPosition = adapter.getCurrentTaskPosition();
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
        confirmEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedPosition = adapter.getCurrentTaskPosition();
                String title = editTitleTextView.getText().toString().trim();
                String description = editDescriptionTextView.getText().toString().trim();
                String date = editDueDateTextView.getText().toString().trim();

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
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editConstraintLayout.setVisibility(View.GONE);
                editRecyclerView.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.GONE);
            }
        });
        editDueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        editDueDateTextView.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onClick(int position) {
        adapter.setCurrentTaskPosition(position);
    }
}