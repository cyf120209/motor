package com.serotonin.bacnet4j.npdu.uart;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiDevice;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class UARTInputStream extends InputStream{

    // SPI device
    public SpiDevice spi = null;

    boolean isReturn=false;

    /**
     * 接收状态控制 true：打开接收 false：关闭接收
     */
    boolean isRead = true;

    byte start = (byte) (0b10000000 | (Reg.RHR << 3));

    byte readMaxCount = 56;


    public UARTInputStream(SpiDevice spi) {
        this.spi = spi;
//        readStream();
    }

    public void setInputThread(boolean aTrue) {
        isRead = aTrue;
    }

    public byte read(byte address) throws IOException {
        byte start = (byte) (0b10000000 | (address << 3));
        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[]{
                start,                              // first byte, start bit
                (byte) 0b11111111                               // third byte transmitted....don't care
        };
        // send conversion request to ADC chip via SPI channel
        byte[] result = spi.write(data);
        return result[1];
    }

//    public byte[] read() throws IOException {
//        byte start = (byte) (0b10000000 | (Reg.RHR << 3));
////        byte b = readRegisterValue(Reg.RHR);
////        System.out.println("readCache  " + b);
//        int count = 0;
//        ByteBuilder builder = new ByteBuilder();
////        while ((byte)(read(Reg.LSR) & UART.DATA_IN_RECEIVER_1) == UART.DATA_IN_RECEIVER_1) {
//        System.out.println("length start");
//        byte length = 0;
//        while ((length = read(Reg.RXLVL)) != 0) {
//            if (length > 32) {
//                length = 32;
//            }
//            count += length;
//            byte data[] = new byte[length + 1];
//            data[0] = start;
//            for (int i = 1; i < length + 1; i++) {
//                data[i] = (byte) 0xFF;
//            }
//            System.out.println("count " + count);
//            byte[] bytes = spi.write(data);
////            byte[] b=new byte[bytes.length-1];
////            System.arraycopy(bytes,1,b,0,bytes.length-1);
//            builder.append(bytes, 1);
////            byte[] bytes1 = builder.toArrays();
////            String binary = binary(bytes, 16);
////            System.out.println("readStream" + binary);
//        }
//
//        return builder.toArrays();
//    }

    public void readStream() {
        readInputStream.start();
    }

    Thread readInputStream = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("readStream start");
                while (true) {
                    if(isRead) {
                        byte length = 0;
                        int xiaoi = 0;
                        int count = 0;
                        ByteBuilder builder = new ByteBuilder();
//                    while ((length = read(Reg.RXLVL)) != 0 || (read(Reg.IIR)&0x04) == 0x04) {
                        //读取接收寄存器状态，如果有数据，读取接收寄存器数据大小并接收
                        while (((byte) (read(Reg.LSR) & UART.DATA_IN_RECEIVER_1) == UART.DATA_IN_RECEIVER_1) || ((read(Reg.RXLVL)) != 0)) {
                            isReturn = false;
                            length = read(Reg.RXLVL);
                            if (length > 56) {
                                length = 56;
                            }
                            count += length;
//                        System.out.println("read count  " + count);
                            byte data[] = new byte[length + 1];
                            data[0] = start;
                            for (int i = 1; i < length + 1; i++) {
                                data[i] = (byte) 0xFF;
                            }
                            byte[] bytes = spi.write(data);
                            builder.append(bytes, 1);

                            //没数据进入
                            while ((byte) (read(Reg.LSR) & UART.DATA_IN_RECEIVER_1) != UART.DATA_IN_RECEIVER_1 && !isReturn) {
                                xiaoi++;
                                if (xiaoi % 3 == 0) {
                                    if (read(Reg.RXLVL) > 0) {
                                        isReturn = true;
                                    }
                                }
                                if (xiaoi >= 50) {
                                    isReturn = true;
                                }
                            }
                        }
                        if (builder.toArrays().length > 1) {
                            SpiInterrupt.receivedData(builder.toArrays());
                        }
//                    }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    });

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if(b==null){
            throw new NullPointerException();
        }
        if(off<0|| len <0 || (off+len)>b.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        if(len==0) {
            return 0;
        }
        int length = read(Reg.RXLVL);
        int index=0;
        int xiaoi = 0;
        while (length>0){
            isReturn = false;
            byte data[] = new byte[length + 1];
            data[0] = start;
            for (int i = 1; i < length + 1; i++) {
                data[i] = (byte) 0xFF;
            }
            byte[] bytes = spi.write(data);

            if (index+length<=len){
                System.arraycopy(bytes,1,b,index,length);
            }
            index+=length;
            length = read(Reg.RXLVL);
            //没数据进入
            while ( length==0 && !isReturn) {
                xiaoi++;
                length = read(Reg.RXLVL);
                if (xiaoi >= 5) {
                    isReturn = true;
                }
            }
        }
        return (index > len) ? len : index;
    }

    @Override
    public int available() throws IOException {
        if(isRead){
            return read(Reg.RXLVL);
        }else {
            return 0;
        }
    }
}
