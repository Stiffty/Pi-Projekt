package gui_Ausfahrt;

import protokoll.Protokoll;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Main {

    private Socket clientSocket;

    private ObjectInputStream dIN;
    private ObjectOutputStream dOUT;

    private Controller controller;

    public Main(Controller con) {
        controller = con;
        try {
            Scanner sc = new Scanner(System.in);

            System.out.printf("Bitte geben Sie die Ip des Servers an. %n \"S\" für localhost.");
            String ip = sc.next();
            System.out.printf("Bitte geben Sie den Port für den Server an. %n \"S\" für 6969.");
            String port = sc.next();
            if (ip.equals("s"))
                ip = "localhost";
            if (port.equals("s"))
                port = "6969";
            clientSocket = new Socket(ip, Integer.parseInt(port));

            dIN = new ObjectInputStream(clientSocket.getInputStream());
            dOUT = new ObjectOutputStream(clientSocket.getOutputStream());

            String send = "Ausfahrt";
            System.out.println(send);
            dOUT.writeUTF(send);
            dOUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Meldet ein Fahrzeug beim Server ab.
     */
    public void FahrzeugAbmelden() {
        try {
            dOUT.writeUTF(String.valueOf(Protokoll.REQESTBEZAHLT));
            dOUT.writeInt(tryParseInt(controller.codeField.getText()) ? Integer.parseInt(controller.codeField.getText()) : -1);
            dOUT.flush();

            if (dIN.readBoolean()) {
                dOUT.writeUTF(String.valueOf(Protokoll.FAHRZEUG_ABMELDEN));
                dOUT.writeInt(tryParseInt(controller.codeField.getText()) ? Integer.parseInt(controller.codeField.getText()) : -1);
                dOUT.flush();
                controller.setCodeFieldText("Auf Wiedersehen.");
            } else {
                controller.setCodeFieldText("Bitte bezahlen Sie.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Versucht ein String in einen Int umzuwandeln.
     *
     * @param value String
     * @return true wenn möglich sonst false.
     */
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
