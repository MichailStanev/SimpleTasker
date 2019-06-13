package com.michailstanev.simpletasker.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.michailstanev.simpletasker.MainActivity.MainActivity;
import com.michailstanev.simpletasker.R;
import com.michailstanev.simpletasker.RegisterFence;
import com.michailstanev.simpletasker.StaticConstants;
import com.michailstanev.simpletasker.UnRegisterFence;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    private Context mContext;

    LinearLayout mainLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fence_list , container , false);

        mainLinearLayout = (LinearLayout) v.findViewById(R.id.fenceListLayout);

        getFences();
        return v;
    }

    public void getFences(){
        //Initiating Stitch
        StitchAppClient stitchAppClient = Stitch.getDefaultAppClient();

        final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(
                RemoteMongoClient.factory , "mongodb-atlas");

        RemoteMongoCollection myCollection = mongoClient.getDatabase(
                "FenceTasker").getCollection("Fences");


        //Get all Fences form Database
        Document filterDoc = new Document()
                .append("name", new Document().append("$exists", true));

        RemoteFindIterable findResults = myCollection
                .find(filterDoc);


        // Another way to iterate through
        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Document> items = task.getResult();
                //Log.d("app", String.format("successfully found %d documents", items.size()));
                for (Document item: items) {

                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.END);
                    linearLayout.setPadding(0 , 40 , 0, 40);


                    TextView textView = new TextView(mContext);
                    textView.setTextSize(30);
                    textView.setText(String.valueOf(item.get("name")));


                    Button btnActivate = new Button(mContext);
                    btnActivate.setText("Activate");
                    btnActivate.setOnClickListener(view -> {
                        StaticConstants.addToActiveKeys(item.getString("name"));


                        RegisterFence registerFence = new RegisterFence();
                        registerFence.setmContext(getContext());
                        registerFence.registerFence(item.getString("name")
                                    , item.getDouble("latitude")
                                    , item.getDouble("longtitude")
                                    , item.getInteger("radius")
                                    , item.getInteger("dwell"), 0 , 0);
                    });

                    Button btnDisable = new Button(mContext);
                    btnDisable.setText("Disable");
                    btnDisable.setOnClickListener(view -> {
                        StaticConstants.removeFromActiveKeys(item.getString("name"));
                        UnRegisterFence unRegisterFence = new UnRegisterFence();
                        unRegisterFence.unregisterFence(String.valueOf(item.get("name")) , mContext);
//                      unRegisterFence.unregisterFence("name" , mContext);
                    });


                    linearLayout.addView(textView);
                        linearLayout.addView(btnActivate);
                        linearLayout.addView(btnDisable);

                    mainLinearLayout.addView(linearLayout);

                    }
            } else {
                Log.e("app", "failed to find documents with: ", task.getException());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =  context;
    }
}