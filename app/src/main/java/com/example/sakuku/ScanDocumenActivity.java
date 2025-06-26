package com.example.sakuku;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ScanDocumentActivity extends AppCompatActivity {

    private Button btnAmount;
    private Button btnDate;
    private Button btnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_document);

        btnAmount = findViewById(R.id.btnAmount);
        btnDate = findViewById(R.id.btnDate);
        btnCategory = findViewById(R.id.btnCategory);

        btnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi untuk mendeteksi jumlah
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi untuk mendeteksi tanggal
            }
        });

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi untuk mendeteksi kategori
            }
        });
    }
}
