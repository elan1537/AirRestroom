package com.air.restroom;
/**
 * Created by Pengpark on 2016. 8. 17..
 */
public class SeoulToilet {
    private String POI_ID;
    private String FNAME;
    private String ANAME;
    private float X;
    private float Y;

    public SeoulToilet(String POI_ID, String FNAME, String ANAME, float X, float Y) {
        this.POI_ID = POI_ID;
        this.FNAME = FNAME;
        this.ANAME = ANAME;
        this.X = X;
        this.Y = Y;
    }

    public String getPOI_ID() {
        return POI_ID;
    }

    public void setPOI_ID(String POI_ID) {
        this.POI_ID = POI_ID;
    }

    public String getFNAME() {
        return FNAME;
    }

    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }

    public String getANAME() {
        return ANAME;
    }

    public void setANAME(String ANAME) {
        this.ANAME = ANAME;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "SeoulToilet{" +
                "POI_ID=" + POI_ID +
                ", FNAME='" + FNAME + '\'' +
                ", ANAME='" + ANAME + '\'' +
                ", X=" + X +
                ", Y=" + Y +
                '}';
    }


}
