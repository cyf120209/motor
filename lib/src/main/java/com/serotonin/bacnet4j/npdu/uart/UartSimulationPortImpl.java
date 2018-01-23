package com.serotonin.bacnet4j.npdu.uart;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class UartSimulationPortImpl extends SerialPort {
    @Override
    public void setSerialPortParams(int i, int i1, int i2, int i3) throws UnsupportedCommOperationException {

    }

    @Override
    public int getBaudRate() {
        return 0;
    }

    @Override
    public int getDataBits() {
        return 0;
    }

    @Override
    public int getStopBits() {
        return 0;
    }

    @Override
    public int getParity() {
        return 0;
    }

    @Override
    public void setFlowControlMode(int i) throws UnsupportedCommOperationException {

    }

    @Override
    public int getFlowControlMode() {
        return 0;
    }

    @Override
    public boolean isDTR() {
        return false;
    }

    @Override
    public void setDTR(boolean b) {

    }

    @Override
    public void setRTS(boolean b) {

    }

    @Override
    public boolean isCTS() {
        return false;
    }

    @Override
    public boolean isDSR() {
        return false;
    }

    @Override
    public boolean isCD() {
        return false;
    }

    @Override
    public boolean isRI() {
        return false;
    }

    @Override
    public boolean isRTS() {
        return false;
    }

    @Override
    public void sendBreak(int i) {

    }

    @Override
    public void addEventListener(SerialPortEventListener serialPortEventListener) throws TooManyListenersException {

    }

    @Override
    public void removeEventListener() {

    }

    @Override
    public void notifyOnDataAvailable(boolean b) {

    }

    @Override
    public void notifyOnOutputEmpty(boolean b) {

    }

    @Override
    public void notifyOnCTS(boolean b) {

    }

    @Override
    public void notifyOnDSR(boolean b) {

    }

    @Override
    public void notifyOnRingIndicator(boolean b) {

    }

    @Override
    public void notifyOnCarrierDetect(boolean b) {

    }

    @Override
    public void notifyOnOverrunError(boolean b) {

    }

    @Override
    public void notifyOnParityError(boolean b) {

    }

    @Override
    public void notifyOnFramingError(boolean b) {

    }

    @Override
    public void notifyOnBreakInterrupt(boolean b) {

    }

    @Override
    public byte getParityErrorChar() throws UnsupportedCommOperationException {
        return 0;
    }

    @Override
    public boolean setParityErrorChar(byte b) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public byte getEndOfInputChar() throws UnsupportedCommOperationException {
        return 0;
    }

    @Override
    public boolean setEndOfInputChar(byte b) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public boolean setUARTType(String s, boolean b) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public String getUARTType() throws UnsupportedCommOperationException {
        return null;
    }

    @Override
    public boolean setBaudBase(int i) throws UnsupportedCommOperationException, IOException {
        return false;
    }

    @Override
    public int getBaudBase() throws UnsupportedCommOperationException, IOException {
        return 0;
    }

    @Override
    public boolean setDivisor(int i) throws UnsupportedCommOperationException, IOException {
        return false;
    }

    @Override
    public int getDivisor() throws UnsupportedCommOperationException, IOException {
        return 0;
    }

    @Override
    public boolean setLowLatency() throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public boolean getLowLatency() throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public boolean setCallOutHangup(boolean b) throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public boolean getCallOutHangup() throws UnsupportedCommOperationException {
        return false;
    }

    @Override
    public void enableReceiveFraming(int i) throws UnsupportedCommOperationException {

    }

    @Override
    public void disableReceiveFraming() {

    }

    @Override
    public boolean isReceiveFramingEnabled() {
        return false;
    }

    @Override
    public int getReceiveFramingByte() {
        return 0;
    }

    @Override
    public void disableReceiveTimeout() {

    }

    @Override
    public void enableReceiveTimeout(int i) throws UnsupportedCommOperationException {

    }

    @Override
    public boolean isReceiveTimeoutEnabled() {
        return false;
    }

    @Override
    public int getReceiveTimeout() {
        return 0;
    }

    @Override
    public void enableReceiveThreshold(int i) throws UnsupportedCommOperationException {

    }

    @Override
    public void disableReceiveThreshold() {

    }

    @Override
    public int getReceiveThreshold() {
        return 0;
    }

    @Override
    public boolean isReceiveThresholdEnabled() {
        return false;
    }

    @Override
    public void setInputBufferSize(int i) {

    }

    @Override
    public int getInputBufferSize() {
        return 0;
    }

    @Override
    public void setOutputBufferSize(int i) {

    }

    @Override
    public int getOutputBufferSize() {
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
