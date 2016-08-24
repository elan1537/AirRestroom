package com.air.restroom;

import android.app.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.util.ArrayList;

/**
 * Created by kms7530 on 16. 8. 17..
 */
public class CloudAdapter implements CalloutBalloonAdapter {
    private View view;

    private String title, st1, st2, st3;
    private int img1, img2, img3;

    private ArrayList<SeoulToilet> list;

    public CloudAdapter(Activity activity, ArrayList<SeoulToilet> list){
        this.view = activity.getLayoutInflater().inflate(R.layout.ballon, null);
        this.list = list;
    }

    @Deprecated
    public void init(String title, int img1, int img2, int img3, String st1, String st2, String st3) {
        this.st1 = st1;
        this.st2 = st2;
        this.st3 = st3;
        this.title = title;

        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        MapPoint.GeoCoordinate point = mapPOIItem.getMapPoint().getMapPointGeoCoord();

        double longitude = point.longitude;
        double latitude = point.latitude;

        SeoulToilet toilet = null;

        for(SeoulToilet toilet1 : list) {
            if(toilet1.getX() == latitude && toilet1.getY() == longitude) {
                toilet = toilet1;
            }
        }

        TextView tv1 = (TextView)view.findViewById(R.id.txt_st1);
        TextView tv2 = (TextView)view.findViewById(R.id.txt_st2);
        TextView tv3 = (TextView)view.findViewById(R.id.txt_st3);
        TextView title = (TextView)view.findViewById(R.id.txt_title);

        ImageView img1_ = (ImageView)view.findViewById(R.id.image_person);
        ImageView img2_ = (ImageView)view.findViewById(R.id.image_disable);
        ImageView img3_ = (ImageView)view.findViewById(R.id.image_isopen);

        tv3.setText(toilet.getANAME());
        title.setText(toilet.getFNAME());

        img1_.setImageResource(this.img1);
        img2_.setImageResource(this.img2);
        img3_.setImageResource(this.img3);

        return view;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        MapPoint.GeoCoordinate point = mapPOIItem.getMapPoint().getMapPointGeoCoord();

        double longitude = point.longitude;
        double latitude = point.latitude;

        SeoulToilet toilet = null;

        for(SeoulToilet toilet1 : list) {
            if(toilet1.getX() == latitude && toilet1.getY() == longitude) {
                toilet = toilet1;
            }
        }

        TextView tv1 = (TextView)view.findViewById(R.id.txt_st1);
        TextView tv2 = (TextView)view.findViewById(R.id.txt_st2);
        TextView tv3 = (TextView)view.findViewById(R.id.txt_st3);
        TextView title = (TextView)view.findViewById(R.id.txt_title);

        ImageView img1_ = (ImageView)view.findViewById(R.id.image_person);
        ImageView img2_ = (ImageView)view.findViewById(R.id.image_disable);
        ImageView img3_ = (ImageView)view.findViewById(R.id.image_isopen);

        tv3.setText(toilet.getANAME());
        title.setText(toilet.getFNAME());

        img1_.setImageResource(this.img1);
        img2_.setImageResource(this.img2);
        img3_.setImageResource(this.img3);

        return view;
    }
}
