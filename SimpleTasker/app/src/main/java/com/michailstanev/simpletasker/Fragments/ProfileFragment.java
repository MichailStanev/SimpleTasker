package com.michailstanev.simpletasker.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.michailstanev.simpletasker.CheckForFence;
import com.michailstanev.simpletasker.MainActivity.WriteToDatabase;
import com.michailstanev.simpletasker.R;
import com.michailstanev.simpletasker.RegisterFence;
import com.michailstanev.simpletasker.StaticConstants;

import static android.content.ContentValues.TAG;


public class ProfileFragment extends Fragment {
    String[] test;
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        String name = "testing1";
//        String type = "locationFence";
//        double lat = 43.243423;
//        double lon = 27.880923;
//        int radius = 200;
//        int dwellTime = 15;
//        WriteToDatabase writeToDatabase = new WriteToDatabase();
//        writeToDatabase.insertOne(name , type, lat , lon ,radius , dwellTime ,0,0);


        CheckForFence checkForFence = new CheckForFence();
        checkForFence.checkForFence(mContext);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}