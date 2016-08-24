package com.air.restroom;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class parseData extends PhoneSettings {
    int count = 0;
    Location lastLocation;
    LocationListener locationListener;
    String geoUrl = "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyCrnwlSqC7aZsHRu93y9eRJx6Jeteih2dw";

    public JSONArray getToilets() {
        return toilets;
    }

    JSONArray toilets;


    private double langitude;
    private double longitude;


    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public parseData(Context applicationContext) {
        super(applicationContext);
    }

    public Map<String, Object> setGeoInform() {
        List<Map<String, Object>> cellT;
        List<Map<String, Object>> wifi;
        Map<String, Object> wifiAccessPoints;
        Map<String, Object> cellTowers;
        Map<String, Object> CellInfo;

        try {
            cellT = new ArrayList<>();
            wifi = new ArrayList<>();
            wifiAccessPoints = new HashMap<>();
            cellTowers = new HashMap<>();
            CellInfo = new HashMap<>();

            ArrayList<String> mcc_mcn = getMcc();

            wifiAccessPoints.put("macAddress", getMac());
            wifiAccessPoints.put("signalStrength", wifiInfo.getRssi());

            cellTowers.put("cellId", String.valueOf(gsmCellLocation.getCid()));
            cellTowers.put("locationAreaCode", String.valueOf(gsmCellLocation.getLac()));
            cellTowers.put("mobileCountryCode", mcc_mcn.get(0));
            cellTowers.put("mobileNetworkCode", mcc_mcn.get(1));

            cellT.add(cellTowers);
            wifi.add(wifiAccessPoints);

            CellInfo.put("homeMobileCountryCode", 450);
            CellInfo.put("homeMobileNetworkCode", 5);
            CellInfo.put("radioType", "wcdma");
            CellInfo.put("carrier", "SKT");
            CellInfo.put("considerIp", "true");
            CellInfo.put("cellTowers", cellT);
            CellInfo.put("wifiAccessPoints", wifi);

        } catch(StringIndexOutOfBoundsException ex) {
            wifi = new ArrayList<>();
            CellInfo = new HashMap<>();
            wifiAccessPoints = new HashMap<>();

            wifiAccessPoints.put("macAddress", getMac());

            wifi.add(wifiAccessPoints);

            CellInfo.put("homeMobileCountryCode", 450);
            CellInfo.put("homeMobileNetworkCode", 5);
            CellInfo.put("radioType", "wcdma");
            CellInfo.put("carrier", "SKT");
            CellInfo.put("considerIp", "true");
            CellInfo.put("wifiAccessPoints", wifi);
        }

        return CellInfo;
    }

    public void gps() {
        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("Main", "isGPSEnabled=" + isGPSEnabled);
        Log.d("Main", "isNetworkEnabled="+ isNetworkEnabled);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                lastLocation = location;

                setLangitude(lat);
                setLongitude(lng);
                Log.d("Location", "lat : " + lat + ", lng : " + lng + "count : " + count++);
                locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }
    }

    public String SendGeoInform() throws IOException {
        Map cellInfo = setGeoInform();
        JSONObject data = new JSONObject(cellInfo);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(geoUrl);

        StringEntity stringEntity;

        stringEntity = new StringEntity(data.toString(), "utf-8");
        stringEntity.setContentType("application/json");

        httpPost.setEntity(stringEntity);

        HttpResponse response;

        response = httpClient.execute(httpPost);
        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity);
    }

//    public ArrayList<JSONArray> getToiletData() {
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        ResponseHandler handler = new ResponseHandler();
//        client.get(url, handler);
//
//        return handler.getList();
//    }

//    class ResponseHandler extends JsonHttpResponseHandler{
//        private ArrayList<JSONArray> list = new ArrayList<>();
//
//        public ResponseHandler() {
//        }
//
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            try {
//                JSONObject j = response.getJSONObject("SearchPublicToiletPOIService");
//                JSONArray row = j.getJSONArray("row");
//                Log.d("onSuccess", row.toString());
//
//                list.add(row);
//                Log.d("getList", list.get(0).toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public ArrayList<JSONArray> getList() {
//            Log.d("getList", list.get(0).toString());
//            return list;
//        }
//    }
}
