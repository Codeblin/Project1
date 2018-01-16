package com.jiannapohotmail.com.project1;

import com.google.android.gms.maps.model.LatLng;
import com.jiannapohotmail.com.project1.R;


public class PointModel {
    private int pointPic;
    private String pointTitle;
    private String pointDescr;
    private LatLng position;

    public PointModel(int pointPic, String pointTitle, String pointDescr) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
    }

    public PointModel(int pointPic, String pointTitle, String pointDescr, LatLng position) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
        this.position = position;
    }

    public int getPointPic() {
        return pointPic;
    }

    public void setPointPic(int pointPic) {
        this.pointPic = pointPic;
    }

    public String getPointTitle() {
        return pointTitle;
    }

    public void setPointTitle(String pointTitle) {
        this.pointTitle = pointTitle;
    }

    public String getPointDescr() {
        return pointDescr;
    }

    public void setPointDescr(String pointDescr) {
        this.pointDescr = pointDescr;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
    // video


}
