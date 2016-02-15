package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fmq-pc on 2016/2/3.
 */
public class Histroy  extends Activity {
    Cursor cursor;
//    int IdRecord = 0;
    ListView listview;
    static String DATE;
    String USER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histroy);

        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        SharedPreferences share = getSharedPreferences("User_date",map.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");
        if(USER_NAME!=null) {
            cursor = db.query("usertb", null, "name like?", new String[]{USER_NAME}, null, null, "name");
        }
        else {
            Toast.makeText(Histroy.this.getApplicationContext(), "USER_NAME为空", Toast.LENGTH_SHORT).show();
        }
        ListView list = (ListView) findViewById(R.id.ListView01);
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.historyitem,
                new String[]{"date", "img1", "state", "distance", "img2", "time", "img3", "step"},
                new int[]{R.id.date, R.id.img1, R.id.state, R.id.distance, R.id.img2, R.id.time, R.id.img3, R.id.step});
        list.setAdapter(adapter);

        //设置跳转到历史主界面
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map1 = (HashMap<String, String>) listView.getItemAtPosition(position);
                DATE = map1.get("date");
//                Toast.makeText(Histroy.this.getApplicationContext(), DATE, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Histroy.this, Histroy_chart.class);
                intent.putExtra("date",DATE);
                startActivity(intent);
            }
        });

       /* if(cursor!=null){
            while(cursor.moveToNext()){
                //Map<String, Object> map = new HashMap<String, Object>();
                Log.i("info",cursor.getString(cursor.getColumnIndex("date")));
                Log.i("info",cursor.getString(cursor.getColumnIndex("time")));
            }
        }*/
    }

    /**
     * 获取数据
     * @return
     */
    private List<Map<String,Object>> getData() {

        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

        if(USER_NAME!=null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    String value = cursor.getString(cursor.getColumnIndex("motionState"));
                    if (value != null) {
                        if (value.equals("慢跑")) {
                            map.put("img1", R.drawable.walk);
                        } else if (value.equals("快跑")) {
                            map.put("img1", R.drawable.run);
                        } else if (value.equals("骑车")) {
                            map.put("img1", R.drawable.ride);
                        } else {
                            map.put("img1", R.drawable.walk);

                        }
                    }


                    //map.put("state", cursor.getString(cursor.getColumnIndex("motionState")));
                    map.put("state",value);
                    map.put("distance", cursor.getString(cursor.getColumnIndex("distance")));
                    map.put("img2", R.drawable.time);
                    map.put("time", cursor.getString(cursor.getColumnIndex("time")));
                    map.put("img3", R.drawable.pace);
                    map.put("step", cursor.getString(cursor.getColumnIndex("theyCount")) + "步");
                    list1.add(map);
                }

            }
        }
        return list1;
    }

    /**
    * 跳转
    * @param
    */
//    返回主界面
    public void change_alpha(View v){
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
