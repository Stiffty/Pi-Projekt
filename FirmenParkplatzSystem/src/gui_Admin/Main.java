package gui_Admin;

import eigene_Datenstrukturen.list.List;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import verwaltung_Parkplatz.Parkplatz;

import java.io.*;
import java.net.Socket;

public class Main {

    private Socket clientSocket;

    //private DataInputStream dIN;
    //private DataOutputStream dOUT;
    private ObjectOutputStream dOUT;
    private ObjectInputStream dIN;
    private List<Parkplatz> parkplätze;

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
                        //while (dIN.available() == 0);

                        parkplätze = (List<Parkplatz>) dIN.readObject();

                        for (int i = 0; i < parkplätze.length(); i++) {
                            System.out.println(i + " " + parkplätze.get(i).getStatus());
                        }

                        //controller.data.clear();
                        for (int i = 0; i < parkplätze.length(); i++) {
                            var d = parkplätze.get(i);
                            var p = new Controller.Parkplatz(d.getId(), d.getStatus().name(), d.isMietet());
                            System.out.println(d.getStatus().name() + " " + i);
                            controller.data.add(p);
                            controller.Parkplätze.setItems(controller.data);
                        }
                        parkplätze = null;
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
