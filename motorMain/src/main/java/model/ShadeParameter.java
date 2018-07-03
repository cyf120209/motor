package model;

public class ShadeParameter {

    private double shadeHeight;
    private double shadeLength;
    private double shadeShade;
    private double shadeInclination;
    private double shadeAzimuth;

    public ShadeParameter(double shadeHeight, double shadeLength, double shadeShade, double shadeInclination, double shadeAzimuth) {
        this.shadeHeight = shadeHeight;
        this.shadeLength = shadeLength;
        this.shadeShade = shadeShade;
        this.shadeInclination = shadeInclination;
        this.shadeAzimuth = shadeAzimuth;
    }

    public double getShadeHeight() {
        return shadeHeight;
    }

    public void setShadeHeight(double shadeHeight) {
        this.shadeHeight = shadeHeight;
    }

    public double getShadeLength() {
        return shadeLength;
    }

    public void setShadeLength(double shadeLength) {
        this.shadeLength = shadeLength;
    }

    public double getShadeInclination() {
        return shadeInclination;
    }

    public void setShadeInclination(double shadeInclination) {
        this.shadeInclination = shadeInclination;
    }

    public double getShadeAzimuth() {
        return shadeAzimuth;
    }

    public void setShadeAzimuth(double shadeAzimuth) {
        this.shadeAzimuth = shadeAzimuth;
    }

    public double getShadeShade() {
        return shadeShade;
    }

    public void setShadeShade(double shadeShade) {
        this.shadeShade = shadeShade;
    }
}
