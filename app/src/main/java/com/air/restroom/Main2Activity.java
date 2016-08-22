package com.air.restroom;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.content.Intent;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class Main2Activity extends ActionBarActivity {
    Intent it;
    double[] coords;
    private static final String API_KEYS = "YOUR_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        it = getIntent();

        coords = it.getDoubleArrayExtra("coord");

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(API_KEYS);

        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.mapView);
        viewGroup.addView(mapView);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]), true);
        System.out.println(coords[0] + " " + coords[1]);
    }

}
