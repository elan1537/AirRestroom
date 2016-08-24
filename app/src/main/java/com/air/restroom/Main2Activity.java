package com.air.restroom;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.content.Intent;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import net.daum.mf.map.api.MapView;

public class Main2Activity extends ActionBarActivity {
    Intent it;
    double[] coords;
    private static final String API_KEYS = "6e83ed1e757e4911a78748828489f975";

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
        mapView.setMapViewEventListener(mapViewEventListener);
        System.out.println(coords[0] + " " + coords[1]);
    }

    MapViewEventListener mapViewEventListener = new MapViewEventListener() {
        @Override
        public void onMapViewInitialized(MapView mapView) {
            Log.d("Init", mapView.toString());
        }

        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
            Log.d("CenterPointMoved", mapPoint.getMapPointGeoCoord().toString());
        }

        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {

        }

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
            Log.d("SingleTapped", mapPoint.getMapPointGeoCoord().latitude + " " + mapPoint.getMapPointGeoCoord().longitude);
        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

        }
    };
}
