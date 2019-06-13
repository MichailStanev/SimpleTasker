package com.michailstanev.simpletasker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.michailstanev.simpletasker.MainActivity.MainActivity;

import static com.michailstanev.simpletasker.StaticConstants.IN_LOCATION_FENCE_KEY;

public class UnRegisterFence{
    private static final String TAG = "Fucking TAG";

    private Context mContext;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }



    public void unregisterFence(String fenceKey , Context context) {
        Log.d(TAG, "unregisterFence: " + context);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .build();
        mGoogleApiClient.connect();

        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(fenceKey)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " successfully removed.");
                //mGoogleApiClient.disconnect();
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " could NOT be removed.");
//               mGoogleApiClient.disconnect();
            }
        });

        //mGoogleApiClient.disconnect();
    }
}
