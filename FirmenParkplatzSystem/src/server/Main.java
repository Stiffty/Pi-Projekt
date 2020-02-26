package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args)  {
        try {
            ServerSocket serverSocket = new ServerSocket(9669);


            Socket clientSocket = serverSocket.accept();

            System.out.printf("%s Connected.%n", clientSocket.getLocalAddress());

            DataInputStream dIN = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dOUT = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                while (dIN.available() == 0);
                System.out.println(dIN.readUTF());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
