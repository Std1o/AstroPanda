package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Q9Activity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q9);

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(95);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, CompleteActivity.class));
    }
}
