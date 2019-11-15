package com.unis.longformforlogistics.map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.unis.longformforlogistics.CheckPermissionsActivity;
import com.unis.longformforlogistics.R;
import com.unis.longformforlogistics.util.AMapUtil;
import com.unis.longformforlogistics.util.ToastUtil;

import androidx.annotation.Nullable;

/**
 * Author:Arnold
 * Date:2019/11/14 16:08
 */
public class AMapActivity extends CheckPermissionsActivity implements RouteSearch.OnRouteSearchListener {
    private static final String TAG = AMapActivity.class.getSimpleName();

    private AMap aMap;
    private MapView mAMapView;
    private RouteSearch mRouteSearch;
    private AMapLocationClient mLocationClient;
    private DriveRouteResult mDriveRouteResult = null;

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;

    private LatLonPoint mCurrentPoint = null; //new LatLonPoint(24.48469, 118.183743)
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);
    private LatLonPoint mEndPoint = new LatLonPoint(25.48469, 119.183743);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        initViews(savedInstanceState);
        initMap();
        registerListener();
        if (mCurrentPoint == null) {
            startLocation();
        }
    }

    private void initViews(@Nullable Bundle savedInstanceState) {
        mAMapView = (MapView) findViewById(R.id.mapView);
        mAMapView.onCreate(savedInstanceState);

        findViewById(R.id.btn_cal_route).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DRIVING_SINGLE_DEFAULT);
            }
        });

        findViewById(R.id.btn_start_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaviLatLng startLatlng = new NaviLatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude());
                NaviLatLng endLatlng = new NaviLatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude());
                Intent intent = new Intent(AMapActivity.this, AMapNaviActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("startLatlng", startLatlng);
                bundle.putParcelable("startLatlng", endLatlng);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mAMapView.getMap();
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

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    private void registerListener() {
//        aMap.setOnMapClickListener(this);
//        aMap.setOnMarkerClickListener(this);

    }

    void startLocation(){
        if(null == mLocationClient){
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        // 地址信息
        locationClientOption.setInterval(1000);
        locationClientOption.setNeedAddress(true);
        mLocationClient.setLocationOption(locationClientOption);
        mLocationClient.setLocationListener(locationSingleListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止单次客户端
     */
    void stopLocation(){
        if(null != mLocationClient){
            mLocationClient.stopLocation();
        }
    }

    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            Log.d(TAG, "定位位置：" + location.toString());
            if (location != null) {
                mCurrentPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
                moveToPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                stopLocation();
            }
        }
    };

    public void searchRouteResult(int routeType, int mode) {
        mStartPoint = mCurrentPoint;
        if (mStartPoint == null) {
            ToastUtil.show(this, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(this, "终点未设置");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "");
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    public void moveToPosition(LatLng latLng) {
        CameraUpdate mCameraUpdate = CameraUpdateFactory
                .newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
        aMap.moveCamera(mCameraUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapView.onPause();
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapView.onDestroy();
        if(mLocationClient != null){
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                Log.d("devin", driveRouteResult.toString());
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(true);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    ToastUtil.show(this, "未查询到路线结果");
                }

            } else {
                ToastUtil.show(this, "未查询到路线结果");
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

}
