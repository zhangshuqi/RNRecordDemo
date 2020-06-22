package com.rnrecorddemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "tank";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是拨打电话
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "拨打电话");

        } else {
            // 如果是来电
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
                    Log.i(TAG, "电话挂断");
                    break;
            }
        }
    }
}
