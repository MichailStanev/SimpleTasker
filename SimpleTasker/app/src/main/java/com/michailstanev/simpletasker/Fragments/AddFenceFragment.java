package com.michailstanev.simpletasker.Fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.michailstanev.simpletasker.MainActivity.WriteToDatabase;
import com.michailstanev.simpletasker.R;
import com.michailstanev.simpletasker.RegisterFence;
import com.michailstanev.simpletasker.StaticConstants;

import org.w3c.dom.Text;


public class AddFenceFragment extends Fragment {
    Context mContext;
    Button goToMap , saveFence;
    TextInputEditText name , radius , dwell;
    TextView lat , lon;
    double dLat , dLon;
    String sName;
    int iRadius , iDwell;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_fence, container , false);

        goToMap = v.findViewById(R.id.getMarkerLocation);
        FragmentManager fm = getFragmentManager();
        goToMap.setOnClickListener(view -> {
            if (fm != null){
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container , new MyMapFragment());
                ft.commit();
            }
        });



        name = v.findViewById(R.id.input_name);

        radius = v.findViewById(R.id.input_radius);

        dwell = v.findViewById(R.id.input_dwell_time);

        lat = v.findViewById(R.id.textLat);
        lon = v.findViewById(R.id.textLong);
        if (StaticConstants.getMarkerLat() == 0 && StaticConstants.getMarkerLong() == 0){
            lat.setText(String.valueOf(StaticConstants.getCurrentLat()));
            lon.setText(String.valueOf(StaticConstants.getCurrentLong()));
            dLat=StaticConstants.getCurrentLat();
            dLon=StaticConstants.getCurrentLong();
        }else {
            lat.setText(String.valueOf(StaticConstants.getMarkerLat()));
            lon.setText(String.valueOf(StaticConstants.getMarkerLong()));
            dLat=StaticConstants.getMarkerLat();
            dLon=StaticConstants.getMarkerLong();
        }

        saveFence = v.findViewById(R.id.commitFence);
        saveFence.setOnClickListener(view -> {
            sName = String.valueOf(name.getText());
            iRadius = Integer.parseInt(radius.getText().toString());
            iDwell = Integer.parseInt(dwell.getText().toString());
            
            if (sName != null && iRadius != 0 && iDwell != 0) {
                WriteToDatabase writeToDatabase = new WriteToDatabase();
                writeToDatabase.insertOne(sName, "location", dLat, dLon, iRadius, iDwell, 0, 0);
                RegisterFence registerFence = new RegisterFence();
                registerFence.setmContext(getContext());
                registerFence.registerFence(sName, dLat, dLon, iRadius, iDwell, 0, 0);
            }else {
                Toast.makeText(mContext, "Enter all paramiter !", Toast.LENGTH_SHORT).show();
            }
        });






        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
}