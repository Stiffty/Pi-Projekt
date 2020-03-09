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
            if (checkIfFull()) {
                dOUT.writeUTF(String.valueOf(Einfahrt.FAHRZEUG_ANMELDEN));
            } else {
                dOUT.writeUTF(String.valueOf(Einfahrt.ERROR001));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfFull(){
        try {
            dOUT.writeUTF(String.valueOf(Einfahrt.ISTPARKHAUSVOLL));
            while (dIN.available() == 0);
            String answer = dIN.readUTF();
            if (answer.equals(String.valueOf(Einfahrt.PARKHAUSISTNICHTVOLL))) {
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
