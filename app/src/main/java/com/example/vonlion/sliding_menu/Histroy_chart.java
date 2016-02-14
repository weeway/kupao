package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Random;

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
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private Random random;

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
                speed.setText(cursor.getString(cursor.getColumnIndex("speed")));
                //Toast.makeText(Histroy_chart.this.getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
        putPointsInChart();
//        LineChart lineChart = (LineChart)findViewById(R.id.lineChart);
//        xVals=new ArrayList<>();
//        yVals=new ArrayList<>();
//        random=new Random();
//        for(int i=0;i<12;i++){
//            float profix=random.nextFloat();
//            yVals.add(new Entry(profix,i));
//            xVals.add((i+1)+"月");
//        }
//        dataSet=new LineDataSet(yVals,"公司年度利润");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        data=new LineData(xVals,dataSet);
//        lineChart.setData(data);
//        lineChart.setDescription("公司年度利润");
//        lineChart.animateXY(3000,3000);
    }

    public void putPointsInChart(){
        SharedPreferences sharedPref = getSharedPreferences("startTimeFlag",0);
        String starttime = sharedPref.getString("starttime","2016-02-23 20:07");
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
        Toast.makeText(getApplicationContext(),i+"",Toast.LENGTH_LONG).show();
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
