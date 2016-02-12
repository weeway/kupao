package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vonlion on 2016/2/11.
 */
public class Histroy_chart extends Activity {
    private TextView date;
    private TextView distance;
    private TextView distance1;
    private TextView time;
    private TextView step;
    private TextView energy;
    private TextView state;
    private TextView speed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy_chart);
//        distance = (TextView)findViewById(R.id.distance);
//        date = (TextView)findViewById(R.id.date);
        distance1 = (TextView)findViewById(R.id.distance1);
        time= (TextView)findViewById(R.id.time);
        speed = (TextView)findViewById(R.id.speed);
//        state = (TextView)findViewById(R.id.state);
        energy = (TextView)findViewById(R.id.energy);
//        step = (TextView)findViewById(R.id.step);
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("usertb", null, "date like?", new String[]{Histroy.DATE}, null, null, "date");

        if(cursor!=null){
            while(cursor.moveToNext()){
//                date.setText(cursor.getString(cursor.getColumnIndex("date")));
//                state.setText(cursor.getString(cursor.getColumnIndex("motionState")));
//                distance.setText(cursor.getString(cursor.getColumnIndex("distance")));
                distance1.setText(cursor.getString(cursor.getColumnIndex("distance")));
                time.setText(cursor.getString(cursor.getColumnIndex("time")));
//                step.setText(cursor.getString(cursor.getColumnIndex("theyCount"))+"步");
                energy.setText(cursor.getString(cursor.getColumnIndex("energy")));
                Toast.makeText(Histroy_chart.this.getApplicationContext(), "123", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Histroy.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
