package test;

import util.STExecutor;

/**
 * Created by lenovo on 2017/9/28.
 */
public class ThreadPool {

    static int j=0;
    public static void main(String[] args) {
        for (j=0;j<100;j++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < 9; i++) {
//                                System.out.println("i=" + i);
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//                    thread.start();
                    for (int i = 0; i < 9; i++) {
                        System.out.println("j="+j+" i="+ i);
//                        if (i == 6) {
//                            try {
//                                thread.join();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                }
            };

//        new Thread(runnable).start();
            STExecutor.submit(runnable);
        }
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
