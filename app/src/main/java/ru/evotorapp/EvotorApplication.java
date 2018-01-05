package ru.evotorapp;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by sergey-rush on 10.12.2017.
 */

public class EvotorApplication extends android.app.Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DataAccess dataAccess = DataAccess.getInstance(context);
        dataAccess.countSlips();
        validateDevice();
    }

    private void validateDevice() {
        String deviceId = LocalSettings.getDeviceId(context);
        if (deviceId.length() < 15) {
            deviceId = getDeviceId();
            if (deviceId.length() >= 15) {
                LocalSettings.setDeviceId(context, deviceId);
            }
        }
    }

    public String getDeviceId() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }
}
