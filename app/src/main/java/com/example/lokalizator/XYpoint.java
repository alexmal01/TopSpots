package com.example.lokalizator;

import java.io.Serializable;

public class XYpoint implements Serializable {
    double x, y;

    public XYpoint(double x, double y) {    //pojedynczaLokalizacjaIntent
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }



}
