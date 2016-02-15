package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Vonlion on 2016/2/11.
 */
public class Histroy_chart extends Activity {
    private TextView distance1;
    private TextView time;
    private TextView energy;
    private TextView speed;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private String starttime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy_chart);
        Intent intent = getIntent();
        starttime = intent.getStringExtra("date");
        distance1 = (TextView)findViewById(R.id.distance1);
        time= (TextView)findViewById(R.id.time);
        speed = (TextView)findViewById(R.id.speed);
        energy = (TextView)findViewById(R.id.energy);
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("usertb", null, "date like?", new String[]{starttime}, null, null, "date");

        if(cursor!=null){
            while(cursor.moveToNext()){
                distance1.setText(cursor.getString(cursor.getColumnIndex("distance")));
                time.setText(cursor.getString(cursor.getColumnIndex("time")));
                energy.setText(cursor.getString(cursor.getColumnIndex("energy")));
                speed.setText(cursor.getString(cursor.getColumnIndex("speed")));
            }
        }
        db.close();
        putPointsInChart();
    }

    public void putPointsInChart(){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("charttb", null, "starttime like?", new String[]{starttime}, null, null, "starttime");

        LineChart lineChart = (LineChart)findViewById(R.id.lineChart);
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();

        int i = 0;
        if(cursor!=null){
            while (cursor.moveToNext()){
                yVals.add(new Entry(cursor.getFloat(cursor.getColumnIndex("curspeed")),i));
                xVals.add(cursor.getString(cursor.getColumnIndex("curtime")));
                i++;
                Log.i("DB",cursor.getString(cursor.getColumnIndex("curspeed")));
                Log.i("DB",cursor.getString(cursor.getColumnIndex("curtime")));
            }
        }
        Toast.makeText(getApplicationContext(),"本次采集"+i+"个"+"数据",Toast.LENGTH_LONG).show();
        dataSet=new LineDataSet(yVals,"运动数据");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        lineChart.setData(data);
        lineChart.setDescription("运动数据");
        lineChart.animateXY(3000,3000);
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Histroy.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
