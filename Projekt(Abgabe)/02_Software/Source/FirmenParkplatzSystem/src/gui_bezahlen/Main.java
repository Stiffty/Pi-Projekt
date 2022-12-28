package gui_bezahlen;

import protokoll.Protokoll;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.NumberFormat;
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

            dOUT.writeUTF("Bezahlen");
            dOUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wird ausgeführt, wenn man den Bezahlen button drückt und wickelt die Bezahlung mit dem Server ab.
     */
    public void bezahlen() {
        try {
            dOUT.writeUTF(String.valueOf(Protokoll.BEZAHLEN));
            dOUT.writeInt(Integer.parseInt(controller.codeField.getText()));
            dOUT.flush();

            String in = dIN.readUTF();
            double preis = dIN.readDouble();
            if (in.equals(Protokoll.ERROR002.name())) {
                in = "Sie haben schon bezahlt.";
                preis = 0;
            } else if (in.equals(Protokoll.ERROR003.name())) {
                in = "Die Bezahl-id ist ungültig";
                preis = 0;
            }

            controller.setZeit(in);
            controller.setPreis(getFormattedPreis(preis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formatiert den Preis in euro.
     *
     * @param preis preis
     * @return formatierter Preis.
     */
    private String getFormattedPreis(double preis) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(preis);
    }
}
