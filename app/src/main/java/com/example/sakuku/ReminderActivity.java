package com.example.sakuku;

import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import java.util.List;
import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    private RecyclerView reminderRecyclerView;
    private Button addReminderButton;
    private DatabaseHelper dbHelper;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        reminderRecyclerView = findViewById(R.id.reminderRecyclerView);
        addReminderButton = findViewById(R.id.addReminderButton);

        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(reminderList);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderRecyclerView.setAdapter(reminderAdapter);

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReminders();
    }

    private void loadReminders() {
        try {
            reminderList.clear();
            reminderList.addAll(dbHelper.getAllReminders());
            reminderAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading reminders: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
