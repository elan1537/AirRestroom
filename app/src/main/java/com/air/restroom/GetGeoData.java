package com.air.restroom;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetGeoData extends Service {
    private ArrayList<SeoulToilet> seoulToilets;
    private double[] coords;
    CloudAdapter cloudAdapter;
    ParseData data;

    public ArrayList<SeoulToilet> getSeoulToilets() {
        return seoulToilets;
    }

    public double[] getCoords() {
        return coords;
    }

    public GetGeoData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        seoulToilets = new ArrayList<>();
        data = new ParseData(getApplicationContext());

        new Thread() {
            @Override
            public void run() {
                try {
                    coords = getUserLocation();
                    Log.d("onStartCommand", "Lat : " + coords[0] + ", Lng : " + coords[1]);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();


        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public double[] getUserLocation() throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(data.SendGeoInform());
        JSONObject coord = (JSONObject) jsonObject.get("location");
        double[] coords;

        coords = new double[]{
                Double.parseDouble(coord.get("lat").toString()),
                Double.parseDouble(coord.get("lng").toString())
        };

        return coords;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        GetGeoData getSystem() {
            return GetGeoData.this;
        }
    }
}
