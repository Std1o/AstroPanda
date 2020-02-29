package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Q1Activity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(12);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, Q2Activity.class));
    }
}
