package serialConnection;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoConnection {

    /*
    * ToDo: Kommentieren,
    *   restlichen Funktionen impementiren
    * */

    private SerialPort sp;

    private int baudRate = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;


    public ArduinoConnection(String port) {
        sp = SerialPort.getCommPort(port);
        sp.setComPortParameters(baudRate, dataBits, stopBits, parity);
        //sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

    }

    public boolean openPort() {
        boolean re = sp.openPort();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return re;
    }

    public boolean closePort() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sp.closePort();
    }

    public void blink(int delay) {
        String in = "blink";
        send(in,delay);
    }

    public void oeffneSchranke(int delay){
        String in = "open";
        send(in,delay);
    }

    public void schliesseSchranke(int delay){
        String in = "close";
        send(in,delay);
    }

    private void send(String in,int delay){
        try {
            sp.getOutputStream().write(in.getBytes());
            sp.getOutputStream().flush();
            System.out.println("Written");
            Thread.sleep(21+delay);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }
}
