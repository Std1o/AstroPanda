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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class Q3Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    boolean isFirstClick = true;
    ArrayList<String> list = new ArrayList<>();
    TextView tvMoney;
    SharedPreferences prefs;
    boolean nextIsAllowed = false;
    EditText et;
    TextView tvName, tvAge;
    AppCompatSpinner spinner;
    ArrayList<Integer> images = new ArrayList<>();
    SharedPreferences languagePref;
    boolean isShowed = false;
    String currentLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLanguage();
        setContentView(R.layout.activity_q3);
        initAndSetViews();
        initSpinner();

        progressBar = findViewById(R.id.progressId);
        progressBar.setProgress(34);

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
        et = findViewById(R.id.etAdvice);
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
                    Toast.makeText(Q3Activity.this, getString(R.string.restart_for_changes), Toast.LENGTH_SHORT).show();
                } else {
                    isShowed = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener CheckBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox rb = (CheckBox)v;
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
        if (nextIsAllowed || !et.getText().toString().isEmpty()) {
            String str = "";
            for (String s : list) {
                str += s + ", ";
            }
            if (!et.getText().toString().isEmpty()) {
                str += et.getText().toString();
            }
            MainActivity.message.add(str);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("moneyCount", prefs.getInt("moneyCount", 0) + 110);
            editor.apply();
            startActivity(new Intent(this, Q4Activity.class));
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.err_no_selected), Toast.LENGTH_SHORT).show();
        }
    }
}
