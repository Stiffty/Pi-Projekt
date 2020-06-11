package gui_Ausfahrt;

import protokoll.Einfahrt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Main {

    private Socket clientSocket;

    private ObjectInputStream dIN;
    private ObjectOutputStream dOUT;


    public Main() {
        try {
            clientSocket = new Socket("127.0.0.1", 9669);

            dIN = new ObjectInputStream(clientSocket.getInputStream());
            dOUT = new ObjectOutputStream(clientSocket.getOutputStream());

            dOUT.writeUTF("Einfahrt");
            dOUT.flush();

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
            dOUT.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfFull(){
        try {
            dOUT.writeUTF(String.valueOf(Einfahrt.ISTPARKHAUSVOLL));
            dOUT.flush();
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
