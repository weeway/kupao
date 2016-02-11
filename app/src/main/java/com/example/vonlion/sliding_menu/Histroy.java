package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fmq-pc on 2016/2/3.
 */
public class Histroy  extends Activity {
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy);
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.query("usertb", null, "name like?", new String[]{Login.USER_NAME}, null, null, "name");

        ListView list = (ListView) findViewById(R.id.ListView01);
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.historyitem,
                new String[]{"date","img1","state","distance","img2","time","img3","step"},
                new int[]{R.id.date,R.id.img1,R.id.state,R.id.distance,R.id.img2,R.id.time,R.id.img3,R.id.step});
        list.setAdapter(adapter);





        if(cursor!=null){
            while(cursor.moveToNext()){
                //Map<String, Object> map = new HashMap<String, Object>();
                Log.i("info",cursor.getString(cursor.getColumnIndex("date")));
                Log.i("info",cursor.getString(cursor.getColumnIndex("time")));
            }

        }
    }

    /**
     * 获取数据
     * @return
     */
    private List<Map<String, Object>> getData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(cursor!=null){
            while(cursor.moveToNext()){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                map.put("img1", R.drawable.run);
                map.put("state", cursor.getString(cursor.getColumnIndex("motionState")));
                map.put("distance", cursor.getString(cursor.getColumnIndex("distance")));
                map.put("img2", R.drawable.time);
                map.put("time", cursor.getString(cursor.getColumnIndex("time")));
                map.put("img3", R.drawable.pace);
                map.put("step", cursor.getString(cursor.getColumnIndex("theyCount"))+"步");
                list.add(map);
            }
        }
        return list;
    }



    /**
     * 跳转
     * @param v
     */
//    返回主界面
    public void change_alpha(View v){
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha1(View v){
        Intent intent = new Intent(this,Histroy_chart.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
