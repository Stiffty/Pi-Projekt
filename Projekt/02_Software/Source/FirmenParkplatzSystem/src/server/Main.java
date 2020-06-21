package server;

import eigene_Datenstrukturen.stack.Stack;
import protokoll.Protokoll;
import verwaltung_Parkplatz.Controller;
import verwaltung_Parkplatz.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private ServerSocket serverSocket;
    public static Controller controller;
    public static ControllerArduino arduinoCon;
    private Stack<String> texttoAdmin;

    private Random rand;

    public Main() {
        rand = new Random();
        texttoAdmin = new Stack<>();
        try {
            Scanner sc = new Scanner(System.in);

            System.out.printf("Bitte geben Sie den Port für den Server an. %n \"S\" für 6969.");
            String port = sc.next();
            if (port.equals("s"))
                port = "6969";
            serverSocket = new ServerSocket(Integer.parseInt(port));
            controller = new Controller(40, 10);
            System.out.printf("Bitte geben Sie den Port für den Arduino an.");
            String aport = sc.next();
            System.out.printf("Wollen Sie den Arduino nutzen ?%n j für JA, n für Nein");
            String a = sc.next();
            arduinoCon = new ControllerArduino(a.equals("j"), aport);

            warteaufAdmin();
            warteAufAusfahrt();
            warteAufEinfahrt();
            warteAufBezahlautomat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Wartet bis eine Anzahl an Admin GUi eingegeben wurde und gibt diese weiter
     */
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
                }
                type = in.readUTF();
            }
            while (!type.equals("Admin"));

            stelleVerbindungMitClientHer(0, type, in, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wartet bis eine Anzahl an Einfahrts GUi eingegeben wurde und gibt diese weiter
     */
    private void warteAufEinfahrt() {
        Scanner sc = new Scanner(System.in);
        int einfahrt;

        System.out.printf("> Wie viele Einfahrtautomaten sollen angeschlossen werden?%n>> ");
        einfahrt = sc.nextInt();

        System.out.printf("> Warte auf %s Einfahrtautomaten.%n", einfahrt);
        waitForClient(einfahrt);
    }

    /**
     * Wartet bis eine Anzahl an Ausfahrts GUi eingegeben wurde und gibt diese weiter.
     */
    private void warteAufAusfahrt() {
        Scanner sc = new Scanner(System.in);
        int ausfahrt;

        System.out.printf("> Wie viele Ausfahrtautomaten sollen angeschlossen werden?%n>> ");
        ausfahrt = sc.nextInt();

        System.out.printf("> Warte auf %s Ausfahrtautomaten.%n", ausfahrt);
        waitForClient(ausfahrt);
    }

    /**
     * Wartet bis eine Anzahl an Bezahl GUi eingegeben wurde und gibt diese weiter.
     */
    private void warteAufBezahlautomat() {
        Scanner sc = new Scanner(System.in);
        int bezahlautomat;

        System.out.printf("> Wie viele Bezahlautomaten sollen angeschlossen werden?%n>> ");
        bezahlautomat = sc.nextInt();

        System.out.printf("> Warte auf %s  Bezahlautomaten.%n", bezahlautomat);
        waitForClient(bezahlautomat);
    }

    /**
     * Wartet auf @index Clients.
     *
     * @param index Anzahl der auf zu wartende Clients,
     */
    private void waitForClient(int index) {
        for (int i = 0; i < index; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                String type = in.readUTF();
                stelleVerbindungMitClientHer(i, type, in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt neuen Thread für die Unterschiedlichen Guis.
     *
     * @param index Der wievielte des Typs.
     * @param id    Name des Typs.
     * @param in    Input stream.
     * @param out   Output stream.
     */
    private void stelleVerbindungMitClientHer(int index, String id, ObjectInputStream in, ObjectOutputStream out) {
        new Thread(() -> {
            String type = id;

            try {
                System.out.printf("> %s %s Connected.%n", type, index + 1);

                //Keywords von den Guis
                switch (type) {
                    case "Einfahrt":
                        texttoAdmin.push("Neue Einfahrt Verbunden: " + type + " " + (index + 1));
                        einfahrtAbfragen(out, in);
                        break;
                    case "Ausfahrt":
                        texttoAdmin.push("Neue Ausfahrt Verbunden: " + type + " " + (index + 1));
                        ausfahrtAbfragen(out, in);
                        break;
                    case "Bezahlen":
                        texttoAdmin.push("Neuer BezahlAutomat Verbunden: " + type + " " + (index + 1));
                        bezahlAutomatAbfragen(out, in);
                        break;
                    case "Admin":
                        admintest(out, in);
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
    private boolean isMietet = false;

    /**
     * Zuständig für die Funktionen des Admin Guis. Läuft in eigenem Thread.
     *
     * @param out Output stream.
     * @param in  Input stream.
     * @throws IOException
     */

    public void admintest(ObjectOutputStream out, ObjectInputStream in) throws IOException {

        out.writeUTF("TEXT");
        out.writeUTF("Connected.");
        out.flush();
        int index = -1;

        if(arduinoCon.isAktiv()) {
            arduinoCon.getArduino().addRifdListener(() -> {
                updateid = rand.nextInt(Controller.parkplätze.length());
                state = "BELEGT";
                controller.belegeParkplatz(updateid);
                adminUpdate = true;

                int aP = controller.getFreieParkplaetze();

                arduinoCon.showText(String.valueOf(aP));

                if (aP == 0) {
                    arduinoCon.setStatus("Voll");
                } else if (aP < controller.parkplätze.length() / 2) {
                    arduinoCon.setStatus("Mittel");
                } else {
                    arduinoCon.setStatus("Leer");
                }

                texttoAdmin.push("Mitarbeiter hat sich mit RIFD Chip registriert.");
            });
        }

        if (texttoAdmin.length() - 1 >= 0) {
            for (int i = 0; i < texttoAdmin.length(); i++) {
                out.writeUTF(texttoAdmin.peek());
                out.flush();
            }
            index = texttoAdmin.length() - 1;
        }
        while (true) {

            int size = texttoAdmin.length() - 1;
            if (adminUpdate) {
                if (updateid == -1) {
                    out.writeUTF("DATA");


                    out.writeObject(Controller.parkplätze);

                    adminUpdate = false;
                } else if (updateid == 0) {
                    out.writeUTF("CREATE");
                    out.writeUTF(state);
                    out.writeBoolean(isMietet);
                    out.flush();
                    adminUpdate = false;
                    updateid = -1;
                } else {
                    out.writeUTF("UPDATE");
                    out.writeInt(updateid);
                    out.writeUTF(state);

                    adminUpdate = false;
                    updateid = -1;
                }
            }
            if (size != index && size >= 0) {
                out.writeUTF("TEXT");
                out.writeUTF(texttoAdmin.peek());
                out.flush();
                index = size;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (in.available() > 0) {
                String input = in.readUTF();

                if (input.equals(Protokoll.ADDPARKPLATZ.name())) {
                    String[] parms = in.readUTF().split(":");
                    controller.addParkplatz(Status.valueOf(parms[0]), Boolean.parseBoolean(parms[1]));
                    updateid = 0;
                    state = parms[0];
                    isMietet = Boolean.parseBoolean(parms[1]);
                    adminUpdate = true;
                } else if (input.equals(Protokoll.REMOVEPARKPLATZ.name())) {
                    int id = in.readInt();
                    controller.removeTicket(id);
                } else if (input.equals(Protokoll.CHANGEPARKPLATZ.name())) {
                    String[] params = in.readUTF().split(":");
                    controller.updateParkplatz(Integer.parseInt(params[0]), Status.valueOf(params[1]), Boolean.parseBoolean(params[2]));
                }
            }
        }
    }

    /**
     * Zuständig für die Funktionen des bazahl Guis. Läuft in eigenem Thread.
     *
     * @param out Output stream.
     * @param in  Input stream.
     * @throws IOException
     */
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
                texttoAdmin.push(id + " hat Bezahlt.");
            }
        }
    }

    /**
     * Zuständig für die Funktionen des Ausfahrts Guis. Läuft in eigenem Thread.
     *
     * @param out Output stream.
     * @param in  Input stream.
     * @throws IOException
     */
    public void ausfahrtAbfragen(ObjectOutputStream out, ObjectInputStream in) throws IOException, InterruptedException {

        while (true) {
            //Abfragen von server
            while (in.available() == 0) {
                Thread.sleep(10);
            }

            String code = in.readUTF();

            if (code.equals(String.valueOf(Protokoll.FAHRZEUG_ABMELDEN))) {
                //Routine
                int ticketId = in.readInt();
                for (int i = 0; i < controller.parkplätze.length(); i++) {
                    if (controller.parkplätze.get(i).getStatus().equals(Status.BELEGT)) {
                        updateid = i;
                        break;
                    }
                }
                state = "FREI";
                controller.raumeParkplatz(updateid);
                adminUpdate = true;
                arduinoCon.öffneSchranke();
                texttoAdmin.push("Fahrzeug hat sich abgemeldet.");
                //controller.removeTicket(ticketId);
            } else if (code.equals(String.valueOf(Protokoll.REQESTBEZAHLT))) {
                int id = in.readInt();
                out.writeBoolean(controller.getIfBezahlt(id));
                out.flush();
                texttoAdmin.push("Request ob bezahlt. Id: " + id);
            }
        }
    }

    /**
     * Zuständig für die Funktionen des Einfahrts Guis. Läuft in eigenem Thread.
     *
     * @param out Output stream.
     * @param in  Input stream.
     * @throws IOException
     */
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
                int aP = controller.getFreieParkplaetze();

                arduinoCon.öffneSchranke();
                arduinoCon.showText(String.valueOf(aP));

                if (aP == 0) {
                    arduinoCon.setStatus("Voll");
                } else if (aP < controller.parkplätze.length() / 2) {
                    arduinoCon.setStatus("Mittel");
                } else {
                    arduinoCon.setStatus("Leer");
                }

                texttoAdmin.push("Neues Fahrzeug hat sich angemeldet.");
                texttoAdmin.push("Ticket Id: " + ticketnum);

            } else if (code.equals(String.valueOf(Protokoll.ISTPARKHAUSVOLL))) {

                texttoAdmin.push("Status Request von Einfahrt.");
                out.writeUTF(String.valueOf(Protokoll.PARKHAUSISTNICHTVOLL));
                out.flush();

            } else if (code.equals(String.valueOf(Protokoll.ERROR001))) {

                texttoAdmin.push("Parkhaus ist Voll. [Error Code 001]");
            }
        }
    }

    public static void main(String[] args) {

        new Main();
    }
}
