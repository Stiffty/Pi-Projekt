package server;

import serialConnection.ArduinoConnection;

public class ControllerArduino {
    private ArduinoConnection arduino;
    private boolean aktiv;

    /**
     * Konstruktor
     *
     * @param aktiv ob die Arduino Verbindungen an sein soll oder nicht.
     * @param port  den Serielle Port des Arduinos. ZB "COM5".
     */
    public ControllerArduino(boolean aktiv, String port) {
        this.aktiv = aktiv;
        if (aktiv) {
            arduino = new ArduinoConnection(port);
            init();
        }
    }

    /**
     * Initialisiert die Verbindung mit dem Arduino.
     */
    private void init() {
        arduino.openPort();
        arduino.readIn();
        arduino.schliesseSchranke(1000);
        arduino.printText(1000, "Frei:", 0);
    }

    /**
     * öffnet Schranke.
     */
    public void öffneSchranke() {
        if (aktiv) {
            arduino.oeffneSchranke(1000);
            // schrankeOpen = true;
        }
    }

    /**
     * schließt Schranke.
     */
    public void schließeSchranke() {
        if (aktiv) {
            arduino.schliesseSchranke(100);
        }
    }

    /**
     * Zeigt text auf dem Display.
     *
     * @param text der text.
     */
    public void showText(String text) {
        if (aktiv) {
            arduino.printText(100, text, 1);
        }
    }

    /**
     * Setzt die Status lampen
     *
     * @param status "Voll","Mittel" oder "Leer".
     */
    public void setStatus(String status) {
        if (aktiv) {
            arduino.setStatus(status, 100);
        }
    }

    public ArduinoConnection getArduino() {
        return arduino;
    }

    public void setArduino(ArduinoConnection arduino) {
        this.arduino = arduino;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
}
