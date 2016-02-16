package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.slidingmenu.view.SlidingMenu;

/**
 * Created by Vonlion on 2015/11/26.
 */
public class main_interface extends Activity{
    private SlidingMenu mLeftMenu;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLeftMenu = (SlidingMenu) findViewById(R.id.id_menu);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void toggleMenu(View view) {
        mLeftMenu.toggle();
    }

    public void change_alpha(View v) {//注销
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha2(View v) {//主界面进入跑步界面
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change_alpha3(View v) {//主界面进入历史界面
        Intent intent = new Intent(this, Histroy.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change1(View v) {//左界面跳转到我的资料
        Intent intent = new Intent(this, mydata.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    public void change6(View v) {//左界面跳转到修改资料界面
        Intent intent = new Intent(this, Edit_data.class);
        startActivity(intent);
        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main_interface Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.vonlion.sliding_menu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main_interface Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.vonlion.sliding_menu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
