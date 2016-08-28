package com.air.restroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button btn_find;
    private static final String DAP_KEY = "YOUR_DAUM_KEY";
    ParseData data;
    MapView mapView;
    Intent it;
    double[] coords;
    private static final String API_KEYS = "6e83ed1e757e4911a78748828489f975";
    MapPoint mapPoint_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(DAP_KEY);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.mapView);
        viewGroup.addView(mapView);

        btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);

        ArrayList<List> toilet = setToiletData();
        ArrayList<SeoulToilet> row = new ArrayList<>();

        for(int i=0; i<toilet.size(); i++) {
            row.addAll(toilet.get(i));
        }
        CloudAdapter cloudAdapter = new CloudAdapter(this, row);
        mapView.setCalloutBalloonAdapter(cloudAdapter);

        for(SeoulToilet toilet1 : row.subList(10, 20)) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("Default Marker");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(toilet1.getY(), toilet1.getX()));
            Log.d("lat, long", String.valueOf(toilet1.getX()) + " / " + String.valueOf(toilet1.getY()));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
        }

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]), true);
        mapView.setMapViewEventListener(mapViewEventListener);
        System.out.println(coords[0] + " " + coords[1]);
    }

    public void init() {
        data = new ParseData(getApplicationContext());

        new Thread() {
            @Override
            public void run() {
                try {
                    coords = getUserLocation();
                    mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]), 1, true);
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName("Default Marker");
                    marker.setTag(0);
                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]));
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    mapView.addPOIItem(marker);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public double[] getUserLocation() throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(data.SendGeoInform());
        JSONObject co = (JSONObject) jsonObject.get("location");
        double[] c;

        c = new double[]{
                Double.parseDouble(co.get("lat").toString()),
                Double.parseDouble(co.get("lng").toString())
        };

        return c;
    }

    public String loadJSONFromAsset(String filename){
        String json = null;
        try {
            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<List> setToiletData() {
        List<String> toilets = null;
        ArrayList<List> total =  null;
        try {
            int Length = this.getAssets().list("").length - 3;
            toilets = new ArrayList<>();

            for (int i = 0; i < Length; i++) {
                String file = "toilet" + String.valueOf(i + 1) + ".json";
                String json = loadJSONFromAsset(file);
                toilets.add(json);
            }

            for (int i = 0; i < 5; i++) {
                Log.d("ToiletData", toilets.get(i));
            }


            total = new ArrayList<>();

            for(String s : toilets) {
                ArrayList<SeoulToilet> toilet_raw = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(s);
                JSONObject Service = (JSONObject)jsonObject.get("SearchPublicToiletPOIService");
                JSONArray row = (JSONArray)Service.get("row");

                for(int i=0; i<row.length(); i++) {
                    JSONObject item = (JSONObject) row.get(i);
                    String POI_ID = item.get("POI_ID").toString();
                    String FNAME = item.get("FNAME").toString();
                    String ANAME = item.get("ANAME").toString();
                    float X = Float.parseFloat(item.get("X_WGS84").toString());
                    float Y = Float.parseFloat(item.get("Y_WGS84").toString());

                    SeoulToilet d = new SeoulToilet(POI_ID, FNAME, ANAME, X, Y);
                    toilet_raw.add(d);
                    Log.d("toilet_raw", toilet_raw.get(i).toString());
                }

                total.add(toilet_raw);
            }

            Log.d("total", "Length : " + total.size());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return total;
    }

    MapView.MapViewEventListener mapViewEventListener = new MapView.MapViewEventListener() {
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

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
            Log.d("SingleTapped", mapPoint.getMapPointGeoCoord().latitude + " " + mapPoint.getMapPointGeoCoord().longitude);

            mapPoint_ = mapPoint;
            Intent intent = new Intent(getApplicationContext(), Popup.class);
            startActivityForResult(intent, 1);

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 1 && resultCode==RESULT_OK){

            if(data.getStringExtra("isEnd").equals("y")){//use data

                Intent intent_info = new Intent(getApplicationContext(), Add_info.class);

                startActivityForResult(intent_info, 2);

            }
            else if(data.getStringExtra("isEnd").equals("n")){//use data
            }
        }

        if (requestCode == 2 && resultCode==RESULT_OK) {

            if(data.getStringExtra("isEnd").equals("y")) {

                int cnt=0;

                MapPOIItem marker = new MapPOIItem();
                marker.setItemName("Default Marker");
                marker.setTag(0);
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(mapPoint_.getMapPointGeoCoord().latitude, mapPoint_.getMapPointGeoCoord().longitude));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                mapView.addPOIItem(marker);

                SharedPreferences info = getSharedPreferences("info", MODE_PRIVATE);
                SharedPreferences.Editor editor = info.edit();

                editor.putString("mapPoint-X_"+String.valueOf(cnt), String.valueOf(mapPoint_.getMapPointGeoCoord().latitude));
                editor.putString("mapPoint-Y_"+String.valueOf(cnt), String.valueOf(mapPoint_.getMapPointGeoCoord().longitude));

                cnt++;

            }
        }
    }
}


//        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]), 7, true);
//        CloudAdapter adapter = new CloudAdapter();
//        adapter.init(this, "lala", R.drawable.abc_btn_radio_material, R.drawable.abc_btn_radio_material,
//                R.drawable.abc_btn_radio_material, "lala", "lala", "lala");
//        mapView.setCalloutBalloonAdapter(adapter);