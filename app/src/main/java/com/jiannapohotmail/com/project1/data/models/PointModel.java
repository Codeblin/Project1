package com.jiannapohotmail.com.project1.data.models;

import com.google.android.gms.maps.model.LatLng;


public class PointModel {
    private int pointPic, pointVideo;
    private String pointTitle;
    private String pointDescr;
    private LatLng position;
    private boolean isActive;

    public PointModel(int pointPic, String pointTitle, String pointDescr) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
    }

    public PointModel(int pointPic, String pointTitle, String pointDescr, LatLng position, boolean isActive) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
        this.position = position;
        this.isActive = isActive;
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

    public int getPointVideo() {
        return pointVideo;
    }

    public void setPointVideo(int pointVideo) {
        this.pointVideo = pointVideo;
    }

    public boolean IsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    // video


}
