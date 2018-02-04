package com.jiannapohotmail.com.project1.data.models;

import com.google.android.gms.maps.model.LatLng;


public class PointModel {
    private int id;
    private int pointPic, pointVideo;
    private String pointTitle;
    private String pointDescr;
    private double latitude, longitude;
    private boolean isActive;

    public PointModel(int pointPic, String pointTitle, String pointDescr) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
    }

    public PointModel(int pointPic, String pointTitle, String pointDescr, double latitude, double longitude, boolean isActive) {
        this.pointPic = pointPic;
        this.pointTitle = pointTitle;
        this.pointDescr = pointDescr;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    // video


}
