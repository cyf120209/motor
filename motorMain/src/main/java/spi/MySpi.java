package spi;

import org.free.bacnet4j.util.SerialParameters;

public class MySpi {

    public static void main(String[] args) {
        SerialParameters parameters = new SerialParameters();
        parameters.setBaudRate(9600);
    }

}
