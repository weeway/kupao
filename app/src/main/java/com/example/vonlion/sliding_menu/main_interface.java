package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.slidingmenu.view.SlidingMenu;

/**
 * Created by Vonlion on 2015/11/26.
 */
public class main_interface extends Activity  {
    private SlidingMenu mLeftMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeftMenu = (SlidingMenu)findViewById(R.id.id_menu);
    }

    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }

    public void change_alpha(View v){//主界面返回登录界面
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha2(View v){//主界面返回登录界面
        Intent intent = new Intent(this, map.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
