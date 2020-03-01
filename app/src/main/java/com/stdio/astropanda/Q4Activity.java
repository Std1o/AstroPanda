package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Q4Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    int id = 0;
    String answer = "";
    TextView tvMoney;
    SharedPreferences prefs;
    boolean nextIsAllowed = false;
    EditText et;

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
        setContentView(R.layout.activity_q4);

        tvMoney = findViewById(R.id.tvMoney);
        prefs = getSharedPreferences("moneyPref", MODE_PRIVATE);
        tvMoney.setText(prefs.getInt("moneyCount", 0) + "");

        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentPagePref.edit();
        editor.putString("currentPage", getClass().getSimpleName());
        editor.apply();

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(48);

        RadioButton redRadioButton = findViewById(R.id.RadioButton1);
        redRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton greenRadioButton = findViewById(R.id.RadioButton2);
        greenRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton blueRadioButton = findViewById(R.id.RadioButton3);
        blueRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton fourth = findViewById(R.id.RadioButton4);
        fourth.setOnClickListener(radioButtonClickListener);

        RadioButton fifth = findViewById(R.id.RadioButton5);
        fifth.setOnClickListener(radioButtonClickListener);
        et = findViewById(R.id.etAdvice);
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.RadioButton5) {
                et.setVisibility(View.VISIBLE);
                nextIsAllowed = false;
            }
            else {
                et.setVisibility(View.GONE);
                RadioButton rb = (RadioButton)v;
                id = rb.getId();
                answer = "" + rb.getText();
                nextIsAllowed = true;
            }
        }
    };

    public void onClick(View view) {
        if (nextIsAllowed || !et.getText().toString().isEmpty()) {
            MainActivity.message += "\n\nКак часто у вас возникают вопросы, на которые вам бы хотелось получить ответы от\n" +
                    "        профессиональных астрологов/нумерологов? - ";
            if (!et.getText().toString().isEmpty() && !nextIsAllowed) {
                answer = "\nВаш вариант - " + et.getText().toString();
            }
            MainActivity.message += answer;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 110);
            editor.apply();
            startActivity(new Intent(this, Q5Activity.class));
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.err_no_selected), Toast.LENGTH_SHORT).show();
        }
    }
}
