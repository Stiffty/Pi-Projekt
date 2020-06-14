package gui_Ausfahrt;

import protokoll.Protokoll;

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

            String send = "Ausfahrt";
            System.out.println(send);
            dOUT.writeUTF(send);
            dOUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FahrzeugAbmelden() {
        try {
            dOUT.writeUTF(String.valueOf(Protokoll.FAHRZEUG_ABMELDEN));
            dOUT.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
