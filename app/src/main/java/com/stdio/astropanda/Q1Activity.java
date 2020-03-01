package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Q1Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    boolean isFirstClick = true;
    ArrayList<String> list = new ArrayList<>();
    TextView tvMoney;
    SharedPreferences prefs;
    boolean nextIsAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        SharedPreferences languagePref = getSharedPreferences("languagePref", MODE_PRIVATE);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(GetVipActivity.getLanguageFromPosition(languagePref
                .getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry())))));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_q1);

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(12);

        tvMoney = findViewById(R.id.tvMoney);
        prefs = getSharedPreferences("moneyPref", MODE_PRIVATE);
        tvMoney.setText(prefs.getInt("moneyCount", 0) + "");

        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentPagePref.edit();
        editor.putString("currentPage", getClass().getSimpleName());
        editor.apply();


        CheckBox redCheckBox = findViewById(R.id.CheckBox1);
        redCheckBox.setOnClickListener(CheckBoxClickListener);

        CheckBox greenCheckBox = findViewById(R.id.CheckBox2);
        greenCheckBox.setOnClickListener(CheckBoxClickListener);

        CheckBox blueCheckBox = findViewById(R.id.CheckBox3);
        blueCheckBox.setOnClickListener(CheckBoxClickListener);

        CheckBox fourthCheckBox = findViewById(R.id.CheckBox4);
        fourthCheckBox.setOnClickListener(CheckBoxClickListener);
    }

    View.OnClickListener CheckBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox rb = (CheckBox)v;
            if (isFirstClick) {
                MainActivity.message += "\n\nОтметьте что из этого вас интересует и чем бы вы пользовались - ";
            }
            isFirstClick = false;
            if (rb.isChecked()) {
                if (!list.contains(rb.getText().toString())) {
                    list.add(rb.getText().toString());
                }
            }
            else {
                list.remove(rb.getText().toString());
            }
            if (!list.isEmpty()) {
                nextIsAllowed = true;
            }
            else {
                nextIsAllowed = false;
            }
        }
    };

    public void onClick(View view) {
        if (nextIsAllowed) {
            for (String s : list) {
                MainActivity.message += s + ", ";
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 110);
            editor.apply();
            startActivity(new Intent(this, Q2Activity.class));
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.err_no_selected), Toast.LENGTH_SHORT).show();
        }
    }
}
