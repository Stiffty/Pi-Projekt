package serialConnection;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

import java.io.*;
import java.nio.*;
import java.util.LinkedList;
import java.util.List;

public class ArduinoConnection {

    /*
     * ToDo:
     *  - restlichen Funktionen implementieren
     * */

    private SerialPort sp;

    private int baudRate = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;

    /**
     * Der Serielle Port wird Konfiguriert
     *
     * @param port Name des Seriellen Ports*
     */
    public ArduinoConnection(String port) {
        sp = SerialPort.getCommPort(port);
        sp.setComPortParameters(baudRate, dataBits, stopBits, parity);
        //sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
    }

    /**
     * Stellt verbindung mit dem im Konstruktor festgelegten Port her.
     * Wartet danach 2 sek.
     *
     * @return ob der Port erfolgreich geöffnet wurde.
     */
    public boolean openPort() {
        boolean re = sp.openPort();

        try {
            //Die 2 sek sind optional wobei sie zu guten test Ergebnissen geführt haben.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return re;
    }

    /**
     * Wartet 2 sek.
     * Schließt die Verbindung mit dem im Konstruktor festgelegten Port.
     *
     * @return ob der Port erfolgreich geschlossen wurde.
     */
    public boolean closePort() {
        try {
            //Die 2 sek sind optional wobei sie zu guten test Ergebnissen geführt haben.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sp.closePort();
    }

    /**
     * Schickt den String "blink" an den im Konstruktor festgelegten und geöffneten Port.
     *
     * @param delay Zeit nach dem Senden in ms. Wird addiert mit 21 ms als minimale Wartezeit.
     */
    public void blink(int delay) {
        String in = "blink";
        send(in, delay);
    }

    /**
     * Schickt den String "open" an den im Konstruktor festgelegten und geöffneten Port.
     *
     * @param delay Zeit nach dem Senden in ms. Wird addiert mit 21 ms als minimale Wartezeit.
     */
    public void oeffneSchranke(int delay) {
        String in = "open";
        send(in, delay);
    }

    /**
     * Schickt den String "close" an den im Konstruktor festgelegten und geöffneten Port.
     *
     * @param delay Zeit nach dem Senden in ms. Wird addiert mit 21 ms als minimale Wartezeit.
     */
    public void schliesseSchranke(int delay) {
        String in = "close";
        send(in, delay);
    }

    public void printText(int delay, String text, int line) {
        String in = (line == 0 ? "print0" : "print1");

        send(in, (delay == 0 ? delay + 5 : delay));

        send(text, delay);
    }

    public void readIn() {
        byte[] key = {(byte) 0x80, (byte) 0x06, (byte) 0xA3, (byte) 0xA3};
        new Thread(() -> {
            StringBuilder s = new StringBuilder();
            while (true) {
                try {
                    while (sp.getInputStream().available() > 0)
                        s.append((char) sp.getInputStream().read());


                    if (s.length() != 0 && sp.getInputStream().available() == 0) {
                        String out = s.toString();
                        if(out.equals("rifd")){
                            System.out.println();
                            //send(key.toString(),100);
                            for (int i = 0; i < key.length; i++) {
                               send(key[i],100);
                            }
                        }
                        //System.out.println((key[2]));
                        s = new StringBuilder();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Sendet den String über die Serielle Schnittstelle und wartet 21 + delay ms .
     *
     * @param in    String der gesendet wird.
     * @param delay Wartezeit nach der Sendung. (>0)
     */
    private void send(String in, int delay) {
        try {
            sp.getOutputStream().write(in.getBytes());
            sp.getOutputStream().flush();
            System.out.println("Written");
            Thread.sleep(21 + delay);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void send(Byte in, int delay) {
        try {
            sp.getOutputStream().write(in);
            sp.getOutputStream().flush();
            System.out.println("Written");
            Thread.sleep(21 + delay);
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
