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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class GetVipActivity extends AppCompatActivity {

    AppCompatSpinner spinner;
    ArrayList<Integer> images = new ArrayList<>();
    SharedPreferences languagePref;
    boolean isShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        languagePref = getSharedPreferences("languagePref", MODE_PRIVATE);
        String currentActivity = currentPagePref.getString("currentPage", "");
        if (!currentActivity.isEmpty()) {
            startActivity(new Intent("." + currentActivity));
            finish();
        } else {
            Resources res = getResources();
// Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            System.out.println(getLanguageFromPosition(languagePref.getInt("language", getSpinnerPosition(Locale.getDefault().getCountry()))));
            conf.setLocale(new Locale(getLanguageFromPosition(languagePref.getInt("language", getSpinnerPosition(Locale.getDefault().getCountry()))))); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
            res.updateConfiguration(conf, dm);
            setContentView(R.layout.activity_get_vip);

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
            int pos = languagePref.getInt("language", getSpinnerPosition(Locale.getDefault().getCountry()));
            spinner.setSelection(pos);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences.Editor editor = languagePref.edit();
                    editor.putInt("language", position);
                    editor.apply();
                    if (isShowed) {
                        Toast.makeText(GetVipActivity.this, getString(R.string.restart_for_changes), Toast.LENGTH_SHORT).show();
                    } else {
                        isShowed = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(this, Q1Activity.class));
    }

    public static int getSpinnerPosition(String code) {
        int position = 0;
        switch (code.toLowerCase()) {
            case "en":
                position = 0;
                break;
            case "de":
                position = 1;
                break;
            case "es":
                position = 2;
                break;
            case "fr":
                position = 3;
                break;
            case "it":
                position = 4;
                break;
            case "ru":
                position = 5;
                break;
        }
        return position;
    }

    public static String getLanguageFromPosition(int position) {
        String code = "";
        switch (position) {
            case 0:
                code = "en";
                break;
            case 1:
                code = "de";
                break;
            case 2:
                code = "es";
                break;
            case 3:
                code = "fr";
                break;
            case 4:
                code = "it";
                break;
            case 5:
                code = "ru";
                break;
        }
        return code;
    }
}
