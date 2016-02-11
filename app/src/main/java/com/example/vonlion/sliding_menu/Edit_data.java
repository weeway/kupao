package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by fmq-pc on 2016/2/10.
 */
public class Edit_data extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);

    }

    public void change_alpha(View v){
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
