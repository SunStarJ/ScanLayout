package com.toyor.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sunstar.scanlayout.ScanLayout;

public class MainActivity extends AppCompatActivity {

    private ScanLayout scanLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanLayout = findViewById(R.id.scanLayout);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLayout.startScan();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLayout.stopScan();
            }
        });
    }
}
