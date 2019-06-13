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
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Arrays;

import static com.michailstanev.simpletasker.MyFenceReceiver.FENCE_RECEIVER_ACTION;
import static com.michailstanev.simpletasker.StaticConstants.IN_LOCATION_FENCE_KEY;
import static com.michailstanev.simpletasker.StaticConstants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;

public class CheckForFence {
    private static final String TAG = "Fucking TAG";

//    private Context mContext;
//    public void setmContext(Context mContext) {
//        this.mContext = mContext;
//    }

    public void checkForFence(Context mContext) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        Awareness.FenceApi.queryFences(mGoogleApiClient,
                FenceQueryRequest.forFences(Arrays.asList(IN_LOCATION_FENCE_KEY)))
                .setResultCallback(new ResultCallback<FenceQueryResult>() {
                    @Override
                    public void onResult(@NonNull FenceQueryResult fenceQueryResult) {
                        if (!fenceQueryResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not query fence: " + IN_LOCATION_FENCE_KEY);
                            return;
                        }
                        FenceStateMap map = fenceQueryResult.getFenceStateMap();
                        if (map.getFenceKeys().contains(IN_LOCATION_FENCE_KEY)) {
                            Log.d(TAG, "onResult: Fence not registered" + map.getFenceKeys());
                            // Fence is not registered.
                        }
                    }
                });


    }
}
