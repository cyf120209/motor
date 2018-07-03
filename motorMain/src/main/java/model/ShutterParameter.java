package model;

public class ShutterParameter {

    private double shutterWidth;
    private double shutterClearance;
    private double shutterInclination;
    private double shutterAzimuth;

    public ShutterParameter(double shutterWidth, double shutterClearance, double shutterInclination, double shutterAzimuth) {
        this.shutterWidth = shutterWidth;
        this.shutterClearance = shutterClearance;
        this.shutterInclination = shutterInclination;
        this.shutterAzimuth = shutterAzimuth;
    }

    public double getShutterWidth() {
        return shutterWidth;
    }

    public void setShutterWidth(double shutterWidth) {
        this.shutterWidth = shutterWidth;
    }

    public double getShutterClearance() {
        return shutterClearance;
    }

    public void setShutterClearance(double shutterClearance) {
        this.shutterClearance = shutterClearance;
    }

    public double getShutterInclination() {
        return shutterInclination;
    }

    public void setShutterInclination(double shutterInclination) {
        this.shutterInclination = shutterInclination;
    }

    public double getShutterAzimuth() {
        return shutterAzimuth;
    }

    public void setShutterAzimuth(double shutterAzimuth) {
        this.shutterAzimuth = shutterAzimuth;
    }
}
