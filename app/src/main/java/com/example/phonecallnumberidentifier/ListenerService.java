package com.example.phonecallnumberidentifier;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

public class ListenerService extends Service {
    private TelephonyManager telephony;
    private PhoneCallListener listener;

    public ListenerService() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(MainActivity.TAG, "onStartCommand");
        listener = new PhoneCallListener();
        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephony.listen(listener, PhoneStateListener.LISTEN_NONE);
        stopSelf();
    }

    public static class PhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.e(MainActivity.TAG, "CHANGE OF: " + incomingNumber);
            Log.e(MainActivity.TAG, "NEW STATE: " + state);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
