package gui_Einfahrt;

import protokoll.Protokoll;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


public class Main {

    private Socket clientSocket;

    private ObjectInputStream dIN;
    private ObjectOutputStream dOUT;

    private Controller controller;

    public Main(Controller con) {
        controller = con;
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
