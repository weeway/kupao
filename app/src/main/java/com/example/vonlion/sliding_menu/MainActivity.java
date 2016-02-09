package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slidingmenu.view.SlidingMenu;

import org.json.JSONException;
import android.net.ParseException;
import java.io.IOException;



public class MainActivity extends Activity{
    private final int SPLASH_DISPLAY_LENGHT = 2500; //延迟2.5秒

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this,Login.class);
                MainActivity.this.startActivity(mainIntent);
//                MainActivity.this.finish();
                overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

}