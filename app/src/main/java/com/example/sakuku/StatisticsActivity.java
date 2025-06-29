package com.example.sakuku;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class StatisticsActivity extends AppCompatActivity {

    private LineChart lineChart;
    private RecyclerView rvCategoryStats;
    private Button btnMonthly, btnWeekly, btnYearly;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        lineChart = findViewById(R.id.line_chart);
        rvCategoryStats = findViewById(R.id.rv_category_stats);
        btnMonthly = findViewById(R.id.btn_monthly);
        btnWeekly = findViewById(R.id.btn_weekly);
        btnYearly = findViewById(R.id.btn_yearly);

        rvCategoryStats.setLayoutManager(new LinearLayoutManager(this));

        btnMonthly.setOnClickListener(v -> loadChartData("monthly"));
        btnWeekly.setOnClickListener(v -> loadChartData("weekly"));
        btnYearly.setOnClickListener(v -> loadChartData("yearly"));

        loadChartData("monthly");
        loadCategoryStats("monthly"); // Initial load for category stats
    }

    private void loadChartData(String filter) {
        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        Map<String, Double> incomeData = new HashMap<>();
        Map<String, Double> expenseData = new HashMap<>();

        if (filter.equals("monthly")) {
            incomeData = dbHelper.getMonthlyIncome();
            expenseData = dbHelper.getMonthlyExpense();
        } else if (filter.equals("weekly")) {
            incomeData = dbHelper.getWeeklyIncome();
            expenseData = dbHelper.getWeeklyExpense();
        } else if (filter.equals("yearly")) {
            incomeData = dbHelper.getYearlyIncome();
            expenseData = dbHelper.getYearlyExpense();
        }

        int i = 0;
        for (Map.Entry<String, Double> entry : incomeData.entrySet()) {
            incomeEntries.add(new Entry(i++, entry.getValue().floatValue()));
        }

        i = 0;
        for (Map.Entry<String, Double> entry : expenseData.entrySet()) {
            expenseEntries.add(new Entry(i++, entry.getValue().floatValue()));
        }

        if (incomeEntries.isEmpty() && expenseEntries.isEmpty()) {
            lineChart.clear();
            lineChart.setNoDataText("Tidak ada data untuk ditampilkan.");
            lineChart.invalidate();
            return;
        }

        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Pemasukan");
        incomeDataSet.setColor(ContextCompat.getColor(this, R.color.income_green));
        incomeDataSet.setCircleColor(ContextCompat.getColor(this, R.color.income_green));
        incomeDataSet.setDrawValues(false);

        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Pengeluaran");
        expenseDataSet.setColor(ContextCompat.getColor(this, R.color.expense_red));
        expenseDataSet.setCircleColor(ContextCompat.getColor(this, R.color.expense_red));
        expenseDataSet.setDrawValues(false);

        LineData lineData = new LineData(incomeDataSet, expenseDataSet);
        lineChart.setData(lineData);

        // Configure XAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}));

        // Configure YAxis
        lineChart.getAxisRight().setEnabled(false); // Disable right Y-axis
        lineChart.getAxisLeft().setGranularity(1f);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.invalidate(); // refresh
    }

    private void loadCategoryStats(String filter) {
        List<CategoryStat> categoryStats = new ArrayList<>();
        Map<String, Double> expenseByCategory = new HashMap<>();

        if (filter.equals("monthly")) {
            expenseByCategory = dbHelper.getMonthlyExpenseByCategory();
        } else if (filter.equals("weekly")) {
            expenseByCategory = dbHelper.getWeeklyExpenseByCategory();
        } else if (filter.equals("yearly")) {
            expenseByCategory = dbHelper.getYearlyExpenseByCategory();
        }

        double totalExpense = 0;
        for (Double amount : expenseByCategory.values()) {
            totalExpense += amount;
        }

        for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            int percentage = (int) ((amount / totalExpense) * 100);
            categoryStats.add(new CategoryStat(category, percentage));
        }

        CategoryStatsAdapter adapter = new CategoryStatsAdapter(categoryStats);
        rvCategoryStats.setAdapter(adapter);
    }
}
