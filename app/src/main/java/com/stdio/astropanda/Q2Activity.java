package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Q2Activity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q2);

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(23);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, Q3Activity.class));
    }
}
