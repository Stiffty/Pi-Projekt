package server;

import protokoll.Protokoll;
import verwaltung_Parkplatz.Controller;
import verwaltung_Parkplatz.Status;
import verwaltung_Parkplatz.Ticket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    private ServerSocket serverSocket;
    public static Controller controller;
    private List<String> textToAdmin;
    private Random rand;

    public Main() {
        rand = new Random();
        textToAdmin = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(9669);
            controller = new Controller(40, 10);

            warteaufAdmin();
            warteAufAusfahrt();
            warteAufEinfahrt();
            warteAufBezahlautomat();
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
        waitForClient(einfahrt);
    }

    private void warteAufAusfahrt() {
        Scanner sc = new Scanner(System.in);
        int ausfahrt;

        System.out.printf("> Wie viele Ausfahrtautomaten sollen angeschlossen werden?%n>> ");
        ausfahrt = sc.nextInt();

        System.out.printf("> Warte auf %s Ausfahrtautomaten.%n", ausfahrt);
        waitForClient(ausfahrt);
    }

    private void warteAufBezahlautomat() {
        Scanner sc = new Scanner(System.in);
        int bezahlautomat;

        System.out.printf("> Wie viele Bezahlautomaten sollen angeschlossen werden?%n>> ");
        bezahlautomat = sc.nextInt();

        System.out.printf("> Warte auf %s  Bezahlautomaten.%n", bezahlautomat);
        waitForClient(bezahlautomat);
    }

    private void waitForClient(int index) {
        for (int i = 0; i < index; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String type = in.readUTF();
                stelleVerbindungMitClientHer(clientSocket, i, type, in, out);
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
                    case "Ausfahrt":
                        textToAdmin.add("Neue Ausfahrt Verbunden: " + type + " " + (index + 1));
                        ausfahrtAbfragen(out, in);
                        break;
                    case "Bezahlen":
                        textToAdmin.add("Neuer BezahlAutomat Verbunden: " + type + " " + (index + 1));
                        bezahlAutomatAbfragen(out, in);
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
    private int updateid = -1;
    private String state = "FREI";

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
                if(updateid == -1) {
                    out.writeUTF("DATA");

                    for (int i = 0; i < Controller.parkplätze.length(); i++) {
                        System.out.println(Controller.parkplätze.get(i).getId() + " " + Controller.parkplätze.get(i).getStatus());
                    }

                    out.writeObject(Controller.parkplätze);

                    adminUpdate = false;
                }else{
                    out.writeUTF("UPDATE");
                    out.writeInt(updateid);
                    out.writeUTF(state);

                    adminUpdate = false;
                    updateid = -1;
                }
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

    public void bezahlAutomatAbfragen(ObjectOutputStream out, ObjectInputStream in) throws IOException, InterruptedException {

        while (true) {
            //Abfragen von server
            while (in.available() == 0) {
                Thread.sleep(10);
            }

            String code = in.readUTF();

            if (code.equals(String.valueOf(Protokoll.BEZAHLEN))) {
                //Routine
                int id = in.readInt();

                String date = controller.getParkTimeForId(id);
                out.writeUTF(Objects.requireNonNullElseGet(date, Protokoll.ERROR002::name));
                out.writeDouble(controller.getPreisForParkdauer(id));
                out.flush();
            }
        }
    }

    public void ausfahrtAbfragen(ObjectOutputStream out, ObjectInputStream in) throws IOException, InterruptedException {

        while (true) {
            //Abfragen von server
            while (in.available() == 0) {
                Thread.sleep(10);
            }

            String code = in.readUTF();

            if (code.equals(String.valueOf(Protokoll.FAHRZEUG_ABMELDEN))) {
                //Routine
                for (int i = 0; i < controller.parkplätze.length(); i++) {
                    if(controller.parkplätze.get(i).getStatus().equals(Status.BELEGT)){
                        updateid = i;
                        break;
                    }
                }
                state = "FREI";
                controller.raumeParkplatz(updateid);
                adminUpdate = true;
                textToAdmin.add("Fahrzeug hat sich abgemeldet.");
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

            if (code.equals(String.valueOf(Protokoll.FAHRZEUG_ANMELDEN))) {
                //Routine
                updateid = rand.nextInt(Controller.parkplätze.length());
                state = "BELEGT";
                controller.belegeParkplatz(updateid);
                adminUpdate = true;

                int ticketnum = controller.regNewTicket();
                out.writeInt(ticketnum);
                out.flush();
                textToAdmin.add("Neues Fahrzeug hat sich angemeldet.");
                textToAdmin.add("Ticket Id: " + ticketnum);

            } else if (code.equals(String.valueOf(Protokoll.ISTPARKHAUSVOLL))) {

                textToAdmin.add("Status Request von Einfahrt.");
                out.writeUTF(String.valueOf(Protokoll.PARKHAUSISTNICHTVOLL));
                out.flush();

            } else if (code.equals(String.valueOf(Protokoll.ERROR001))) {

                textToAdmin.add("Parkhaus ist Voll. [Error Code 001]");
            }
        }
    }

    public static void main(String[] args) {

        new Main();
    }
}
