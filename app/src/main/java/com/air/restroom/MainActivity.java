package com.air.restroom;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Button button, b2, b3;
    parseData parsing;
    PhoneSettings settings;
    ArrayList<SeoulToilet> toilets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new PhoneSettings(getApplicationContext());
        parsing = new parseData();
        button = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsing.getToiletData(toilets);
                System.out.println(toilets.toString());
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mac = settings.getMac();
                Toast.makeText(getApplicationContext(), mac, Toast.LENGTH_LONG).show();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            JSONObject json = new JSONObject(parsing.SendGeoInform());
                            System.out.println("lat : " + json.get("lat") + ", lng : " + json.get("lng") + "accuracy : " + json.get("accuracy"));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsing.gps();
            }
        });

    }
}
