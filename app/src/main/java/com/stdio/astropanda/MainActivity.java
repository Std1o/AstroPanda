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
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AlarmManagerBroadcastReceiver alarm;
    public static ArrayList<String> message = new ArrayList<>();
    AppCompatSpinner spinner;
    ArrayList<Integer> images = new ArrayList<>();
    SharedPreferences languagePref;
    boolean isShowed = false;
    Button btnGetVip;
    public static String ageStr;
    EditText etName, etDate, etMail;
    private Calendar c = Calendar.getInstance();
    private Calendar now = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        String currentActivity = currentPagePref.getString("currentPage", "");
        if (!currentActivity.isEmpty()) {
            startActivity(new Intent("." + currentActivity).setPackage("com.stdio.astropanda"));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            setLocalization();
            alarm=new AlarmManagerBroadcastReceiver();

            Context context= this.getApplicationContext();
            if(alarm!=null){
                alarm.SetAlarm(context);
            }else{
                Toast.makeText(context,"Alarm is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            try {
                ExcelCreator.createExcelFile(this, getString(R.string.app_name), null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void setLocalization() {
        languagePref = getSharedPreferences("languagePref", MODE_PRIVATE);
        Resources res = getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        System.out.println(GetVipActivity.getLanguageFromPosition(languagePref.getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry()))));
        conf.setLocale(new Locale(GetVipActivity.getLanguageFromPosition(languagePref.getInt("language", GetVipActivity.getSpinnerPosition(Locale.getDefault().getCountry()))))); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        setContentView(R.layout.activity_main);

        initViews();

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
                    Toast.makeText(MainActivity.this, getString(R.string.restart_for_changes), Toast.LENGTH_SHORT).show();
                } else {
                    isShowed = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        btnGetVip = findViewById(R.id.btnGetVip);
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(CheckBoxClickListener);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        etName = findViewById(R.id.etName);
        etDate = findViewById(R.id.etDate);
        etMail = findViewById(R.id.etMail);
    }

    View.OnClickListener CheckBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox)v;
            if (checkBox.isChecked()) {
                btnGetVip.setEnabled(true);
            }
            else {
                btnGetVip.setEnabled(false);
            }
        }
    };

    public void onClick(View view) {
        if (!etName.getText().toString().isEmpty() && !etDate.getText().toString().isEmpty()) {
            onAllowedClick();
        }
        else {
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
        }
    }

    private void onAllowedClick() {
        String name = etName.getText().toString();
        int age = 0;
        String date = etDate.getText().toString();
        date = date.replace(".", "/").replace("-", "/");
        try {
            c.setTime(dateFormat.parse(date));
            age = now.get(Calendar.YEAR) - c.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < c.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            String idade = Integer.toString(age);
            etDate.setText(idade);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (age % 10 == 1){
            ageStr = age + " год";
        }
        else if (age % 10 == 2 || age % 10 == 3 || age % 10 == 4){
            ageStr = age + " года";
        }
        else {
            ageStr = age + " лет";
        }
        new PrefManager(this).setName(name);
        new PrefManager(this).setAge(ageStr);
        MainActivity.message.add(name);
        MainActivity.message.add(ageStr);
        MainActivity.message.add(etMail.getText().toString());
        startActivity(new Intent(this, GetVipActivity.class));
    }
}
