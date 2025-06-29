package com.example.sakuku;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ScanResultsActivity extends AppCompatActivity {

    private TextView tvTotal, tvDate, tvMerchant;
    private LinearLayout llTransactionDetails;
    private Spinner spinnerCategory;
    private EditText etNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_results);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> finish());

        tvTotal = findViewById(R.id.tv_total);
        tvDate = findViewById(R.id.tv_date);
        tvMerchant = findViewById(R.id.tv_merchant);
        llTransactionDetails = findViewById(R.id.ll_transaction_details);
        spinnerCategory = findViewById(R.id.spinner_category);
        etNotes = findViewById(R.id.et_notes);

        // Populate dummy data for now
        tvTotal.setText("Rp 120.000");
        tvDate.setText("15 Juli 2023");
        tvMerchant.setText("Restoran Sederhana");

        // Add dummy transaction items
        addTransactionDetail("1x Nasi Goreng", "Rp 35.000");
        addTransactionDetail("2x Es Teh", "Rp 20.000");
        addTransactionDetail("1x Ayam Bakar", "Rp 55.000");

        // Populate spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        etNotes.setText("Makan siang dengan teman kantor");
    }

    private void addTransactionDetail(String item, String amount) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 0);

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(layoutParams);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvItem = new TextView(this);
        tvItem.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        tvItem.setText(item);
        itemLayout.addView(tvItem);

        TextView tvAmount = new TextView(this);
        tvAmount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tvAmount.setText(amount);
        itemLayout.addView(tvAmount);

        llTransactionDetails.addView(itemLayout);
    }
}
