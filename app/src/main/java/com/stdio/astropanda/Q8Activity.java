package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Q8Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    int id = 0;
    String answer = "";
    TextView tvMoney;
    SharedPreferences prefs;
    boolean nextIsAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences languagePref = getSharedPreferences("languagePref", MODE_PRIVATE);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(GetVipActivity.getLanguageFromPosition(languagePref
                .getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry())))));
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_q8);

        tvMoney = findViewById(R.id.tvMoney);
        prefs = getSharedPreferences("moneyPref", MODE_PRIVATE);
        tvMoney.setText(prefs.getInt("moneyCount", 0) + "");

        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentPagePref.edit();
        editor.putString("currentPage", getClass().getSimpleName());
        editor.apply();

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(89);

        RadioButton redRadioButton = findViewById(R.id.radioButton1);
        redRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton greenRadioButton = findViewById(R.id.radioButton2);
        greenRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton third = findViewById(R.id.radioButton3);
        third.setOnClickListener(radioButtonClickListener);

        RadioButton fourth = findViewById(R.id.radioButton4);
        fourth.setOnClickListener(radioButtonClickListener);

        RadioButton fifth = findViewById(R.id.radioButton5);
        fifth.setOnClickListener(radioButtonClickListener);
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            id = rb.getId();
            answer = "" + rb.getText();
            nextIsAllowed = true;
        }
    };

    public void onClick(View view) {
        if (nextIsAllowed) {
            MainActivity.message += "\n\nКакое вознаграждение вы готовы дать астрологу/нумерологу за консультацию который" +
                    " работает с известными личностями и звездами? - ";
            MainActivity.message += answer;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 110);
            editor.apply();
            startActivity(new Intent(this, Q9Activity.class));
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.err_no_selected), Toast.LENGTH_SHORT).show();
        }
    }
}
