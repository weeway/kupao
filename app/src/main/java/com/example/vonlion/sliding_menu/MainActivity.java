package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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



public class MainActivity extends Activity {

    private EditText username;
    private EditText password;
    String msg="pp";
    private SlidingMenu mLeftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        mLeftMenu = (SlidingMenu)findViewById(R.id.id_menu);

        findViewById(R.id.traceroute_rootview).setOnClickListener(new View.OnClickListener() {
            //点击屏幕外取消输入框
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.traceroute_rootview:
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                }

            }
        });


    }
    public void toggleMenu(View view){
        mLeftMenu.toggle();
    }

    public void change_alpha(View v){//登录跳转注册
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    //登录进入主界面

    public void change_roll(View v) throws ParseException, IOException, JSONException {
        final String name = this.username.getText().toString();
        final String pwd = this.password.getText().toString();
        final Intent intent = new Intent(this, main_interface.class);
        (new Thread(new Runnable() {
            public void run() {
                Get get = new Get();
                try {
                    msg = get.loginb("http://115.159.120.123:8080/LoginServer/ReceiveServers?username=" + name + "&&password=" + pwd);
                } catch (IOException var3) {

                    var3.printStackTrace();
                }

                Looper.prepare();
                Toast.makeText(MainActivity.this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                if(msg.equals("登陆成功")){

                    startActivity(intent);

                    overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
                }
                Looper.loop();
        }
        })).start();
    }

}