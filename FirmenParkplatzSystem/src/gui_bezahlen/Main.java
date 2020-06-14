package gui_bezahlen;

import protokoll.Protokoll;

import java.io.*;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

            dOUT.writeUTF("Bezahlen");
            dOUT.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bezahlen() {
        try {
            dOUT.writeUTF(String.valueOf(Protokoll.BEZAHLEN));
            dOUT.writeInt(Integer.parseInt(controller.codeField.getText()));
            dOUT.flush();

            String in = dIN.readUTF();
            double preis = dIN.readDouble();
           if(in.equals(Protokoll.ERROR002.name())){
               in = "Sie haben schon bezahlt.";
               preis = 0;
           }else if(in.equals(Protokoll.ERROR003.name())){
               in = "Die Bezahl-id ist ungültig";
               preis = 0;
           }

            controller.setZeit(in);
            controller.setPreis(getFormattedPreis(preis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getFormattedPreis(double preis){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(preis);
    }
    private String getCurrentTimeStamp(long time) {
        LocalTime d =LocalTime.ofSecondOfDay(time);
        return new SimpleDateFormat("hh:mm:ss").format(d);
    }
}
