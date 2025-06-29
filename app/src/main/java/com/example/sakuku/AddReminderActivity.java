package com.example.sakuku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddReminderActivity extends AppCompatActivity {

    private EditText etReminderTitle, etReminderDate, etReminderTime;
    private Button btnSaveReminder;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        dbHelper = new DatabaseHelper(this);

        etReminderTitle = findViewById(R.id.et_reminder_title);
        etReminderDate = findViewById(R.id.et_reminder_date);
        etReminderTime = findViewById(R.id.et_reminder_time);
        btnSaveReminder = findViewById(R.id.btn_save_reminder);

        btnSaveReminder.setOnClickListener(v -> {
            String title = etReminderTitle.getText().toString();
            String date = etReminderDate.getText().toString();
            String time = etReminderTime.getText().toString();

            if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(AddReminderActivity.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addReminder(title, date, time, 0.0, "Active"); // Default status to Active
                Toast.makeText(AddReminderActivity.this, "Pengingat disimpan: " + title, Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke ReminderActivity
            }
        });
    }
}