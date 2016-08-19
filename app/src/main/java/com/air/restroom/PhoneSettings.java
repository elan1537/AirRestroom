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
    TelephonyManager manager;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    GsmCellLocation gsmCellLocation;
    LocationManager locationManager;

    public PhoneSettings(Context context) {
        this.context = context;
//        gsmCellLocation = (GsmCellLocation) manager.getCellLocation();
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public PhoneSettings() {

    }

    public String getMac() {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    public ArrayList<String> getMcc() {
        ArrayList<String> Mcc_Mcn = new ArrayList<>();
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String networkOperator = manager.getSimOperator();
        Mcc_Mcn.add(networkOperator.substring(0, 3));
        Mcc_Mcn.add(networkOperator.substring(3));

        return Mcc_Mcn;
    }
}
