package com.air.restroom;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.ArrayList;

public class PhoneSettings {
    Context context;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    GsmCellLocation gsmCellLocation;
    LocationManager locationManager;
    TelephonyManager manager;
    int count = 0;

    public PhoneSettings(Context context) {
        System.out.println("Count : " + count);
        count++;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        gsmCellLocation = (GsmCellLocation) manager.getCellLocation();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public PhoneSettings() {

    }


    public String getMac() {
        return wifiInfo.getMacAddress();
    }

    public ArrayList<String> getMcc() {
        ArrayList<String> Mcc_Mcn = new ArrayList<>();

        String networkOperator = manager.getSimOperator();
        Mcc_Mcn.add(networkOperator.substring(0, 3));
        Mcc_Mcn.add(networkOperator.substring(3));

        return Mcc_Mcn;
    }
}
