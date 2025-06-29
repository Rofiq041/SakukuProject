package com.example.sakuku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticActivity extends AppCompatActivity {

    private TextView saldoTextView;
    private Button bulananButton, mingguanButton, tahunanButton;
    private TextView foodTextView, transportTextView, shoppingTextView, entertainmentTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        dbHelper = new DatabaseHelper(this);

        saldoTextView = findViewById(R.id.saldoTextView);
        bulananButton = findViewById(R.id.bulananButton);
        mingguanButton = findViewById(R.id.mingguanButton);
        tahunanButton = findViewById(R.id.tahunanButton);
        foodTextView = findViewById(R.id.foodTextView);
        transportTextView = findViewById(R.id.transportTextView);
        shoppingTextView = findViewById(R.id.shoppingTextView);
        entertainmentTextView = findViewById(R.id.entertainmentTextView);

        updateStatistics("monthly"); // Default to monthly statistics

        bulananButton.setOnClickListener(v -> updateStatistics("monthly"));
        mingguanButton.setOnClickListener(v -> updateStatistics("weekly"));
        tahunanButton.setOnClickListener(v -> updateStatistics("yearly"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics("monthly"); // Refresh statistics on resume
    }

    private void updateStatistics(String filter) {
        double currentBalance = dbHelper.getCurrentBalance();
        saldoTextView.setText(String.format("Rp %.0f", currentBalance));

        double totalExpense = dbHelper.getTotalExpense(filter);

        double foodExpense = dbHelper.getTotalExpenseByCategory("Food", filter);
        double transportExpense = dbHelper.getTotalExpenseByCategory("Transport", filter);
        double shoppingExpense = dbHelper.getTotalExpenseByCategory("Shopping", filter);
        double entertainmentExpense = dbHelper.getTotalExpenseByCategory("Entertainment", filter);

        // Calculate percentages
        int foodPercentage = (totalExpense > 0) ? (int) ((foodExpense / totalExpense) * 100) : 0;
        int transportPercentage = (totalExpense > 0) ? (int) ((transportExpense / totalExpense) * 100) : 0;
        int shoppingPercentage = (totalExpense > 0) ? (int) ((shoppingExpense / totalExpense) * 100) : 0;
        int entertainmentPercentage = (totalExpense > 0) ? (int) ((entertainmentExpense / totalExpense) * 100) : 0;

        foodTextView.setText(String.format("Makanan - %d%%", foodPercentage));
        transportTextView.setText(String.format("Transportasi - %d%%", transportPercentage));
        shoppingTextView.setText(String.format("Belanja - %d%%", shoppingPercentage));
        entertainmentTextView.setText(String.format("Hiburan - %d%%", entertainmentPercentage));
    }
}
