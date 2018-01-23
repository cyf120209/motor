package com.serotonin.bacnet4j.npdu.uart;

import com.pi4j.io.spi.SpiDevice;

import java.io.IOException;
import java.io.OutputStream;

public class UARTOutputStream extends OutputStream{

    // SPI device
    public SpiDevice spi_0 = null;

    public UARTInputStream inputStream = null;

    public UARTOutputStream(SpiDevice spi, UARTInputStream inputStream) {
        this.spi_0 = spi;
        this.inputStream=inputStream;
    }

    public byte[] write(byte address, byte value) throws IOException {
        byte start = (byte) (address << 3);
        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[]{
                start,                              // first byte, start bit
                value                               // third byte transmitted....don't care
        };
        // send conversion request to ADC chip via SPI channel
        byte[] result = spi_0.write(data);
        return result;
    }

    public void writeConfig(byte address, byte value) throws IOException {
        byte start = (byte) (address << 3);
        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[]{
                start,                              // first byte, start bit
                value                               // third byte transmitted....don't care
        };
        // send conversion request to ADC chip via SPI channel
        spi_0.write(data);
    }

    /**
     *
     * @return 如果为true， 说明发送寄存器内部有数据
     *          如果为flase， 说明发送寄存器内部没数据
     * @throws IOException
     */
    private boolean readTHR() throws IOException {
        boolean b = (byte) (read(Reg.LSR) & UART.THR_EMPTY_1) != UART.THR_EMPTY_1;
//        System.out.println("------------"+b);
        return b;
    }

    @Override
    public void write(int b) throws IOException {
        inputStream.setInputThread(false);
        spi_0.write((byte) b);
        inputStream.setInputThread(true);
    }

    public void write(byte[] value) throws IOException {
        int sent = 0;
        int size= 30;
        //关闭接收
        inputStream.setInputThread(false);
        while (sent < value.length) {
            boolean sentStatue = false;

            //读取发送寄存器状态，如果忙，读取发送寄存器剩余空间，若大于设定值(size)，就发送size个字节
            while (readTHR() && !sentStatue){
                if (read(Reg.TXLVL) > size){
                    sentStatue = true;
                }
                //中断
//                if ((read(Reg.IIR)&0x02) != 0x02) {
//                    size = 10;
//                }
            }

            if (value.length - sent > size) {
                byte[] tmp = new byte[size+1];
                tmp[0]=0;
                System.arraycopy(value, sent, tmp, 1, size);
                // send conversion request to ADC chip via SPI channel
                writeBuffer(tmp);
                sent+=size;
            } else {
                byte[] tmp = new byte[value.length-sent+1];
                tmp[0]=0;
                System.arraycopy(value, sent, tmp, 1, value.length-sent);
                // send conversion request to ADC chip via SPI channel
                writeBuffer(tmp);
                sent=value.length;
            }
        }
        //数据发送完，打开接收
        inputStream.setInputThread(true);
    }

    private void writeBuffer(byte[] tmp) throws IOException {
//        try {
//            long last_nano = System.nanoTime();
            spi_0.write(tmp);
//            long nano = System.nanoTime();
//            System.out.println(String.format("nano-last_nano_output         % 10d",(nano-last_nano)));
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public byte read(byte address) throws IOException {
        byte start = (byte) (0b10000000 | (address << 3));
        // create a data buffer and initialize a conversion request payload
        byte data[] = new byte[]{
                start,                              // first byte, start bit
                (byte) 0b11111111                               // third byte transmitted....don't care
        };
        // send conversion request to ADC chip via SPI channel
        byte[] result = spi_0.write(data);
        return result[1];
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }
}
