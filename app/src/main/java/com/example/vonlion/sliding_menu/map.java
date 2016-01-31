package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

public class map extends Activity implements LocationSource, AMapLocationListener{
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy locationClient;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    LatLng [] latLngs = new LatLng[3];
    int cnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
    }

    public void alarmStart(){
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

        int alarmInterval = 5;
        if(alarm != null){
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+2*1000,
                    alarmInterval*1000,alarmPi);
        }
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            AMapLocation loc = (AMapLocation) msg.obj;

            //画轨迹曲线
            if(loc.getAccuracy() < 10f){
                latLngs[cnt] = new LatLng(loc.getLatitude(),loc.getLongitude());
            }
            if(cnt == 2){
                drawArc(latLngs);
                latLngs[0]=latLngs[2];
                cnt = 0;
//                Toast.makeText(getApplicationContext(),"draw", Toast.LENGTH_SHORT).show();
            }
            cnt++;

            // 显示系统小蓝点
            mListener.onLocationChanged(loc);

            //视图相机移动到当前位置
            CameraPosition cameraPosition;
            CameraUpdate cameraUpadate;
            cameraPosition = new CameraPosition(new LatLng(loc.getLatitude(), loc.getLongitude()), 18.0f, 0.0f, 0.0f);
            cameraUpadate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            aMap.animateCamera(cameraUpadate);

            //获取当前屏幕中心点 位置信息
            cameraPosition = aMap.getCameraPosition();
        }
    };

    /**
     * 初始化AMap对象
     */
    private void init() {
        alarmStart();
        locationClient = LocationManagerProxy.getInstance(map.this);
        locationClient.requestLocationData(
                LocationProviderProxy.AMapNetwork, 1000, 3,map.this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setLocationSource(this);
        CameraUpdateFactory.zoomTo(18.0f);

       // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    public void drawArc(LatLng [] latLngs){
        ArcOptions arcOptions;
        arcOptions = new ArcOptions();
        arcOptions.visible(true);
        arcOptions.strokeWidth(10f);
        arcOptions.strokeColor(0xFF0080FF);
        aMap.addArc(arcOptions.point(latLngs[0],latLngs[1],latLngs[2]));
    }

    public void change_roll(View v){//地图界面跳转主界面
        Intent intent = new Intent(this, main_interface.class);

        startActivity(intent);

        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
    }

    //接收广播 开始定位
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("LOCATION")){
//                Toast.makeText(getApplicationContext(),"BroadcastRecevier",Toast.LENGTH_SHORT).show();
                locationClient = LocationManagerProxy.getInstance(map.this);
                //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
                //在定位结束后，在合适的生命周期调用destroy()方法
                //其中如果间隔时间为-1，则定位只定一次
                locationClient.requestLocationData(
                        LocationProviderProxy.AMapNetwork, 1000, 3,map.this);
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

        if(null != alarmReceiver){
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
        if(null != loc){
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            mListener.onLocationChanged(loc);
            mHandler.sendMessage(msg);
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
    public void deactivate() {}

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}