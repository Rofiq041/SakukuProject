package com.example.sakuku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentBalance, tvIncome, tvExpense;
    private BarChart monthlyChart;
    private DatabaseHelper dbHelper;
    private RecyclerView rvTransactions;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bind views
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        tvIncome = findViewById(R.id.tv_income);
        tvExpense = findViewById(R.id.tv_expense);
        monthlyChart = findViewById(R.id.monthly_chart);
        rvTransactions = findViewById(R.id.rv_transactions);
        tabLayout = findViewById(R.id.tab_layout);

        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList);
        rvTransactions.setAdapter(transactionAdapter);

        updateFinancialSummary();
        setupChart();
        setupTabs();
        loadTransactions("all");

        // Action Buttons
        LinearLayout btnIncome = findViewById(R.id.btn_income);
        LinearLayout btnExpense = findViewById(R.id.btn_expense);
        LinearLayout btnStats = findViewById(R.id.btn_stats);
        LinearLayout btnReminder = findViewById(R.id.btn_reminder);

        btnIncome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            intent.putExtra("transaction_type", "income");
            startActivity(intent);
        });

        btnExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            intent.putExtra("transaction_type", "expense");
            startActivity(intent);
        });

        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
            startActivity(intent);
        });

        btnReminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home screen
            } else if (itemId == R.id.nav_report) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            } else if (itemId == R.id.nav_bill) {
                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
            return true;
        });

        // FAB
        FloatingActionButton fabScan = findViewById(R.id.fab_scan);
        fabScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanDocumentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFinancialSummary();
        setupChart();
        loadTransactions(getSelectedTab());
    }

    private void updateFinancialSummary() {
        double currentBalance = dbHelper.getCurrentBalance();
        double totalIncome = dbHelper.getTotalIncome();
        double totalExpense = dbHelper.getTotalExpense();

        tvCurrentBalance.setText(String.format("Rp %.0f", currentBalance));
        tvIncome.setText(String.format("Rp %.0f", totalIncome));
        tvExpense.setText(String.format("Rp %.0f", totalExpense));
    }

    private void setupChart() {
        List<BarEntry> entries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM/yyyy", Locale.US);
        String currentMonthYear = monthYearFormat.format(calendar.getTime());

        Cursor cursor = dbHelper.getTransactionsByTypeAndMonth("expense", currentMonthYear);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
                int amountIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT);

                if (dateIndex != -1 && amountIndex != -1) {
                    String dateStr = cursor.getString(dateIndex);
                    double amount = cursor.getDouble(amountIndex);

                    try {
                        int day = Integer.parseInt(dateStr.substring(0, 2));
                        entries.add(new BarEntry(day, (float) amount));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        BarDataSet dataSet = new BarDataSet(entries, "Pengeluaran");
        dataSet.setColor(ContextCompat.getColor(this, R.color.primary_purple));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(2f);

        monthlyChart.setData(barData);
        monthlyChart.getDescription().setEnabled(false);
        monthlyChart.getLegend().setEnabled(false);

        XAxis xAxis = monthlyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        monthlyChart.getAxisRight().setEnabled(false);
        monthlyChart.getAxisLeft().setGranularity(20f);

        monthlyChart.invalidate();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Semua"));
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"));
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadTransactions(getSelectedTab());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void loadTransactions(String type) {
        transactionList.clear();
        Cursor cursor;
        if (type.equals("all")) {
            cursor = dbHelper.getAllTransactions();
        } else {
            cursor = dbHelper.getTransactionsByType(type);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int typeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE);
                int amountIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
                int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);

                if (idIndex != -1 && typeIndex != -1 && amountIndex != -1 && dateIndex != -1 && descriptionIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String transactionType = cursor.getString(typeIndex);
                    double amount = cursor.getDouble(amountIndex);
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(descriptionIndex);
                    String category = ""; // Placeholder for category
                    transactionList.add(new Transaction(id, transactionType, amount, date, description, category));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        transactionAdapter.updateData(transactionList);
    }

    private String getSelectedTab() {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        if (selectedTabPosition == 1) {
            return "income";
        } else if (selectedTabPosition == 2) {
            return "expense";
        } else {
            return "all";
        }
    }
}
