package com.example.sakuku;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchTransactionActivity extends AppCompatActivity {

    private EditText searchEditText;
    private EditText searchResultEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchResultEditText = findViewById(R.id.searchResultEditText);

        Button todayButton = findViewById(R.id.todayButton);
        Button weekButton = findViewById(R.id.weekButton);
        Button monthButton = findViewById(R.id.monthButton);
        Button incomeButton = findViewById(R.id.incomeButton);
        Button expenseButton = findViewById(R.id.expenseButton);
        Button foodButton = findViewById(R.id.foodButton);
        Button transportButton = findViewById(R.id.transportButton);
        Button shoppingButton = findViewById(R.id.shoppingButton);
        Button entertainmentButton = findViewById(R.id.entertainmentButton);

        // Implement button click listeners if necessary
        todayButton.setOnClickListener(view -> filterByToday());
        weekButton.setOnClickListener(view -> filterByWeek());
        monthButton.setOnClickListener(view -> filterByMonth());
        expenseButton.setOnClickListener(view -> filterByExpense());
        incomeButton.setOnClickListener(view -> filterByIncome());
        // Add similar listeners for category buttons
    }

    private void filterByToday() {
        // Implement filtering logic
    }

    private void filterByWeek() {
        // Implement filtering logic
    }

    private void filterByMonth() {
        // Implement filtering logic
    }

    private void filterByIncome() {
        // Implement filtering logic
    }

    private void filterByExpense() {
        // Implement filtering logic
    }
}