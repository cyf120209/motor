package test;

import util.STExecutor;

/**
 * Created by lenovo on 2017/9/28.
 */
public class ThreadPool {

    public static void main(String[] args) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                for (int i=0;i<9;i++){
                    System.out.println(""+i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
//        STExecutor.submit(runnable);
//        try {
//            boolean state = STExecutor.getState(runnable);
//            Thread.sleep(1000);
//            System.out.println(""+state);
//            boolean sate = STExecutor.getState(runnable);
//            Thread.sleep(2000);
//            STExecutor.shutdown(runnable);
//            System.out.println(""+sate);
//            boolean ste = STExecutor.getState(runnable);
//            Thread.sleep(1000);
//            System.out.println(""+ste);
//            boolean te = STExecutor.getState(runnable);
//            Thread.sleep(2000);
//            System.out.println(""+te);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }
}
