package com.example.sakuku;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDate, etNotes;
    private Button btnSaveTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etAmount = findViewById(R.id.et_amount);
        etDate = findViewById(R.id.et_date);
        etNotes = findViewById(R.id.et_notes);
        btnSaveTransaction = findViewById(R.id.btn_save_transaction);

        btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });
    }

    private void saveTransaction() {
        String amount = etAmount.getText().toString();
        String date = etDate.getText().toString();
        String notes = etNotes.getText().toString();

        // Logic to save the transaction (e.g., to a database)

        Toast.makeText(this, "Transaksi disimpan", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }
}