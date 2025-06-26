package com.example.sakuku;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    private TextView saldoTextView;
    private Button bulananButton;
    private Button mingguanButton;
    private Button tahunanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        saldoTextView = findViewById(R.id.saldoTextView);
        bulananButton = findViewById(R.id.bulananButton);
        mingguanButton = findViewById(R.id.mingguanButton);
        tahunanButton = findViewById(R.id.tahunanButton);

        // Set button click listeners
        bulananButton.setOnClickListener(view -> showMonthlyStatistics());
        mingguanButton.setOnClickListener(view -> showWeeklyStatistics());
        tahunanButton.setOnClickListener(view -> showYearlyStatistics());
    }

    private void showMonthlyStatistics() {
        // Logic to display monthly statistics
    }

    private void showWeeklyStatistics() {
        // Logic to display weekly statistics
    }

    private void showYearlyStatistics() {
        // Logic to display yearly statistics
    }
}