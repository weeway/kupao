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

        view = (TextView) findViewById(R.id.spinnerText);
        spinner = (Spinner) findViewById(R.id.Spinner01);

        view2 = (TextView) findViewById(R.id.spinnerText2);
        spinner2 = (Spinner) findViewById(R.id.Spinner02);

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,m);
        adapter2 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item,m2);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner2.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);



    }

    public void change_alpha(View v){
        Intent intent = new Intent(this,main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }

    private static final String[] m={"150","151","152","153","154","155","156","157","158","159",
                                     "160","161","162","163","164","165","166","167","168","169",
                                     "170","171","172","173","174","175","176","177","178","179",
                                     "180","181","182","183","184","185","186","187","188","189",
                                     "190","191","192","193","194","195","196","197","198","199","200"};
    private static final String[] m2={"40","41","42","43","44","45","46","47","48","49",
                                      "50","51","52","53","54","55","56","57","58","59",
                                      "60","61","62","63","64","65","66","67","68","69",
                                      "70","71","72","73","74","75","76","77","78","79",
                                      "80","81","82","83","84","85","86","87","88","89",
                                      "90","91","92","93","94","95","96","97","98","99",
                                      "100","101","102","103","104","105","106","107","108","109","110"};
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    private TextView view2 ;
    private Spinner spinner2;
    private ArrayAdapter<String> adapter2;

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//            view.setText("身高  "+m[arg2]+"cm");
            view.setText("身高  ");
            view2.setText("体重  ");

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public void change_alpha2(View v){
        Intent intent = new Intent(this,Edit_name.class);

        startActivity(intent);

        overridePendingTransition(R.anim.out_alpha, R.anim.enter_alpha);
    }
}
