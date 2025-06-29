package com.example.sakuku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class ScanDocumentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ImageView ivCameraPreview;
    private LinearLayout btnDetectAmount, btnDetectDate, btnDetectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_document);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> finish());

        tabLayout = findViewById(R.id.tab_layout);
        ivCameraPreview = findViewById(R.id.iv_camera_preview);
        btnDetectAmount = findViewById(R.id.btn_detect_amount);
        btnDetectDate = findViewById(R.id.btn_detect_date);
        btnDetectCategory = findViewById(R.id.btn_detect_category);

        tabLayout.addTab(tabLayout.newTab().setText("Struk"));
        tabLayout.addTab(tabLayout.newTab().setText("Tagihan"));
        tabLayout.addTab(tabLayout.newTab().setText("QR Code"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        btnDetectAmount.setOnClickListener(v -> {
            // Simulate scan and go to results
            Intent intent = new Intent(ScanDocumentActivity.this, ScanResultsActivity.class);
            startActivity(intent);
        });

        btnDetectDate.setOnClickListener(v -> {
            // Simulate scan and go to results
            Intent intent = new Intent(ScanDocumentActivity.this, ScanResultsActivity.class);
            startActivity(intent);
        });

        btnDetectCategory.setOnClickListener(v -> {
            // Simulate scan and go to results
            Intent intent = new Intent(ScanDocumentActivity.this, ScanResultsActivity.class);
            startActivity(intent);
        });
    }
}
