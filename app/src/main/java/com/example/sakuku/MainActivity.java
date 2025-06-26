package com.example.sakuku;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentBalance, tvIncome, tvExpense;
    private BarChart monthlyChart;
    private LinearLayout cardIncome, cardExpense, cardStats, cardReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        tvIncome = findViewById(R.id.tv_income);
        tvExpense = findViewById(R.id.tv_expense);
        monthlyChart = findViewById(R.id.monthly_chart);

        cardIncome = findViewById(R.id.card_income);
        cardExpense = findViewById(R.id.card_expense);
        cardStats = findViewById(R.id.card_stats);
        cardReminder = findViewById(R.id.card_reminder);

        // Set dummy data
        tvCurrentBalance.setText("Rp 3.250.000");
        tvIncome.setText("+ Rp 5.000.000");
        tvExpense.setText("- Rp 1.750.000");

        setupChart();

        // Set click listeners
        cardIncome.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Tambah Pemasukan", Toast.LENGTH_SHORT).show());
        cardExpense.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Tambah Pengeluaran", Toast.LENGTH_SHORT).show());
        cardStats.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Lihat Statistik", Toast.LENGTH_SHORT).show());
        cardReminder.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Pengingat", Toast.LENGTH_SHORT).show());
    }

    private void setupChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 40));
        entries.add(new BarEntry(5, 60));
        entries.add(new BarEntry(10, 120));
        entries.add(new BarEntry(15, 30));
        entries.add(new BarEntry(20, 80));
        entries.add(new BarEntry(25, 140));
        entries.add(new BarEntry(30, 50));

        BarDataSet dataSet = new BarDataSet(entries, "Pengeluaran");
        dataSet.setColor(getResources().getColor(R.color.primary_purple));
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

        monthlyChart.invalidate(); // refresh
    }
}
