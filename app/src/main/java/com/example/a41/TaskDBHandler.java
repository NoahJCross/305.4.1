package com.example.a41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class TaskDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Tasks";
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "title";
    private static final String DESCRIPTION_COL = "description";
    private static final String DUEDATE_COL = "duedate";

    // Constructor
    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // onCreate method called when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the Tasks table
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT NOT NULL,"
                + DESCRIPTION_COL + " TEXT NOT NULL,"
                + DUEDATE_COL + " TEXT NOT NULL)";
        // Execute the query
        db.execSQL(query);
    }

    // Method to add a new task to the database
    public void addNewTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // Put task details into ContentValues
        values.put(TITLE_COL, task.getTitle());
        values.put(DESCRIPTION_COL, task.getDescription());
        values.put(DUEDATE_COL, task.getDueDate());

        // Insert task into the database
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    // Method to get all tasks from the database
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Iterate through the cursor and add tasks to the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ID_COL)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_COL)));
                task.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(DUEDATE_COL)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // Close cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return taskList;
    }

    // Method to update a task in the database
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE_COL, task.getTitle());
        values.put(DESCRIPTION_COL, task.getDescription());
        values.put(DUEDATE_COL, task.getDueDate());

        // Update the task in the database
        return db.update(TABLE_NAME, values, ID_COL + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    // Method to delete a task from the database
    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the task with the specified ID
        db.delete(TABLE_NAME, ID_COL + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
    }

    // onUpgrade method called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
