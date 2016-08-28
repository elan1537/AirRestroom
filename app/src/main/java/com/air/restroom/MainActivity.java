package com.air.restroom;

import android.content.Intent;
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
    double[] coords;
    ParseData data;
    MapView mapView;

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
}


//        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]), 7, true);
//        CloudAdapter adapter = new CloudAdapter();
//        adapter.init(this, "lala", R.drawable.abc_btn_radio_material, R.drawable.abc_btn_radio_material,
//                R.drawable.abc_btn_radio_material, "lala", "lala", "lala");
//        mapView.setCalloutBalloonAdapter(adapter);