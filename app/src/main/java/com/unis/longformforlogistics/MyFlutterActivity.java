//package com.unis.longformforlogistics;
//
//import android.content.BroadcastReceiver;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.BatteryManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.unis.longformforlogistics.plugin.FlutterPlugin;
//import com.unis.longformforlogistics.plugin.MyGeneratedPluginRegistrant;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
////import io.flutter.embedding.android.FlutterActivity;
//import io.flutter.app.FlutterActivity;
//import io.flutter.plugin.common.EventChannel;
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.view.FlutterView;
//import io.flutter.facade.Flutter;
//import io.flutter.plugins.GeneratedPluginRegistrant;
//
///**
// * Author:Arnold
// * Date:2019/11/6 11:12
// */
//public class MyFlutterActivity extends AppCompatActivity {
//
//    private static final String BATTERY_CHANNEL = "samples.flutter.io/battery";
//    private static final String CHARGING_CHANNEL = "samples.flutter.io/charging";
//    private FlutterView flutterView =null;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        flutterView = Flutter.createView(this, getLifecycle(), "route1");
//        addContentView(flutterView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        MyGeneratedPluginRegistrant.registerWith(flutterView.getPluginRegistry());
//
////        FlutterPlugin.getSinkEvent().success("sink sucess!!!");
//
////        initPlatformChannels();
//    }
//
//    private void initPlatformChannels() {
//        new MethodChannel(flutterView, BATTERY_CHANNEL).setMethodCallHandler(
//                new MethodChannel.MethodCallHandler() {
//                    @Override
//                    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
//                        if (methodCall.method.equals("getBatteryLevel")) {
//                            int batteryLevel = getBatteryLevel();
//
//                            if (batteryLevel != -1) {
//                                result.success(batteryLevel);
//                            } else {
//                                result.error("UNAVAILABLE", "Battery level not available.", null);
//                            }
//                        } else {
//                            result.notImplemented();
//                        }
//                    }
//                }
//        );
//
//        new EventChannel(flutterView, CHARGING_CHANNEL).setStreamHandler(
//                new EventChannel.StreamHandler() {
//                    private BroadcastReceiver chargingStateChangeReceiver;
//                    @Override
//                    public void onListen(Object arguments, EventChannel.EventSink events) {
//                        events.success("charging");
//                    }
//
//                    @Override
//                    public void onCancel(Object arguments) {
//                    }
//                }
//        );
//    }
//
//    private int getBatteryLevel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
//            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        } else {
//            Intent intent = new ContextWrapper(getApplicationContext()).
//                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
//                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//        }
//    }
//}
