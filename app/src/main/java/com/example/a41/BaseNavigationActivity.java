package com.example.a41;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseNavigationActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNav;
    protected FrameLayout frameLayout;

    protected void selectNavigationItem(@IdRes int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_navigation);

        bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(navListener);

        frameLayout = findViewById(R.id.frameLayout);
        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null && getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    public void setContentLayout(int layoutResId) {
        LayoutInflater.from(this).inflate(layoutResId, frameLayout, true);
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent = null;

                    if (item.getItemId() == R.id.tasks) {
                        intent = new Intent(BaseNavigationActivity.this, ViewTaskActivity.class);
                    } else if (item.getItemId() == R.id.addTask) {
                        intent = new Intent(BaseNavigationActivity.this, AddTaskActivity.class);
                    } else if (item.getItemId() == R.id.editTask) {
                        intent = new Intent(BaseNavigationActivity.this, EditTaskActivity.class);
                    }

                    if (intent != null) {
                        startActivity(intent);
                        finish();
                        return true;
                    } else {
                        return false;
                    }
                }
            };
}
