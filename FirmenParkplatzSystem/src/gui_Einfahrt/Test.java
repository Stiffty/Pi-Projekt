package gui_Einfahrt;

import javafx.application.Application;
import serialConnection.ArduinoConnection;

public class Test {
    public static void main(String[] args) {

        new Thread(()->{
            Application.launch(GUI_Einfahrt.class);
        }).start();

    }
}
