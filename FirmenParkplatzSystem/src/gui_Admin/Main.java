package gui_Admin;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    private Socket clientSocket;

    private DataInputStream dIN;
    private DataOutputStream dOUT;


    public Main() {
        try {
            clientSocket = new Socket("127.0.0.1", 9669);
            clientSocket.setKeepAlive(true);

            dIN = new DataInputStream(clientSocket.getInputStream());
            dOUT = new DataOutputStream(clientSocket.getOutputStream());

            dOUT.writeUTF("Admin");

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
                    while (dIN.available() == 0){Thread.sleep(10);}

                    if (counter++ == 1000) {
                        textField.setText(text.substring(text.length() / 2, text.length()));
                        counter = 500;
                    }

                    text = textField.getText() + "\n";
                    textField.setText(text + "> " + dIN.readUTF());
                    textField.setScrollTop(100000000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
