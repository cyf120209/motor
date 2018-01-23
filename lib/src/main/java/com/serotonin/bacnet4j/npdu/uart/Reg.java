package com.serotonin.bacnet4j.npdu.uart;

/**
 * SC16IS740 寄存器地址
 */
public class Reg {

    /**
     * 读取寄存器 ，接收寄存器
     */
    public final static byte RHR = (byte) 0x00;

    /**
     * 写入寄存器 ，发送寄存器
     */
    public final static byte THR = (byte) 0x00;

    /**
     * 中断寄存器
     */
    public final static byte IER = (byte) 0x01;

    public final static byte FCR = (byte) 0x02;
    public final static byte IIR = (byte) 0x02;
    public  final static byte LCR = (byte) 0x03;
    public  final static byte MCR = (byte) 0x04;
    public  final static byte LSR = (byte) 0x05;
    public  final static byte MSR = (byte) 0x06;
    public  final static byte TCR = (byte) 0x06;
    public  final static byte SPR = (byte) 0x07;
    public  final static byte TLR = (byte) 0x07;
    public  final static byte TXLVL = (byte) 0x08;
    public  final static byte RXLVL = (byte) 0x09;

    public final static byte EFCR = (byte) 0x0F;

    public final static byte DLL = (byte) 0x00;
    public final static byte DLH = (byte) 0x01;
    public final static byte EFR = (byte) 0x02;
    public final static byte XON1 = (byte) 0x04;
    public final static byte XON2 = (byte) 0x05;
    public final static byte XOFF1 = (byte) 0x06;
    public final static byte XOFF2 = (byte) 0x07;

}
