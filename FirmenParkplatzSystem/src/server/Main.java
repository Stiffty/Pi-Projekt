package server;

import protokoll.Einfahrt;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Main {

    ServerSocket serverSocket;


    public Main() {
        try {
            serverSocket = new ServerSocket(9669);

            warteAufClients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void warteAufClients() {
        Scanner sc = new Scanner(System.in);
        int einfahrt;

        System.out.printf("> Wie viele Einfahrtautomaten sollen angeschlossen werden?%n>> ");
        einfahrt = sc.nextInt();

        System.out.printf("> Warte auf %s Einfahrtautomaten.%n",einfahrt);
        for (int i = 0; i < einfahrt; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                stelleVerbindungMitClientHer(clientSocket,i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stelleVerbindungMitClientHer(Socket client,int index) {
        new Thread(() -> {
            String type;

            try {
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());

                type = in.readUTF();

                System.out.printf("> %s %s Connected.%n", type,index+1);

                //Keywords von den Guis
                switch (type){
                    case "Einfahrt":
                        einfahrtAbfragen(out,in);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void einfahrtAbfragen(DataOutputStream out, DataInputStream in ) throws IOException {

        while (true) {
            //Abfragen von server
            while (in.available() == 0) ;
            String code = in.readUTF();

            if (code.equals( String.valueOf(Einfahrt.FAHRZEUG_ANMELDEN))) {
                //Routine
                System.out.println("> Neues Fahrzeug hat sich angemeldet.");
            }
        }
    }

    public static void main(String[] args) {

        new Main();
    }
}
