package com.example.sakuku;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchTransactionActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnToday, btnThisWeek, btnThisMonth, btnIncomeFilter, btnExpenseFilter;
    private CardView cardFood, cardTransport, cardShopping, cardEntertainment, cardHome, cardOther;
    private RecyclerView rvSearchResults;
    private DatabaseHelper dbHelper;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;

    private String currentFilterType = "all"; // "all", "income", "expense"
    private String currentFilterTime = "all"; // "all", "today", "this_week", "this_month"
    private String currentFilterCategory = "all"; // "all", "Food", etc.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transaction);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        etSearch = findViewById(R.id.et_search);
        btnToday = findViewById(R.id.btn_today);
        btnThisWeek = findViewById(R.id.btn_this_week);
        btnThisMonth = findViewById(R.id.btn_this_month);
        btnIncomeFilter = findViewById(R.id.btn_income_filter);
        btnExpenseFilter = findViewById(R.id.btn_expense_filter);
        cardFood = findViewById(R.id.card_food);
        // Initialize other category cards here

        rvSearchResults = findViewById(R.id.rv_search_results);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionList);
        rvSearchResults.setAdapter(transactionAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTransactions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnToday.setOnClickListener(v -> {
            currentFilterTime = "today";
            searchTransactions(etSearch.getText().toString());
        });

        btnThisWeek.setOnClickListener(v -> {
            currentFilterTime = "this_week";
            searchTransactions(etSearch.getText().toString());
        });

        btnThisMonth.setOnClickListener(v -> {
            currentFilterTime = "this_month";
            searchTransactions(etSearch.getText().toString());
        });

        btnIncomeFilter.setOnClickListener(v -> {
            currentFilterType = "income";
            searchTransactions(etSearch.getText().toString());
        });

        btnExpenseFilter.setOnClickListener(v -> {
            currentFilterType = "expense";
            searchTransactions(etSearch.getText().toString());
        });

        cardFood.setOnClickListener(v -> {
            currentFilterCategory = "Food";
            searchTransactions(etSearch.getText().toString());
        });

        // Initial load
        searchTransactions("");
    }

    private void searchTransactions(String query) {
        transactionList.clear();
        Cursor cursor = null;

        // Get all transactions first, then filter in memory
        cursor = dbHelper.getAllTransactions();

        List<Transaction> tempTransactionList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int typeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE);
                int amountIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT);
                int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
                int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
                int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);

                if (idIndex != -1 && typeIndex != -1 && amountIndex != -1 && dateIndex != -1 && descriptionIndex != -1 && categoryIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String type = cursor.getString(typeIndex);
                    double amount = cursor.getDouble(amountIndex);
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(descriptionIndex);
                    String category = cursor.getString(categoryIndex);
                    tempTransactionList.add(new Transaction(id, type, amount, date, description, category));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        List<Transaction> filteredList = new ArrayList<>();
        for (Transaction transaction : tempTransactionList) {
            boolean matches = true;

            // Filter by query
            if (!query.isEmpty() &&
                    !(transaction.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                            transaction.getCategory().toLowerCase().contains(query.toLowerCase()))) {
                matches = false;
            }

            // Filter by type
            if (!currentFilterType.equals("all") && !transaction.getType().equals(currentFilterType)) {
                matches = false;
            }

            // Filter by time
            if (!currentFilterTime.equals("all")) {
                if (currentFilterTime.equals("today") && !isToday(transaction.getDate())) {
                    matches = false;
                } else if (currentFilterTime.equals("this_week") && !isThisWeek(transaction.getDate())) {
                    matches = false;
                } else if (currentFilterTime.equals("this_month") && !isThisMonth(transaction.getDate())) {
                    matches = false;
                }
            }

            // Filter by category
            if (!currentFilterCategory.equals("all") && !transaction.getCategory().equals(currentFilterCategory)) {
                matches = false;
            }

            if (matches) {
                filteredList.add(transaction);
            }
        }

        transactionList.clear();
        transactionList.addAll(filteredList);
        transactionAdapter.updateData(transactionList);
    }

    // Helper methods for date filtering
    private boolean isToday(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateString);
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            Calendar calToday = Calendar.getInstance();

            return calDate.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) &&
                    calDate.get(Calendar.MONTH) == calToday.get(Calendar.MONTH) &&
                    calDate.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isThisWeek(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateString);
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            Calendar calToday = Calendar.getInstance();

            return calDate.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) &&
                    calDate.get(Calendar.WEEK_OF_YEAR) == calToday.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isThisMonth(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateString);
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            Calendar calToday = Calendar.getInstance();

            return calDate.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) &&
                    calDate.get(Calendar.MONTH) == calToday.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
