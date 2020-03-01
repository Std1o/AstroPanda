package com.stdio.astropanda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm=new AlarmManagerBroadcastReceiver();

        Context context= this.getApplicationContext();
        if(alarm!=null){
            alarm.SetAlarm(context);
        }else{
            Toast.makeText(context,"Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(this, GetVipActivity.class));
    }
}
