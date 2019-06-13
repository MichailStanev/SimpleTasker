package com.michailstanev.simpletasker;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import static com.michailstanev.simpletasker.StaticConstants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;

public class RegisterFence {
    private static final String TAG = "Fucking TAG";

    private Context mContext;
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void registerFence(String name , double lat , double lon , int radius , int dwellTime , double starTime , double endTime) {
        Log.d(TAG, "registerFence: context is " + mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "registerFence: Permission Error");
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {

            //Initialising a googleApiClient
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(Awareness.API)
                    .build();
            mGoogleApiClient.connect();

            String fenceRecieverAction = "com.hitherejoe.aware.ui.fence.LocationFenceReceiver.FENCE_RECEIVER_ACTION";

            Intent intent = new Intent(fenceRecieverAction);
            PendingIntent myPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

            //Creating fence
//            double lat = 43.243423;
//            double lon = 27.880923;
//            int radius = 200;
//            int dwellTime = 15;

            AwarenessFence inLocationFence = LocationFence.in(lat, lon, radius, dwellTime);

            Awareness.FenceApi.updateFences(
                    mGoogleApiClient,
                    new FenceUpdateRequest.Builder()
                            .addFence(name, inLocationFence, myPendingIntent)
                            .build())
                    .setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d(TAG, "onResult: Register Success! name: " + name + " lat/long: " + lat +"/"+lon + " radius: " + radius);
                            //mGoogleApiClient.disconnect();
                        } else {
                            Log.d(TAG, "onResult: Register Fail");
//                            mGoogleApiClient.disconnect();
                        }
                    });

            //mGoogleApiClient.disconnect();
        }
    }
}
