package main.view;

import com.pi4j.wiringpi.*;
import manager.impl.MyManager;
import util.Public;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by lenovo on 2017/1/18.
 */
public class Main{

    private BoxLayoutCase mBoxLayoutCase;


    public static void main(String[] args) throws Exception {
        String os = System.getProperty("os.name");
        if(!os.toLowerCase().startsWith("win")){
            try {
                initWritingPi();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        final Main main = new Main();
        main.guiInit();
        MyManager manager = new MyManager();
        manager.init();

    }

    private static void initWritingPi() throws Exception{
        GpioInterrupt.addListener(new GpioInterruptListener() {
            @Override
            public void pinStateChange(GpioInterruptEvent event) {
                System.out.println("Raspberry Pi PIN [" + event.getPin() +"] is in STATE [" + event.getState() + "]");

                if(event.getPin() == 4 && (!event.getState())) {
//                    Gpio.digitalWrite(6, event.getStateValue());
                    //关机
                    System.out.println(""+event.getState());
                    try {
                        Public.createShutdownScriptFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
//                Gpio.digitalRead(4);
//                if(event.getPin() == 0) {
//                    Gpio.digitalWrite(5, event.getStateValue());
//                }
            }
        });

        // setup wiring pi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // export all the GPIO pins that we will be using
        GpioUtil.export(4, GpioUtil.DIRECTION_IN);
//        GpioUtil.export(7, GpioUtil.DIRECTION_IN);
//        GpioUtil.export(5, GpioUtil.DIRECTION_OUT);
        GpioUtil.export(1, GpioUtil.DIRECTION_OUT);

        // set the edge state on the pins we will be listening for
        GpioUtil.setEdgeDetection(4, GpioUtil.EDGE_BOTH);
//        GpioUtil.setEdgeDetection(7, GpioUtil.EDGE_BOTH);

        // configure GPIO pins 5, 6 as an OUTPUT;
//        Gpio.pinMode(5, Gpio.OUTPUT);
        Gpio.pinMode(1, Gpio.OUTPUT);

        // configure GPIO 0 as an INPUT pin; enable it for callbacks
        Gpio.pinMode(4, Gpio.INPUT);
        Gpio.pullUpDnControl(4, Gpio.PUD_UP);
        GpioInterrupt.enablePinStateChangeCallback(4);

        Gpio.digitalWrite(1, Gpio.HIGH);
        int i = Gpio.digitalRead(4);
        System.out.println("i "+i);
//        int i18 = Gpio.digitalRead(18);
//        System.out.println("i18 "+i18);
    }


    private void guiInit() throws ParseException {
        mBoxLayoutCase = new BoxLayoutCase();
    }
}