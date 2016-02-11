package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Vonlion on 2016/2/11.
 */
public class Edit_location extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location);
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
