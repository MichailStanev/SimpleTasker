package com.michailstanev.simpletasker.MainActivity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class WriteToDatabase {


    public void insertOne(String name , String type , double lat , double lon , int radius , int dwell , double startTime , double endTime){

        //Initiating Stitch
        StitchAppClient stitchAppClient = Stitch.getDefaultAppClient();

        final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(
                RemoteMongoClient.factory , "mongodb-atlas");

        RemoteMongoCollection myCollection = mongoClient.getDatabase(
                "FenceTasker").getCollection("Fences");

        //Creating document for import
        Document newItem = new Document()
                .append("name", name)
                .append("type", type)
                .append("latitude", lat)
                .append("longtitude", lon)
                .append("radius", radius)
                .append("dwell", dwell)
                .append("startTime", startTime)
                .append("endTime", endTime)
                ;


        final Task<RemoteInsertOneResult> insertTask = myCollection.insertOne(newItem);
        insertTask.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@NonNull Task <RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.d("app", String.format("successfully inserted item with id %s",
                            task.getResult().getInsertedId()));
                } else {
                    Log.e("app", "failed to insert document with: ", task.getException());
                }
            }
        });
    }
}
