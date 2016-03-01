package com.example.vonlion.wooker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by fmq-pc on 2016/2/10.
 */
public class Edit_data extends Activity {
    TextView nickname;
    TextView height;
    TextView weight;
    TextView goal;
    TextView adress1;
    TextView adress2;
    TextView signature;
    String  USER_NAME;
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);
        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");

        nickname = (TextView) findViewById(R.id.nickname);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        goal = (TextView) findViewById(R.id.goal);
        adress1 = (TextView) findViewById(R.id.adress1);
        adress2 = (TextView) findViewById(R.id.adress2);
        signature = (TextView) findViewById(R.id.signature);


    }

    public void change_alpha(View v){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", USER_NAME);
        cv.put("nickname",nickname.getText().toString());
        cv.put("height",height.getText().toString());
        cv.put("weight",weight.getText().toString());
        cv.put("goal",goal.getText().toString());
        cv.put("adress1",adress1.getText().toString());
        cv.put("adress2",adress2.getText().toString());
        cv.put("signature",signature.getText().toString());

        db.insert("userdata", null, cv);
        cv.clear();
        
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改头像
    public void change_alpha1(View v){
        Intent intent = new Intent(this,Edit_photo.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改姓名
    public void change_alpha2(View v){
        Intent intent = new Intent(this,Edit_name.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

//    修改身高
    public void change_alpha3(View v){
        Intent intent = new Intent(this,Edit_height.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改体重
    public void change_alpha4(View v){
        Intent intent = new Intent(this,Edit_weight.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改目标
    public void change_alpha5(View v){
        Intent intent = new Intent(this,Edit_target.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改地区
    public void change_alpha6(View v){
        Intent intent = new Intent(this,Edit_location.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改地址
    public void change_alpha7(View v){
        Intent intent = new Intent(this,Edit_address.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //    修改个性签名
    public void change_alpha8(View v){
        Intent intent = new Intent(this,Edit_mark.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
