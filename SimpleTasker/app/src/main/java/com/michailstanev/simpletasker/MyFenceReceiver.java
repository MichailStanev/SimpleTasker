package com.michailstanev.simpletasker;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.fence.FenceState;
import com.michailstanev.simpletasker.MainActivity.MainActivity;

import static com.michailstanev.simpletasker.StaticConstants.IN_LOCATION_FENCE_KEY;

public class MyFenceReceiver extends BroadcastReceiver {
    private static final String TAG = "Testing";

    public static final int STATUS_IN = 0;
    public static final int STATUS_OUT = 1;
    public static final int STATUS_ENTERING = 2;
    public static final int STATUS_EXITING = 3;


    public static final String FENCE_RECEIVER_ACTION =
            "com.hitherejoe.aware.ui.fence.LocationFenceReceiver.FENCE_RECEIVER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent1 = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent1);
            return;
        }

        FenceState fenceState = FenceState.extract(intent);
        Log.d(TAG,  " onReceive: Recieved an intent: " + fenceState.getFenceKey()
                + "  Status: " + fenceState.getCurrentState()
                + " Key is: " +IN_LOCATION_FENCE_KEY);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context, "Boot completed", Toast.LENGTH_SHORT).show();
        }
//        if (TextUtils.equals(fenceState.getFenceKey(), IN_LOCATION_FENCE_KEY))
        if (StaticConstants.listOfKeys.contains(fenceState.getFenceKey())) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    final AudioManager mode2 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    mode2.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    Toast.makeText(context, "You are home", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onReceive: " + fenceState);
                    setPhoneState(STATUS_IN);
                    break;
                case FenceState.FALSE:
                    final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    Toast.makeText(context, "You not are home", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onReceive: " + fenceState);
                    setPhoneState(STATUS_OUT);
                    break;
                case FenceState.UNKNOWN:
                    break;
            }
        }
    }

    private void setPhoneState(int status) {
        switch (status) {
            case STATUS_IN:
                //mHeadphoneText.setText(R.string.text_at_home);
                Log.d(TAG, "setPhoneState: IN");
                break;
            case STATUS_OUT:
                //mHeadphoneText.setText(R.string.text_not_at_home);
                Log.d(TAG, "setPhoneState:  OUT");
                break;
            case STATUS_ENTERING:
                Log.d(TAG, "setPhoneState:  Entering");
                //mHeadphoneText.setText(R.string.text_entering_home);
                break;
            case STATUS_EXITING:
                //mHeadphoneText.setText(R.string.text_exiting_home);
                break;
        }
    }
}

