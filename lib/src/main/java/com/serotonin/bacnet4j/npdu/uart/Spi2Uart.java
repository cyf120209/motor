package com.serotonin.bacnet4j.npdu.uart;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

public class Spi2Uart {

    // SPI device
    public SpiDevice spi = null;
    private final UARTInputStream uartInputStream;
    private final UARTOutputStream uartOutputStream;
    public Spi2Uart() throws IOException {
        //        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED_400k, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        uartInputStream = new UARTInputStream(spi);
        uartOutputStream = new UARTOutputStream(spi, uartInputStream);
        writeConfig(Reg.LCR, UART.ENHANCED_FEATURE_REGISTER_ENTER);
        writeConfig(Reg.EFR, UART.EF_ENABLE);
        writeConfig(Reg.LCR, UART.ENHANCED_FEATURE_REGISTER_EXIT);
        writeConfig(Reg.IER, (byte) 0x10);
        writeConfig(Reg.MCR, UART.CLOCK_DIVISOR_1);
        writeConfig(Reg.LCR, UART.DIVISOR_EN);

        writeBaudRate(UART.BAUDRATE_115200, uartInputStream.read(Reg.MCR));

        writeConfig(Reg.LCR, (byte) (UART.WORD_LENGTH_8_BIT | UART.STOP_BIT_1 | UART.PARITY_DISABLE));
        writeConfig(Reg.FCR, (byte) (UART.RX_FIFO_60_CHAR | UART.TX_FIFO_56_SPACE | UART.FIFO_ENABLE));
        //auto RS485 set
        writeConfig(Reg.EFCR, (byte) 0x31);

        writeConfig(Reg.IER, (byte) 0x00);
    }

    public Spi2Uart(int speed,int baudrate) throws IOException {
        //        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                speed, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        uartInputStream = new UARTInputStream(spi);
        uartOutputStream = new UARTOutputStream(spi, uartInputStream);
        writeConfig(Reg.LCR, UART.ENHANCED_FEATURE_REGISTER_ENTER);
        writeConfig(Reg.EFR, UART.EF_ENABLE);
        writeConfig(Reg.LCR, UART.ENHANCED_FEATURE_REGISTER_EXIT);
        writeConfig(Reg.IER, (byte) 0x10);
        writeConfig(Reg.MCR, UART.CLOCK_DIVISOR_1);
        writeConfig(Reg.LCR, UART.DIVISOR_EN);

        writeBaudRate(baudrate, uartInputStream.read(Reg.MCR));

        writeConfig(Reg.LCR, (byte) (UART.WORD_LENGTH_8_BIT | UART.STOP_BIT_1 | UART.PARITY_DISABLE));
        writeConfig(Reg.FCR, (byte) (UART.RX_FIFO_60_CHAR | UART.TX_FIFO_56_SPACE | UART.FIFO_ENABLE));
        //auto RS485 set
        writeConfig(Reg.EFCR, (byte) 0x31);

        writeConfig(Reg.IER, (byte) 0x00);
    }

    public void writeConfig(byte address, byte value){
        try {
            uartOutputStream.writeConfig(address,value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public byte[] read() throws IOException {
//        return UARTInputStream.read();
//    }

    public void readStream() throws IOException {
        uartInputStream.readStream();
    }

    public byte read(byte address) throws IOException {
        return uartInputStream.read(address);
    }

    public void write(byte[] value) throws IOException {
        uartOutputStream.write(value);
    }

    public byte[] write(byte address, byte value) throws IOException {
        return uartOutputStream.write(address,value);
    }

    private int writeBaudRate(int baudRate, byte mcr) throws IOException {
        int mcr_7 = mcr & 0x80;
        int prescaler = 0;
        switch (mcr_7) {
            case 0:
                prescaler = 1;
                break;
            case 1:
                prescaler = 4;
                break;
            default:
                prescaler = 1;
                break;
        }
        int divisor = UART.XTAL / prescaler / 16 / baudRate;
        byte l = (byte) divisor;
        byte h = (byte) (divisor >> 8);
        uartOutputStream.writeConfig(Reg.DLL, l);
        uartOutputStream.writeConfig(Reg.DLH, h);
        return divisor;

    }

    public UARTInputStream getUartInputStream() {
        return uartInputStream;
    }

    public UARTOutputStream getUartOutputStream() {
        return uartOutputStream;
    }
}
