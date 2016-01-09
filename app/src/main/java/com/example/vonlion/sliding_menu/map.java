package com.example.vonlion.sliding_menu;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.LatLng;

import static java.lang.Math.sqrt;

public class map extends Activity implements LocationSource, AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    LatLng[] latLngs = new LatLng[3];
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //在onCreat方法中给aMap对象赋值
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 必须要写
        init();

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        CameraUpdateFactory.zoomTo(4.0f);//更改缩放级别到适合
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);

    }
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
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (mListener != null && amapLocation != null) {
            if (amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            }
        }

        if(amapLocation.getAccuracy() < 10f){
        latLngs[cnt] = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
        if(cnt == 2){
            drawArc(latLngs);
            latLngs[0]=latLngs[2];
            cnt = 0;
        }
        cnt++;
    }
}
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 1000, 3, this);
        }
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
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

    public boolean isAppropriate(LatLng last, LatLng cur){
        double difference = sqrt((cur.latitude-last.latitude)*(cur.latitude-last.latitude)
                +(cur.longitude-last.longitude)*(cur.longitude-last.longitude));
        double standard = 0.002;

        if(difference < standard) return true;
        else return false;
    }

    public void drawArc(LatLng[] latLngs){
        ArcOptions arcOptions;
        arcOptions = new ArcOptions();
        arcOptions.visible(true);
        arcOptions.strokeWidth(10f);
        arcOptions.strokeColor(0xFF0080FF);
        aMap.addArc(arcOptions.point(latLngs[0],latLngs[1],latLngs[2]));
    }
}