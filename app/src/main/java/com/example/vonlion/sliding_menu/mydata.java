package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by fmq-pc on 2016/1/30.
 */
public class mydata extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydata);
    }

        public void change_alpha(View v){
            Intent intent = new Intent(this,main_interface.class);

            startActivity(intent);

            overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
        }
}


