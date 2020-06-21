package gui_Einfahrt;

import protokoll.Protokoll;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
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
            if(ip.equals("s"))
                ip = "localhost";
            if(port.equals("s"))
                port = "6969";
            clientSocket = new Socket(ip, Integer.parseInt(port));

            dIN = new ObjectInputStream(clientSocket.getInputStream());
            dOUT = new ObjectOutputStream(clientSocket.getOutputStream());

            dOUT.writeUTF("Einfahrt");
            dOUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wird ausgeführt, wenn man den Button drückt und übermittelt dem Server das sich ein fahrzeug angemeldet hat.
     */
    public void sendeNeuesFahrzeug() {
        try {
            if (checkIfFull()) {
                dOUT.writeUTF(String.valueOf(Protokoll.FAHRZEUG_ANMELDEN));
                dOUT.flush();
                System.out.println("Send" + new Date().getTime());
                controller.setTextFieldCode(dIN.readInt());
            } else {
                dOUT.writeUTF(String.valueOf(Protokoll.ERROR001));
            }
            dOUT.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fragt Server, ob das Parkhaus voll ist.
     * @return true wenn nicht false, wenn ja.
     */
    private boolean checkIfFull(){
        try {
            dOUT.writeUTF(String.valueOf(Protokoll.ISTPARKHAUSVOLL));
            dOUT.flush();
            while (dIN.available() == 0);
            String answer = dIN.readUTF();
            if (answer.equals(String.valueOf(Protokoll.PARKHAUSISTNICHTVOLL))) {
                return true;
            }else{
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
