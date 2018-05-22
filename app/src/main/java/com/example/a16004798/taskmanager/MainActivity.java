package com.example.a16004798.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int reqCode = 12345;

    ListView lv;
    DBHelper db;
    ArrayList<String> alTask = new ArrayList<String>();
    ArrayAdapter aaTask;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5); //amount 5 means, 5 seconds later, it pop up

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(MainActivity.this, BroadcastReceiverNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);

        //Set the alarm
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


        db = new DBHelper(this);
        db.getWritableDatabase();


        btnAdd = findViewById(R.id.btnAdd);
        lv = findViewById(R.id.listView);

        alTask.addAll(db.getTaskContent());
        alTask.add("Testing\nTesting");

        aaTask = new ArrayAdapter(this, android.R.layout.simple_list_item_1, alTask);
        lv.setAdapter(aaTask);
        db.close();

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 9);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 9){
            db = new DBHelper(this);
            db.getWritableDatabase();
            db.close();

            aaTask.notifyDataSetChanged();
        }
    }
}
