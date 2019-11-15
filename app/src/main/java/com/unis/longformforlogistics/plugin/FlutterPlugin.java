//package com.unis.longformforlogistics.plugin;
//
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.BatteryManager;
//import android.os.Build;
//import android.widget.Toast;
//
//import com.unis.longformforlogistics.App;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import androidx.annotation.Nullable;
//import io.flutter.plugin.common.EventChannel;
//import io.flutter.plugin.common.MethodCall;
//import io.flutter.plugin.common.MethodChannel;
//import io.flutter.plugin.common.PluginRegistry;
//import io.flutter.plugin.common.PluginRegistry.Registrar;
//
//import static android.content.Context.BATTERY_SERVICE;
//
///**
// * Author:Arnold
// * Date:2019/11/11 17:33
// */
//public class FlutterPlugin implements MethodChannel.MethodCallHandler {
//    private static final String FLUTTER_PLUGIN_SEND = "flutter_send";
//    private static final String FLUTTER_PLUGIN_RECEIVE = "flutter_receive";
//
//    private Registrar registrar;
//    private static EventChannel.EventSink eventSink;
//
//    private FlutterPlugin(Registrar registrar) {
//        this.registrar = registrar;
//
//        EventChannel eventChannel = new EventChannel(registrar.messenger(),FLUTTER_PLUGIN_RECEIVE);
//        EventChannel.StreamHandler streamHandler = new EventChannel.StreamHandler() {
//            @Override
//            public void onListen(Object o, EventChannel.EventSink sink) {
//                eventSink = sink;
//            }
//
//            @Override
//            public void onCancel(Object o) {
//                eventSink = null;
//            }
//        };
//        eventChannel.setStreamHandler(streamHandler);
//    }
//
//    public static void registerWith(Registrar registrar) {
//        MethodChannel channel = new MethodChannel(registrar.messenger(), FLUTTER_PLUGIN_SEND);
//        channel.setMethodCallHandler(new FlutterPlugin(registrar));
//    }
//
//    public static EventChannel.EventSink getSinkEvent() {
//        return eventSink;
//    }
//
//    @Override
//    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
//        String method = methodCall.method;
//        switch (method) {
//            case "getPlatformVersion":
//                result.success("Android " + android.os.Build.VERSION.RELEASE);
//                break;
//            case "toast":
//                String msg = methodCall.argument("msg");
//                Toast.makeText(registrar.context(), msg, Toast.LENGTH_SHORT).show();
//                result.success("成功啦");
//                break;
//            case "getBatteryLevel":
//                int batteryLevel = getBatteryLevel();
//                result.success(batteryLevel);
//                break;
//            case "sink":
//                eventSink.success("sink");
//                break;
//            default:
//                result.notImplemented();
//                break;
//        }
//    }
//
//    private int getBatteryLevel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            BatteryManager batteryManager = (BatteryManager) registrar.context().getSystemService(BATTERY_SERVICE);
//            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//        } else {
//            Intent intent = new ContextWrapper(registrar.context()).
//                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
//                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//        }
//    }
//
//    private final Map<String, Handler> handlerMap = new HashMap<String, Handler>() {
//    };
//
//    @FunctionalInterface
//    interface Handler {
//        void call(Map<String, Object> args, MethodChannel.Result methodResult) throws Exception;
//    }
//}
