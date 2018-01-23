package com.serotonin.bacnet4j.npdu.uart;


public class UART {

    public final static int XTAL = 1843200;

    public final static int BAUDRATE_115200 = 115200; //1.8432M晶振不可用
    public final static int BAUDRATE_57600 = 57600;
    public final static int BAUDRATE_38400 = 38400;
    public final static int BAUDRATE_19200 = 19200;
    public final static int BAUDRATE_9600 = 9600;

    /**
     * 数据位长度
     */
    public final static byte WORD_LENGTH_5_BIT = (byte) (0b00000000);
    public final static byte WORD_LENGTH_6_BIT = (byte) (0b00000001);
    public final static byte WORD_LENGTH_7_BIT = (byte) (0b00000010);
    public final static byte WORD_LENGTH_8_BIT = (byte) (0b00000011);


    public final static byte THR_EMPTY_1 = (byte) (0b00100000);
    public final static byte DATA_IN_RECEIVER_1 = (byte) (0b00000001);

    /**
     * 停止位
     */
    public final static byte STOP_BIT_1 = (byte) (0b00000000);
    public final static byte STOP_BIT_2 = (byte) (0b00000100);
    public final static byte STOP_BIT_1_5 = (byte) (0b00000100);

    /**
     * 奇偶校验
     */
    public final static byte PARITY_ENABLE = (byte) (0b00001000);
    public final static byte PARITY_DISABLE = (byte) (0b00000000);
    public final static byte PARITY_TYPE_EVEN = (byte) (0b00010000);
    public final static byte PARITY_TYPE_ODD = (byte) (0b00000000);

    /**
     *
     */
    public final static byte BREAK_CTL_BIT_EN = (byte) (0b01000000);
    public final static byte BREAK_CTL_BIT_DIS = (byte) (0b00000000);

    /**
     * 除数锁
     */
    public final static byte DIVISOR_EN = (byte) (0b10000000);
    public final static byte DIVISOR_EX = (byte) (0b00000000);

    /**
     * 打开增强控制器
     */
    public final static byte ENHANCED_FEATURE_REGISTER_ENTER = (byte) (0b10111111);
    public final static byte ENHANCED_FEATURE_REGISTER_EXIT = (byte) (0b00000000);

    /**
     *调制解调器
     */
    public final static byte CLOCK_DIVISOR_1 = (byte) (0b00000000);
    public final static byte CLOCK_DIVISOR_4 = (byte) (0b10000000);

    public final static byte IRDA_MODE = (byte) (0b01000000);

    public final static byte XON_ANY_ON = (byte) (0b00100000);
    public final static byte XON_ANY_OFF = (byte) (0b00000000);

    public final static byte LOOPBACK_ENABLE = (byte) (0b00010000);
    public final static byte LOOPBACK_DISABLE = (byte) (0b00000000);

    public final static byte TCR_TLR_ENABLE = (byte) (0b00000100);
    public final static byte TCR_TLR_DISABLE = (byte) (0b00000000);

    public final static byte AUTO_RTS = (byte) (0b00000010);
    public final static byte CLOSE_RTS = (byte) (0b00000000);

    public final static byte AUTO_DTR = (byte) (0b00000001);
    public final static byte CLOSE_DTR = (byte) (0b00000000);

    /**
     * FCR FIFO控制寄存器
     */
    public final static byte RX_FIFO_8_CHAR = (byte) (0b00000000);
    public final static byte RX_FIFO_16_CHAR = (byte) (0b01000000);
    public final static byte RX_FIFO_56_CHAR = (byte) (0b10000000);
    public final static byte RX_FIFO_60_CHAR = (byte) (0b11000000);

    public final static byte TX_FIFO_8_SPACE = (byte) (0b00000000);
    public final static byte TX_FIFO_16_SPACE = (byte) (0b00010000);
    public final static byte TX_FIFO_32_SPACE = (byte) (0b00100000);
    public final static byte TX_FIFO_56_SPACE = (byte) (0b00110000);

    public final static byte RESET_TX_FIFO = (byte) (0b00000100);
    public final static byte RESET_RX_FIFO = (byte) (0b00000010);

    public final static byte FIFO_ENABLE = (byte) (0b00000001);
    public final static byte FIFO_DISABLE = (byte) (0b00000000);

    /**
     * 增强控制器
     */
    public final static byte CTS_FLOW_CTL_ENABLE = (byte) (0b10000000);
    public final static byte CTS_FLOW_CTL_DISABLE = (byte) (0b00000000);
    public final static byte RTS_FLOW_CTL_ENABLE = (byte) (0b01000000);
    public final static byte RTS_FLOW_CTL_DISABLE = (byte) (0b00000000);

    /**
     * 特殊字符检测
     */
    public final static byte SPECIAL_CHAR_DETECT = (byte) (0b00100000);

    /**
     * 增强使能
     */
    public final static byte EF_ENABLE = (byte) (0b00010000);
    public final static byte EF_DISABLE = (byte) (0b00000000);


}
