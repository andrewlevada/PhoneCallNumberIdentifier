package com.example.phonecallnumberidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "NUMBERIDENTIFIER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPermissions()) return;

        Log.e(TAG, "Init service?");

        if (!isServiceRunning(ListenerService.class, this)) {
            Log.e(TAG, "Yes");
            startService(new Intent(this, ListenerService.class));
        }
    }

    private boolean checkPermissions() {
        int sdk = Build.VERSION.SDK_INT;

        List<String> requestedPermissions = new ArrayList<>();

        if (sdk >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestedPermissions.add(Manifest.permission.READ_PHONE_STATE);
                requestedPermissions.add(Manifest.permission.CALL_PHONE);
            }
        }

        if (sdk >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_DENIED) {
                requestedPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS);
                requestedPermissions.add(Manifest.permission.READ_PHONE_NUMBERS);
            }
        }

        if (requestedPermissions.size() != 0) {
            requestPermissions(requestedPermissions.toArray(new String[0]), 0);
            return false;
        }

        return true;
    }

    private static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) return false;

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}