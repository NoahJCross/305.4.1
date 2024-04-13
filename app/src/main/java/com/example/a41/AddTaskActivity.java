package com.example.a41;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends BaseNavigationActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private Button confirmButton;
    private TaskDBHandler taskDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_add_task);
        bottomNav.getMenu().findItem(R.id.addTask).setChecked(true);


        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTextView = findViewById(R.id.dateTextView);
        taskDbHandler = new TaskDBHandler(this);

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String title = titleTextView.getText().toString().trim();
                String description = descriptionTextView.getText().toString().trim();
                String date = dateTextView.getText().toString().trim();

                if (!title.isEmpty() && !description.isEmpty() && !date.isEmpty()) {
                    Task task = new Task(title, description, date);
                    taskDbHandler.addNewTask(task);
                    titleTextView.setText("");
                    descriptionTextView.setText("");
                    dateTextView.setText("");
                    Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        dateTextView.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });


    }
}