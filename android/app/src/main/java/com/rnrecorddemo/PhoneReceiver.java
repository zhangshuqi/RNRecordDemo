package com.rnrecorddemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "tank";

    @Override
    public void onReceive(Context context, Intent intent) {

       /* if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是拨打电话
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "拨打电话");

        } else {

        }*/
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Service.TELEPHONY_SERVICE);
        switch (tManager.getCallState()) {

            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(TAG, "电话响铃");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // 电话响铃
                Log.i(TAG, "来电接通 或者 去电");

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                // 电话挂断
                try {
                    if (RecordModule.reactContext != null) {
                        Log.i(TAG, "reactContext not null");
                        sendEvent(RecordModule.reactContext, "onCallFinish", "");
                    } else
                        Log.i(TAG, "reactContext null");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public static void sendEvent(ReactContext reactContext, String eventName, String params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
