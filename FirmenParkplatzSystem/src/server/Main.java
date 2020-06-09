package server;

import protokoll.Einfahrt;
import verwaltung_Parkplatz.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    ServerSocket serverSocket;
    static Controller controller;
    List<String> textToAdmin = new ArrayList<>();

    public Main() {
        try {
            serverSocket = new ServerSocket(9669);
            controller = new Controller(40, 10);

            warteaufAdmin();
            warteAufEinfahrt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void warteaufAdmin() {
        try {
            String type = new String();
            System.out.println("> Bitte Connecten Sie min. ein Admin Panel. ");
            Socket clientSocket;
            ObjectOutputStream out;
            ObjectInputStream in;
            do {
                clientSocket = serverSocket.accept();

                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                while (in.available() == 0) {
                    Thread.sleep(100);
                    System.out.println(clientSocket.isConnected());
                }
                System.out.println(4);
                type = in.readUTF();
            }
            while (!type.equals("Admin"));

            stelleVerbindungMitClientHer(clientSocket, 0, type, in, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void warteAufEinfahrt() {
        Scanner sc = new Scanner(System.in);
        int einfahrt;

        System.out.printf("> Wie viele Einfahrtautomaten sollen angeschlossen werden?%n>> ");
        einfahrt = sc.nextInt();

        System.out.printf("> Warte auf %s Einfahrtautomaten.%n", einfahrt);
        for (int i = 0; i < einfahrt; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                stelleVerbindungMitClientHer(clientSocket, i, in.readUTF(), in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stelleVerbindungMitClientHer(Socket client, int index, String id, ObjectInputStream in, ObjectOutputStream out) {
        new Thread(() -> {
            String type = id;

            try {
                //ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                //ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                //DataInputStream in = new DataInputStream(client.getInputStream());
                // out = new DataOutputStream(client.getOutputStream());


                System.out.printf("> %s %s Connected.%n", type, index + 1);

                //Keywords von den Guis
                switch (type) {
                    case "Einfahrt":
                        textToAdmin.add("Neue Einfahrt Verbunden: " + type + " " + (index + 1));
                        einfahrtAbfragen(out, in);
                        break;
                    case "Admin":
                        admintest(out);
                        break;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean adminUpdate = true;

    public void admintest(ObjectOutputStream out) throws IOException {

        out.writeUTF("TEXT");
        out.writeUTF("Connected.");
        out.flush();
        int index = -1;

        if (textToAdmin.size() - 1 >= 0) {
            for (String text : textToAdmin
            ) {
                out.writeUTF(text);
                out.flush();
            }
            index = textToAdmin.size() - 1;
        }
        while (true) {
            int size = textToAdmin.size() - 1;
            if (adminUpdate) {
                out.writeUTF("DATA");
                for (int i = 0; i < Controller.parkplätze.length(); i++) {
                    System.out.println(Controller.parkplätze.get(i).getId() + " " +Controller.parkplätze.get(i).getStatus());
                }
                out.writeObject(Controller.parkplätze);
                out.flush();
                adminUpdate = false;
                System.out.println("Admin updated");
            }
            if (size != index && size >= 0) {
                out.writeUTF("TEXT");
                out.writeUTF(textToAdmin.get(size));
                out.flush();
                index = size;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void einfahrtAbfragen(ObjectOutputStream out, ObjectInputStream in) throws IOException, InterruptedException {

        while (true) {
            //Abfragen von server
            while (in.available() == 0) {
                Thread.sleep(10);
            }

            String code = in.readUTF();

            if (code.equals(String.valueOf(Einfahrt.FAHRZEUG_ANMELDEN))) {
                //Routine
                controller.belegeParkplatz(-1);
                adminUpdate = true;
                textToAdmin.add("Neues Fahrzeug hat sich angemeldet.");

            } else if (code.equals(String.valueOf(Einfahrt.ISTPARKHAUSVOLL))) {

                textToAdmin.add("Status Request von Einfahrt.");
                out.writeUTF(String.valueOf(Einfahrt.PARKHAUSISTNICHTVOLL));
                out.flush();

            } else if (code.equals(String.valueOf(Einfahrt.ERROR001))) {

                textToAdmin.add("Parkhaus ist Voll. [Error Code 001]");
            }
        }
    }

    public static void main(String[] args) {

        new Main();
    }
}
