package com.rnrecorddemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class RecordModule extends ReactContextBaseJavaModule {
    public  static ReactApplicationContext reactContext;

    public RecordModule(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    /**
     * @return js调用的模块名
     */
    @NonNull
    @Override
    public String getName() {
        return "RecordModule";
    }

    /**
     * @return js调用 设置文件存储路径
     */
    @ReactMethod
    public void setRecordFilePath(String filePath) {
        RecordManager.getInstance().setRecordFilePath(filePath);
        Toast.makeText(reactContext, filePath, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return js调用 获取录音文件的list
     */
    @ReactMethod
    public void getRecordFileList(Promise promise) {

        RecordManager.getInstance().getRecodeFileList(reactContext,promise);


    }

    /**
     * @return js调用  跳转进入设置打开自动通话
     */
    @ReactMethod
    public void toOpenAutoRecordSetting() {

        ComponentName componentName = new ComponentName("com.android.phone", "com.android.phone.CallFeaturesSetting");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactContext.startActivity(intent);
    }

    @ReactMethod
    public void uploadRecordFileSuccess(List<String> filePathList) {
        RecordManager.getInstance().removeFile(filePathList);
    }

    /**
     * @return js调用   获取Android 手机 系统版本名字
     */
    @ReactMethod
    public void getPhoneDeviceName(Promise promise) {
        String deviceName = "";
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        if (manufacturer.equals("Huawei".toUpperCase())) {
            deviceName = (manufacturer + "_" + getPhoneDeviceName("ro.build.version.emui")).toUpperCase();
        } else if (manufacturer.equals("xiaomi".toUpperCase())) {
            deviceName = manufacturer + "_miui" + getPhoneDeviceName("ro.miui.ui.version.name").toUpperCase();
        }
        promise.resolve(deviceName);
       // sendEvent(reactContext, "onCallFinish", "");

    }

    public static void sendEvent(ReactContext reactContext, String eventName, String params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    public static String getPhoneDeviceName(String phoneType) {
        Class<?> classType = null;
        String emui = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            //   emui = (String) getMethod.invoke(classType, new Object[]{"ro.build.version.emui"});
            emui = (String) getMethod.invoke(classType, new Object[]{phoneType});

         /*   String mode = (String) getMethod.invoke(classType, new Object[]{"ro.miui.has_handy_mode_sf"});
            Logger.d("phone mode"+mode);
            String blur = (String) getMethod.invoke(classType, new Object[]{"ro.miui.has_real_blur"});
            Logger.d("phone blur"+ blur);

            String name = (String) getMethod.invoke(classType, new Object[]{"ro.miui.ui.version.name"});
            Logger.d("phone name"+ name);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emui;
    }

}
