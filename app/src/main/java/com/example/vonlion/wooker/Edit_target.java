package com.example.vonlion.wooker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Vonlion on 2016/2/11.
 */
public class Edit_target extends Activity {
    String USER_NAME;
    TextView edgoal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_targent);
        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");
    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void yesgoal(View v){
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();
        edgoal = (TextView)findViewById(R.id.edgoal);

        Cursor cursor = db.query("userdata", null, "username like?", new String[]{USER_NAME}, null, null, "username");

        cv.put("username",USER_NAME);
        cv.put("goal",edgoal.getText().toString());
        if(cursor.getCount()!=0) {
            db.update("userdata", cv, "username=?", new String[]{USER_NAME});
        }else {
            db.insert("userdata", null, cv);
        }
        cv.clear();
        cursor.close();
        Intent intent = new Intent(this,Edit_data.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
