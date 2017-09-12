package limitsAndStops.presenter;

/**
 * Created by lenovo on 2017/2/11.
 */
public interface LimitsAndStopsPresenter {

    void extended();
    void stop();
    void retracted();
    void JOpen();
    void JClose();
    void upperLimit();
    void lowerLimit();
    void preStop();
    void nextStop();
    void addStop();
    void remoteStop();
    void remoteAllStop();
    void reverse();

    void getDraperInformation();
}
