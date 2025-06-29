package com.example.sakuku;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDate, etNotes;
    private Button btnSaveTransaction;
    private DatabaseHelper dbHelper;
    private String transactionType; // "income" or "expense"
    private String selectedCategory = ""; // To store the selected category

    private CardView cardFood, cardTransport, cardShopping, cardEntertainment, cardHome, cardOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        dbHelper = new DatabaseHelper(this);

        transactionType = getIntent().getStringExtra("transaction_type");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivBack = findViewById(R.id.iv_back);
        ImageView ivClose = findViewById(R.id.iv_close);
        TextView tvTitle = findViewById(R.id.tv_title);

        if (transactionType.equals("income")) {
            tvTitle.setText("Tambah Pemasukan");
        } else {
            tvTitle.setText("Tambah Pengeluaran");
        }

        ivBack.setOnClickListener(v -> finish());
        ivClose.setOnClickListener(v -> finish());

        etAmount = findViewById(R.id.et_amount);
        etDate = findViewById(R.id.et_date);
        etNotes = findViewById(R.id.et_notes);
        btnSaveTransaction = findViewById(R.id.btn_save_transaction);

        // Category Cards
        cardFood = findViewById(R.id.card_food);
        // Initialize other category cards here

        // Set current date to etDate
        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy"; // In which you want to display format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));

        etDate.setOnClickListener(v -> {
            new DatePickerDialog(AddTransactionActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(sdf.format(myCalendar.getTime()));
                    }, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        cardFood.setOnClickListener(v -> {
            selectedCategory = "Food";
            Toast.makeText(AddTransactionActivity.this, "Kategori: Makanan", Toast.LENGTH_SHORT).show();
        });

        // Set listeners for other category cards here

        btnSaveTransaction.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString();
            String date = etDate.getText().toString();
            String notes = etNotes.getText().toString();

            if (amountStr.isEmpty() || date.isEmpty() || notes.isEmpty() || selectedCategory.isEmpty()) {
                Toast.makeText(AddTransactionActivity.this, "Harap isi semua kolom dan pilih kategori", Toast.LENGTH_SHORT).show();
            } else {
                double amount = Double.parseDouble(amountStr);
                long result = dbHelper.addTransaction(transactionType, amount, selectedCategory, date, notes);
                if (result != -1) {
                    Toast.makeText(AddTransactionActivity.this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddTransactionActivity.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
