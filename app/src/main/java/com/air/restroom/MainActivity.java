package com.air.restroom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Button button, b2, b3, b4;
    parseData parsing;
    TextView textView;
    ArrayList<SeoulToilet> toilets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parsing = new parseData(getApplicationContext());

        button = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView);

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
                String mac = parsing.getMac();
                Toast.makeText(getApplicationContext(), mac, Toast.LENGTH_LONG).show();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            JSONObject json = new JSONObject(parsing.SendGeoInform());
                            System.out.println(json.toString());
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
                textView.setText("lng : " + String.valueOf(parsing.getLangitude()) + ", lat : " + String.valueOf(parsing.getLongitude()));
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] coord = {
                        parsing.getLangitude(),
                        parsing.getLongitude()
                };

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("coord", coord);
                startActivity(intent);
            }
        });
    }
}
