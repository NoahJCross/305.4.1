package com.example.a41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Tasks";
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "title";
    private static final String DESCRIPTION_COL = "description";
    private static final String DUEDATE_COL = "duedate";

    public TaskDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public TaskDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT NOT NULL,"
                + DESCRIPTION_COL + " TEXT NOT NULL,"
                + DUEDATE_COL + " TEXT NOT NULL)";
        db.execSQL(query);
    }

    public void addNewTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TITLE_COL, task.getTitle());
        values.put(DESCRIPTION_COL, task.getDescription());
        values.put(DUEDATE_COL, task.getDueDate());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        cursor.close();
        db.close();

        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE_COL, task.getTitle());
        values.put(DESCRIPTION_COL, task.getDescription());
        values.put(DUEDATE_COL, task.getDueDate());

        return db.update(TABLE_NAME, values, ID_COL + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
