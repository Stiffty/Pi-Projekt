package gui_Admin;

import eigene_Datenstrukturen.list.List;
import javafx.scene.control.TextArea;
import protokoll.Protokoll;
import verwaltung_Parkplatz.Parkplatz;
import verwaltung_Parkplatz.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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


    /**
     * Wird aufgerufen, wenn der Button gedrückt wurde und wickelt das hinzufügen eines Fahrzeuges mit dem Server ab.
     *
     * @param status   status des neuen Parkplatzes.
     * @param isMietet ob es ein gemieteter parkplatz ist.
     */
    public void addParkplatz(String status, String isMietet) {
        try {
            dOUT.writeUTF(Protokoll.ADDPARKPLATZ.name());
            dOUT.writeUTF(status + ":" + isMietet);
            dOUT.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Entfernt Parkplatz mit bestimmter id.
     *
     * @param id id des zu löschenden Parkplatz.
     */
    public void removeParkplatz(String id) {
        if (tryParseInt(id)) {
            int iD = Integer.parseInt(id);
            try {
                dOUT.writeUTF(Protokoll.REMOVEPARKPLATZ.name());
                dOUT.writeInt(iD);
                dOUT.flush();

                int index = getIndexforId(iD);
                if (index != -1) {
                    if (parkplätze.get(index).getStatus() == Status.FREI) {
                        frei--;
                    } else {
                        belegt--;
                    }

                    controller.data.remove(index);
                    parkplätze.remove(index);
                    controller.Parkplätze.setItems(controller.data);
                    controller.setBelegt(belegt);
                    controller.setfrei(frei);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Ändert die Parameter eines Parkplatzes.
     *
     * @param id
     * @param status
     * @param isMietet
     */
    public void changeParkplatz(String id, String status, String isMietet) {
        if (tryParseInt(id)) {
            int i = Integer.parseInt(id);
            try {
                dOUT.writeUTF(Protokoll.CHANGEPARKPLATZ.name());
                dOUT.writeUTF(i + ":" + status + ":" + isMietet);
                dOUT.flush();

                if (status.equals("FREI")) {
                    frei++;
                    belegt--;
                } else {
                    frei--;
                    belegt++;
                }
                if (i > controller.data.size())
                    return;
                var pp = controller.data.get(i);
                pp.setStatus(status);
                pp.setIsMietet(Boolean.parseBoolean(isMietet));
                controller.data.set(i, pp);
                controller.Parkplätze.setItems(controller.data);
                controller.setBelegt(belegt);
                controller.setfrei(frei);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gibt den Index von einem parkplatz zurück.
     *
     * @param id id des Parkplatzes.
     * @return index des Parkplatzes.
     */
    private int getIndexforId(int id) {
        for (int i = 0; i < parkplätze.size(); i++) {
            if (parkplätze.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int frei = 0;
    private int belegt = 0;

    /**
     * Startet ein Thread der auf Befehle von dem Server wartet.
     *
     * @param textField
     */
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
                    if (in.equals("TEXT")) {
                        if (counter++ == 1000) {
                            textField.setText(text.substring(text.length() / 2, text.length()));
                            counter = 500;
                        }

                        text = textField.getText() + "\n";
                        textField.setText(text + "> " + dIN.readUTF());
                        textField.setScrollTop(100000000);
                    } else if (in.equals("DATA")) {

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
                    } else if (in.equals("UPDATE")) {
                        int index = dIN.readInt();
                        String state = dIN.readUTF();
                        Parkplatz park = parkplätze.get(index);

                        if (state.equals("FREI")) {
                            frei++;
                            belegt--;
                        } else if (state.equals("BELEGT")) {
                            frei--;
                            belegt++;
                        }

                        var pp = new Controller.Parkplatz(park.getId(), state, park.isMietet());
                        controller.data.set(index, pp);
                        controller.Parkplätze.setItems(controller.data);
                        controller.setBelegt(belegt);
                        controller.setfrei(frei);
                    } else if (in.equals("CREATE")) {
                        String state = dIN.readUTF();
                        boolean isMietet = dIN.readBoolean();
                        Parkplatz park = new Parkplatz(parkplätze.size(), Status.valueOf(state), isMietet);
                        parkplätze.add(park);

                        if (state.equals("FREI")) {
                            frei++;
                        } else if (state.equals("BELEGT")) {
                            belegt++;
                        }

                        var pp = new Controller.Parkplatz(park.getId(), park.getStatus().name(), park.isMietet());
                        controller.data.add(pp);
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

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
