package com.unis.longformforlogistics.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
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

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;

    private AMap aMap;
    private SelfAMapView mAMapView;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult = null;

    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);
    private LatLonPoint mEndPoint = new LatLonPoint(25.48469, 119.183743);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setTitle("订单详情");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        initViews(savedInstanceState);
        initMap();
        registerListener();
    }

    private void initViews(@Nullable Bundle savedInstanceState) {
        mAMapView = (SelfAMapView) findViewById(R.id.mapView);
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

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    private void registerListener() {
//        aMap.setOnMapClickListener(this);
//        aMap.setOnMarkerClickListener(this);

    }

    public void searchRouteResult(int routeType, int mode) {
        mStartPoint = getCurrentPoint();
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

    private LatLonPoint getCurrentPoint() {
        AMapLocation currentLocation = mAMapView.getCurrentPosition();
        return new LatLonPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapView.onDestroy();
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
