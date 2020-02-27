package gui_Einfahrt;

import protokoll.Einfahrt;

import java.io.*;
import java.net.Socket;


public class Main {

    private Socket clientSocket;

    private DataInputStream dIN;
    private DataOutputStream dOUT;


    public Main() {
        try {
            clientSocket = new Socket("127.0.0.1", 9669);

            dIN = new DataInputStream(clientSocket.getInputStream());
            dOUT = new DataOutputStream(clientSocket.getOutputStream());

            dOUT.writeUTF("Einfahrt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendeNeuesFahrzeug() {
        try {
            dOUT.writeUTF(String.valueOf(Einfahrt.FAHRZEUG_ANMELDEN));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
