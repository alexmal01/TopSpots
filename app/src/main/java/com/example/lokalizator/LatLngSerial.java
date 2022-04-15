package com.example.lokalizator;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LatLngSerial implements Serializable {
    LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LatLngSerial(LatLng latLng) {
        this.latLng = latLng;
    }
}
