package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class map extends Activity  implements LocationSource, AMap.OnMapScreenShotListener,
        View.OnClickListener, AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy locationClient;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    private TextView tvDistance = null;
    private TextView tvSteps = null;
    private TextView tvShowTime = null;
    private TextView tvCaloric;
    private Button btStop = null;
    private Button btStart = null;
    private Timer timer = null;
    private Message msg = null;
    private TimerTask task = null;
    private LatLng[] las = new LatLng[2];
    private LatLng[] latLngs = new LatLng[3];
    private LatLng lonMin = new LatLng(0, 0), lonMax = new LatLng(0, 0),
            latMin = new LatLng(0, 0), latMax = new LatLng(0, 0);
    private int flag = 0;
    private int totalSec = 0;
    private int secForStoreChartData = 0;
    private double length = 0;
    private int cnt = 0;
    private double averSpeed = 0;
    private double sum = 0;
    private int times = 0;
    int disFirstPart;
    int disSecondPart;
    int calFirstPart ;
    int calSecondPart;
    private AlertDialog.Builder builder;
    private String starttime;
    String USER_NAME;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView(savedInstanceState);
        initMap();
        alarmStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        builder  = new AlertDialog.Builder(map.this);

        //开始时间-24小时制
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        starttime = sDateFormat.format(new java.util.Date());
        SharedPreferences share = getSharedPreferences("User_date",Login.MODE_PRIVATE);
        USER_NAME =share.getString("username", "");
        //Toast.makeText(map.this.getApplicationContext(),USER_NAME, Toast.LENGTH_SHORT).show();
    }

    //获取控件及设置监听
    public void initView(Bundle savedInstanceState) {
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvShowTime = (TextView) findViewById(R.id.tvTime);
//        tvSteps = (TextView) findViewById(R.id.tvSteps);
        btStart = (Button) findViewById(R.id.btStart);
        btStop = (Button) findViewById(R.id.btStop);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        btStart.setOnClickListener(this);
        btStop.setOnClickListener(this);
    }

    public void alarmStart() {
        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();

        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);

        int alarmInterval = 1;
        if (alarm != null) {
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2 * 1000,
                    alarmInterval * 1000, alarmPi);
        }
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            AMapLocation loc = (AMapLocation) msg.obj;
            secForStoreChartData++;
            // 显示系统小蓝点
            mListener.onLocationChanged(loc);
            displayDistance(length);
            displayCaloric();
            displayRealtimeSpeed(loc);
            drawTrace(loc);
            camMoveToCurPos(loc);
            if(secForStoreChartData%(5) == 0){
                // 每两秒进行一次广播，每十分钟记录一次速度、时间
                storeChartData(loc);
                Toast.makeText(getApplicationContext(),"保存数据",Toast.LENGTH_SHORT).show();
            }
        }
    };

    //储存画图表所需的数据
    public void storeChartData(AMapLocation loc) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm");
        String date = sDateFormat.format(new java.util.Date());
        DatabaseHelper database = new DatabaseHelper(this);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("curspeed", String.valueOf(loc.getSpeed()));
        cv.put("curtime", date);
        cv.put("starttime", starttime);
        cv.put("username", USER_NAME);
        db.insert("charttb", null, cv);
        cv.clear();
    }

    public void displayRealtimeSpeed(AMapLocation loc){
        TextView tvRealtimeSpeed = (TextView) findViewById(R.id.tvRealtimeSpeed);
        String realtimeSpeed = String.format("%.1f",loc.getSpeed()*3.6);
        tvRealtimeSpeed.setText(realtimeSpeed);
    }

    //显示跑步距离
    public void displayDistance(double length) {
        disFirstPart = (int) length / 1000;
        disSecondPart = ((int) length % 1000) / 100;
        tvDistance.setText(disFirstPart + "." + disSecondPart);
    }

    //显示热量
    public void displayCaloric() {
        TextView tvWeight = (TextView) findViewById(R.id.tvWeight);
        int weight = 100;
        try {
            weight = Integer.parseInt(tvWeight.getText().toString().substring(0, 2));
        } catch (Exception e) {
            weight=100;
        }

        tvCaloric = (TextView) findViewById(R.id.tvCaloric);
        double caloric = ((double) weight * (length / 1000) * 1.036);
        calFirstPart = (int) caloric / 1000;
        calSecondPart = ((int) caloric % 1000) / 100;
        tvCaloric.setText(calFirstPart + "." + calSecondPart);
    }

    //画轨迹
    public void drawTrace(AMapLocation loc) {
        //需要一个全局计数器cnt
        if (loc.getAccuracy() < 30f) {
            latLngs[cnt] = new LatLng(loc.getLatitude(), loc.getLongitude());
            if (cnt == 2) {
                drawArc(latLngs,loc);
                latLngs[0] = latLngs[2];
                cnt = 0;
            }
            cnt++;
        }
    }

    //视图相机移动到当前位置
    public void camMoveToCurPos(AMapLocation loc) {
        CameraPosition cameraPosition;
        CameraUpdate cameraUpadate;
        cameraPosition = new CameraPosition(new LatLng(loc.getLatitude(), loc.getLongitude()), 18.0f, 0.0f, 0.0f);
        cameraUpadate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.animateCamera(cameraUpadate);
    }

    //初始化AMap对象
    private void initMap() {
        locationClient = LocationManagerProxy.getInstance(map.this);
        locationClient.requestLocationData(
                LocationProviderProxy.AMapNetwork, 1000, 3, map.this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    //设置AMap属性
    private void setUpMap() {
        aMap.setLocationSource(this);
        CameraUpdateFactory.zoomTo(18.0f);
//        changeLogo();
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);

        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    //修改小蓝点样式
    private void changeLogo() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        BitmapDescriptorFactory bitmapDescriptorFactory = new BitmapDescriptorFactory();
        BitmapDescriptor bitmapDescriptor = bitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_24dp);
        myLocationStyle = myLocationStyle.myLocationIcon(bitmapDescriptor);
        aMap.setMyLocationStyle(myLocationStyle);
    }

    //画弧线
    public void drawArc(LatLng[] latLngs,AMapLocation loc) {
        ArcOptions arcOptions;
        //ARGB
//        int Tomato = 0xFFFF6347;
//        int OrangeRed = 0xFFFF4500;
//        int Red = 0xFFFF0000;
//        int LightGreen = 0xFF90EE90;
//        int Yellow = 0xFFEEEE00;
//        int OliveDrab = 0xFFC0FF3E;
//        int Gold = 0xFFFFD700;
        arcOptions = new ArcOptions();
        arcOptions.visible(true);
        arcOptions.strokeWidth(13f);
        arcOptions.strokeColor(0xFF0080FF);
        arcOptions.strokeColor(choseColor(loc));
        aMap.addArc(arcOptions.point(latLngs[0], latLngs[1], latLngs[2]));
    }

    public int choseColor(AMapLocation loc){
        int color = 0xFF4BEE12;
        float interval = 0.2f;
        int COLOR[] = { 0xff4bee12,0xff88ff16,0xffb4ff19,0xffdeff1d,0xffe9f71d,
                        0xffeeec1d,0xfff2de1d,0xfff6ce1d,0xfff9bd1d,0xfffbae1d,
                        0xfffb9e1d,0xfffc8d1d,0xfffd7e1d,0xfffc711d,0xfffe611d,
                        0xfffd521d
                      };
        for(int index = 0; index < 16; index++){
            if(index*interval<loc.getSpeed() && loc.getSpeed()<=(index+1)*interval){
                color = COLOR[index];
                break;
            }
            else if(3f<loc.getSpeed()){
                color = COLOR[15];
                break;
            }
        }
        return color;
    }
    //获得最大、最小经纬点
    private void getMaxMinLatLng(AMapLocation location) {
        if (location.getLatitude() > latMax.latitude)
            latMax = new LatLng(location.getLatitude(), 0);
        if (location.getLatitude() < latMin.latitude)
            latMin = new LatLng(location.getLatitude(), 0);
        if (location.getLongitude() > lonMax.longitude)
            latMax = new LatLng(0, location.getLongitude());
        if (location.getLongitude() < lonMin.longitude)
            latMin = new LatLng(0, location.getLongitude());
    }

    //获得合适的比例尺
    private double getApproriateZoom() {
        double zoom = 0;
        double lenX = AMapUtils.calculateLineDistance(lonMax, lonMin);
        double lenY = AMapUtils.calculateLineDistance(latMax, latMin);
        double len = (lenX > lenY ? lenX : lenY);
        return zoom;
    }

    //跳转至主界面
    public void change_roll(View v) {
        Intent intent = new Intent(this, main_interface.class);
        startActivity(intent);
        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
    }

    //接收广播 开始定位
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION")) {
//                Toast.makeText(getApplicationContext(),"BroadcastRecevier",Toast.LENGTH_SHORT).show();
                locationClient = LocationManagerProxy.getInstance(map.this);
                //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
                //在定位结束后，在合适的生命周期调用destroy()方法
                //其中如果间隔时间为-1，则定位只定一次
                locationClient.requestLocationData(
                        LocationProviderProxy.AMapNetwork, 1000, 3, map.this);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        mListener = null;
        if (locationClient != null) {
            locationClient.removeUpdates(this);
            locationClient.destroy();
        }
        locationClient = null;

        if (null != alarmReceiver) {
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
    }

    /**
     * 定位成功后回调函数
     * 位置发生变化后，向Handler发送loc位置信息对象
     */

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            mListener.onLocationChanged(loc);
            mHandler.sendMessage(msg);
            if (loc.getAccuracy() < 36f) {
                //累计距离
                las[flag] = new LatLng(loc.getLatitude(), loc.getLongitude());
                if (flag == 1) {
                    length += AMapUtils.calculateLineDistance(las[0], las[1]);
                    las[0] = las[1];
                    flag = 0;
                }
                flag++;

                //最大最小经纬
                getMaxMinLatLng(loc);
                if (totalSec % 120 == 0) {
                    sum += loc.getSpeed();
                    times++;
                }
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            totalSec++;

             int min = totalSec / 60;
             int sec = totalSec % 60;
             int hour = min / 60;
            min = min % 60;
            tvShowTime.setText(String.format(
                    "%1$02d:%2$02d:%3$02d", hour, min, sec
            ));
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btStart) {
            if (btStart.getText().equals("开始跑步")) {
                Toast.makeText(getApplicationContext(), "开始跑步", Toast.LENGTH_SHORT).show();
                btStart.setText("暂停跑步");
                if (null == timer) {
                    if (null == task) {
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (null == msg) {
                                    msg = new Message();
                                } else {
                                    msg = Message.obtain();
                                }
                                timerHandler.sendMessage(msg);
                            }
                        };
                    }
                    timer = new Timer(true);
                    timer.schedule(task, 1000, 1000);
                }
            } else if (btStart.getText().equals("暂停跑步")) {
                Toast.makeText(getApplicationContext(), "暂停跑步", Toast.LENGTH_SHORT).show();
                btStart.setText("继续跑步");
                task.cancel();
                task = null;
                timer.cancel();
                timer.purge();
                timer = null;
            }else if(btStart.getText().equals("继续跑步")){
                Toast.makeText(getApplicationContext(),"继续跑步",Toast.LENGTH_LONG).show();
                btStart.setText("暂停跑步");
                if (null == timer) {
                    if (null == task) {
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (null == msg) {
                                    msg = new Message();
                                } else {
                                    msg = Message.obtain();
                                }
                                timerHandler.sendMessage(msg);
                            }
                        };
                    }
                    timer = new Timer(true);
                    timer.schedule(task, 1000, 1000);
                }
            }
        } else if (v.getId() == R.id.btStop) {
            Toast.makeText(getApplicationContext(), "已停止\n可拖动地图查看轨迹", Toast.LENGTH_SHORT).show();

            //取消定位
            mListener = null;
            if (locationClient != null) {
                locationClient.removeUpdates(this);
                locationClient.destroy();
            }
            locationClient = null;

            //取消广播
            if (null != alarmReceiver) {
                unregisterReceiver(alarmReceiver);
                alarmReceiver = null;
            }

            //停止计时
            if (task != null) {
                task.cancel();
                task = null;
            }
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }

            //设按钮为不可用
            btStart.setEnabled(false);
            btStop.setEnabled(false);

            //移动相机到合适位置
            CameraPosition cameraPosition;
            CameraUpdate cameraUpadate;
            cameraPosition = new CameraPosition(new LatLng((latMax.latitude + latMax.latitude) / 2
                    , (lonMax.longitude + lonMin.longitude) / 2), 12.0f, 0.0f, 0.0f);
            cameraUpadate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            aMap.animateCamera(cameraUpadate);

            //计算平均速度
            averSpeed = (sum / times)*3.6;

            //获取轨迹截图
            aMap.getMapScreenShot(this);

            /**
             * 弹出弹窗存入数据
             **/
            DatabaseHelper database = new DatabaseHelper(this);
            final SQLiteDatabase db = database.getReadableDatabase();
            builder.setTitle("确认" ) ;
            builder.setMessage("是否保存？" ) ;
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
                    //String date = sDateFormat.format(new Date());
                    String time = tvShowTime.getText().toString();
//                    String steps = tvSteps.getText().toString();
                    String distance = Integer.toString(disFirstPart) + "." + Integer.toString(disSecondPart);
                    String caloric = Integer.toString(calFirstPart) + "." + Integer.toString(calSecondPart);
                    String state;
                    if (averSpeed <= 9) {
                        state = "慢跑";
                    } else if (averSpeed > 9 && averSpeed
                            <= 12) {
                        state = "快跑";
                    } else {
                        state = "骑车";
                    }
                    ContentValues cv = new ContentValues();
                    int steps = (int)length*100/65;
                    cv.put("name", USER_NAME);
                    cv.put("speed",String.format("%.2f",averSpeed));
                    cv.put("date", starttime);
                    cv.put("distance", distance);
                    cv.put("time", time);
                    cv.put("theyCount", steps);
                    cv.put("energy", caloric);
                    cv.put("motionState", state);
                    db.insert("usertb", null, cv);
                    cv.clear();

                    Intent intent = new Intent(map.this, Histroy_chart.class);
                    intent.putExtra("date",starttime);
                    startActivity(intent);
                    overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
                }
            } );
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        File f = new File("/sdcard/", "轨迹截图.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "map Page", // TODO: Define a title for the content shown.
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
                "map Page", // TODO: Define a title for the content shown.
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