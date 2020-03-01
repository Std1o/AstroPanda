package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlarmManagerBroadcastReceiver alarm;
    public static String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences currentPagePref = getSharedPreferences("currentPagePref", MODE_PRIVATE);
        String currentActivity = currentPagePref.getString("currentPage", "");
        if (!currentActivity.isEmpty()) {
            startActivity(new Intent("." + currentActivity));
            finish();
        } else {
            setContentView(R.layout.activity_main);
            alarm=new AlarmManagerBroadcastReceiver();

            Context context= this.getApplicationContext();
            if(alarm!=null){
                alarm.SetAlarm(context);
            }else{
                Toast.makeText(context,"Alarm is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(this, GetVipActivity.class));
    }
}
