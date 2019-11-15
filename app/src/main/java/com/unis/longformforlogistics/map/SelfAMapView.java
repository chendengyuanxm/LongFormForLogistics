package com.unis.longformforlogistics.map;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;

/**
 * Author:Arnold
 * Date:2019/11/15 16:30
 */
public class SelfAMapView extends MapView implements AMapLocationListener{
    private static final String TAG = SelfAMapView.class.getSimpleName();
    private Context context;

    private AMap aMap;
    private AMapLocationClient mLocationClient;

    private AMapLocation mCurrentPosition;

    public SelfAMapView(Context context) {
        this(context, null, -1);
    }

    public SelfAMapView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public SelfAMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
        init();
    }

    public SelfAMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        this.context = context;
        init();
    }

    private void init() {
        initMap();
        startLocation();
    }

    private void initMap() {
        if (aMap == null) {
            aMap = getMap();
        }

        aMap.setMapTextZIndex(2);//地图文字的Z轴指数,设置为2可以将地图底图文字设置在添加的覆盖物之上

        UiSettings uiSettings = aMap.getUiSettings();//地图的UI设置控制器
        uiSettings.setCompassEnabled(false);// 设置指南针是否显示
        uiSettings.setZoomControlsEnabled(true);// 设置缩放按钮是否显示
        uiSettings.setScaleControlsEnabled(true);// 设置比例尺是否显示
        uiSettings.setRotateGesturesEnabled(true);// 设置地图旋转是否可用
        uiSettings.setTiltGesturesEnabled(true);// 设置地图倾斜是否可用
        uiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为秒。
        myLocationStyle.interval(3000);
        //设置连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        //具体场景可根据高德API查看
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.showMyLocation(true);//显示定位蓝点
        //设置圆圈颜色
        myLocationStyle.radiusFillColor(0x70ffffff);
        //设置边框颜色
        myLocationStyle.strokeColor(0xffffffff);
        aMap.setMyLocationStyle(myLocationStyle);//关联myLocationStyle
        //开启定位,设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认false。
        aMap.setMyLocationEnabled(true);
        //监听定位信息的回调
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
            }
        });
    }

    private void registerListener() {
//        aMap.setOnMapClickListener(this);
//        aMap.setOnMarkerClickListener(this);

    }

    private void startLocation(){
        if(null == mLocationClient){
            mLocationClient = new AMapLocationClient(context);
        }

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        // 地址信息
        locationClientOption.setInterval(1000);
        locationClientOption.setNeedAddress(true);
        mLocationClient.setLocationOption(locationClientOption);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    /**
     * 停止单次客户端
     */
    private void stopLocation(){
        if(null != mLocationClient){
            mLocationClient.stopLocation();
        }
    }

    public void moveToPosition(LatLng latLng) {
        CameraUpdate mCameraUpdate = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
        aMap.moveCamera(mCameraUpdate);
    }

    public AMapLocation getCurrentPosition() {
        return mCurrentPosition;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.d(TAG, "定位位置：" + aMapLocation.toString());
        if (aMapLocation != null) {
            mCurrentPosition = aMapLocation;
            moveToPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            stopLocation();
        }
    }
}
