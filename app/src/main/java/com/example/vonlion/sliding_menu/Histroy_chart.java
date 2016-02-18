package com.example.vonlion.sliding_menu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    private AMap aMap;
    private MapView traceMapView;

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
        traceMapView = (MapView) findViewById(R.id.traceMap);
        traceMapView.onCreate(savedInstanceState);
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
//        putPointsInChart();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
        dataSet=new LineDataSet(yVals,"速度-时间 数据");
//        dataSet.setDrawFilled(true);
        dataSet.setColors(new int[]{0xff00ffff});//设置曲线颜色
        dataSet.setDrawCubic(true);//将折线设置成曲线
        dataSet.setDrawValues(false);//不显示曲线上的数据
        dataSet.setCircleSize(3.4f);//数据点大小
        dataSet.setLineWidth(3f);//曲线宽度
        dataSet.setCircleColor(0x77caff70);//数据点外围颜色
        dataSet.setCircleColorHole(0x77caff70);//数据点中心颜色
        dataSet.setDrawCircles(false);//不显示数据点
        data=new LineData(xVals,dataSet);
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setStartAtZero(true);//Y轴从0开始

        Bitmap bitmap;
        BitmapDrawable bitmapDrawable;
        Drawable drawable = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither=false;                     //Disable Dithering mode
        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inMutable = true;
        options.inSampleSize = 4;
        options.inTempStorage=new byte[32 * 1024];

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.chart_bg2,options);
        bitmapDrawable = new BitmapDrawable(bitmap);
        drawable = bitmapDrawable;
        System.gc();

        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(0xeeffffff);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextColor(0xeeffffff);
        rightYAxis.setEnabled(false);//取消显示右Y轴
        lineChart.setDrawGridBackground(false);//取消显示网格背景
        lineChart.setBackgroundColor(0x08000000);//地图背景颜色
        lineChart.setData(data);
        lineChart.setSelected(false);
        lineChart.dispatchSetSelected(false);
        lineChart.setDescriptionColor(0x11ffffff);//图表介绍文本的颜色
        lineChart.setBackground(drawable); //图表背景图片
        lineChart.setDescription("");
        lineChart.setBorderColor(0xeeffffff);//轴线颜色
        lineChart.animateXY(2000,2000);//X、Y轴动画2s、2s

        showTrace();
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Histroy.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void showTrace(){
        initMap();
        LatLng[] latLng = new LatLng[3];
        int cnt = 0;
        latLng[1] = new LatLng(31.231706,121.472644);
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query("tracetb", null, "starttime like?", new String[]{starttime}, null, null, "starttime");
        if(cursor!=null){
            while (cursor.moveToNext()){
                float speed = Float.parseFloat(cursor.getString(cursor.getColumnIndex("speed")));
                latLng[cnt] = new LatLng(Float.parseFloat(cursor.getString(cursor.getColumnIndex("latitude"))),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex("longitude"))));
                if(cnt == 2){
                    drawArc(latLng,speed);
                    latLng[0]=latLng[2];
                    cnt = 0;
                }
                cnt++;
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("starttime")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("latitude")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("longitude")));
                Log.i("DataBase",cursor.getString(cursor.getColumnIndex("speed")));
            }
            camMoveToCurPos(latLng[1]);
        }
    }

    public void drawArc(LatLng[] latLngs,float speed) {
        ArcOptions arcOptions;
        arcOptions = new ArcOptions();
        arcOptions.visible(true);
        arcOptions.strokeWidth(13f);
        arcOptions.strokeColor(choseColor(speed));
        aMap.addArc(arcOptions.point(latLngs[0], latLngs[1], latLngs[2]));
    }

    public int choseColor(float speed){
        int color = 0xFF4BEE12;
        float interval = 0.25f;
        int COLOR[] = { 0xff4bee12,0xff88ff16,0xffb4ff19,0xffdeff1d,0xffe9f71d,
                        0xffeeec1d,0xfff2de1d,0xfff6ce1d,0xfff9bd1d,0xfffbae1d,
                        0xfffb9e1d,0xfffc8d1d,0xfffd7e1d,0xfffc711d,0xfffe611d,
                        0xfffd521d
                      };
        for(int index = 0; index < 16; index++){
            if(index*interval<speed && speed<=(index+1)*interval){
                color = COLOR[index];
                break;
            }
            else if(3f<speed){
                color = COLOR[15];
                break;
            }
        }
        return color;
    }

    public void camMoveToCurPos(LatLng latLng) {
        CameraPosition cameraPosition;
        CameraUpdate cameraUpadate;
        cameraPosition = new CameraPosition(latLng, 16.0f, 0.0f, 0.0f);
        cameraUpadate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.animateCamera(cameraUpadate);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = traceMapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        CameraUpdateFactory.zoomTo(18.0f);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);
//        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
    }
}
