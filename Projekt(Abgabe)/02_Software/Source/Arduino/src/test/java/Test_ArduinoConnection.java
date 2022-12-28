import serialConnection.ArduinoConnection;

public class Test_ArduinoConnection {

    public static void main(String[] args) {
        ArduinoConnection ac = new ArduinoConnection("COM5");

        ac.addRifdListener(() ->{
            System.out.println("Access allowed");
        });

        System.out.println(ac.openPort());
        ac.readIn();
//        ac.blink(100);
//        ac.blink(10000);
//        ac.blink(0);
        //ac.schliesseSchranke(1000);
        //ac.oeffneSchranke(0);

        ac.printText(100,"Hallo Welt",0);
        ac.printText(100,"hi",1);
        ac.printText(100,"Haaa",0);

        ac.setStatus("Voll", 10);

        while (true);
        //System.out.println(ac.closePort());
    }
}
