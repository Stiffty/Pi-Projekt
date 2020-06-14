package gui_Admin;

import eigene_Datenstrukturen.list.List;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import verwaltung_Parkplatz.Parkplatz;
import verwaltung_Parkplatz.Status;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    private Socket clientSocket;

    //private DataInputStream dIN;
    //private DataOutputStream dOUT;
    private ObjectOutputStream dOUT;
    private ObjectInputStream dIN;
    private java.util.List<Parkplatz> parkplätze;

    Controller controller;

    public Main(Controller con) {
        try {
            controller = con;
            clientSocket = new Socket("localhost", 9669);
            clientSocket.setKeepAlive(true);
            dIN = new ObjectInputStream(clientSocket.getInputStream());
            dOUT = new ObjectOutputStream(clientSocket.getOutputStream());
            dOUT.writeUTF("Admin");
            dOUT.flush();
            //dOUT.writeUTF("Admin");
//            System.out.println("test3");
//            dIN = new DataInputStream(clientSocket.getInputStream());
//            dOUT = new DataOutputStream(clientSocket.getOutputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int frei = 0;
    private int belegt = 0;
    public void starteListener(TextArea textField) {
        new Thread(() -> {
            try {
                String text = new String();
                int counter = 0;
                while (true) {
                    while (dIN.available() == 0) {
                        Thread.sleep(10);
                    }
                    String in = dIN.readUTF();
                    System.out.println(in);
                    if (in.equals("TEXT")) {
                        if (counter++ == 1000) {
                            textField.setText(text.substring(text.length() / 2, text.length()));
                            counter = 500;
                        }

                        text = textField.getText() + "\n";
                        textField.setText(text + "> " + dIN.readUTF());
                        textField.setScrollTop(100000000);
                    } else if (in.equals("DATA")) {

                        System.out.printf("called: %s,%n", dIN.available());
                       // while (dIN.available() == 0);

                        List<Parkplatz> p = (List<Parkplatz>) dIN.readObject();
                        parkplätze = new ArrayList<>();

                        controller.data.clear();
                        for (int i = 0; i < p.length(); i++) {
                            var d = p.get(i);
                            parkplätze.add(d);

                            if (d.getStatus().name().equals("FREI"))
                                frei++;
                            else if (d.getStatus().name().equals("BELEGT"))
                                belegt++;

                            var u = new Controller.Parkplatz(d.getId(), d.getStatus().name(), d.isMietet());
                            controller.data.add(u);
                            controller.Parkplätze.setItems(controller.data);
                        }
                        controller.setBelegt(belegt);
                        controller.setfrei(frei);
                    }else if(in.equals("UPDATE")){
                        int index = dIN.readInt();
                        String state = dIN.readUTF();
                        Parkplatz park = parkplätze.get(index);

                        if(state.equals("FREI")) {
                            frei++;
                            belegt--;
                        }else if (state.equals("BELEGT")){
                            frei--;
                            belegt++;
                        }

                        var pp = new Controller.Parkplatz(park.getId(), state, park.isMietet());
                        controller.data.set(index, pp);
                        controller.Parkplätze.setItems(controller.data);
                        controller.setBelegt(belegt);
                        controller.setfrei(frei);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
