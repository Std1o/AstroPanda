package com.stdio.astropanda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class Q8Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    int id = 0;
    String answer = "";
    TextView tvMoney;
    SharedPreferences prefs;
    boolean nextIsAllowed = false;
    TextView tvName, tvAge;
    AppCompatSpinner spinner;
    ArrayList<Integer> images = new ArrayList<>();
    SharedPreferences languagePref;
    boolean isShowed = false;
    String currentLocale;
    private boolean forIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLanguage();
        setContentView(R.layout.activity_q8);
        initAndSetViews();
        initSpinner();

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

    private void setLanguage() {
        languagePref = getSharedPreferences("languagePref", MODE_PRIVATE);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        currentLocale = GetVipActivity.getLanguageFromPosition(languagePref.getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry())));
        conf.setLocale(new Locale(GetVipActivity.getLanguageFromPosition(languagePref
                .getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry())))));
        res.updateConfiguration(conf, dm);
    }

    private void initAndSetViews() {
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);

        String age = new PrefManager(this).getAge();
        if (!currentLocale.equals("ru")) {
            age = age.replace("лет", "years old");
            age = age.replace("год", "years old");
            age = age.replace("года", "years old");
        }
        tvName.setText(new PrefManager(this).getName());
        tvAge.setText(age);
    }

    private void initSpinner() {
        images.add(R.drawable.en);
        images.add(R.drawable.de);
        images.add(R.drawable.es);
        images.add(R.drawable.fr);
        images.add(R.drawable.it);
        images.add(R.drawable.ru);

        spinner = findViewById(R.id.spinner2);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, images) {

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row2, null);
                }

                ImageView ivDrink = v.findViewById(R.id.imageView);
                ivDrink.setImageDrawable(ResourcesCompat.getDrawable(getResources(), images.get(position), null));
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row_drop_down, null);
                }
                ImageView ivDrink = v.findViewById(R.id.imageView);
                ivDrink.setImageDrawable(ResourcesCompat.getDrawable(getResources(), images.get(position), null));
                return v;
            }
        };

        spinner.setAdapter(spinnerAdapter);
        System.out.println(Locale.getDefault().getCountry());
        int pos = languagePref.getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry()));
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = languagePref.edit();
                editor.putInt("language", position);
                editor.apply();
                if (isShowed) {
                    Toast.makeText(Q8Activity.this, getString(R.string.restart_for_changes), Toast.LENGTH_SHORT).show();
                } else {
                    isShowed = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (isFinishing() && !forIntent) {
            try {
                ExcelCreator.createExcelFile(this, getString(R.string.app_name), null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
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
            MainActivity.message.add(answer);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 110);
            editor.apply();
            forIntent = true;
            startActivity(new Intent(this, Q9Activity.class));
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.err_no_selected), Toast.LENGTH_SHORT).show();
        }
    }
}
