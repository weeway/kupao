package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Vonlion on 2016/1/5.
 */
public class RunningActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);
    }

    public void change_roll(View v){//跑步界面返回主界面
        Intent intent = new Intent(this, main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
    }
}
