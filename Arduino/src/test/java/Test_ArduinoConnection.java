import serialConnection.ArduinoConnection;

public class Test_ArduinoConnection {

    public static void main(String[] args) {
        ArduinoConnection ac = new ArduinoConnection("COM4");
        System.out.println(ac.openPort());
        ac.blink(100);
        ac.blink(10000);
        ac.blink(0);
        System.out.println(ac.closePort());
    }
}
