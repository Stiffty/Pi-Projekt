package gui_Ausfahrt;

import gui_Einfahrt.GUI_Einfahrt;
import gui_Einfahrt.Main;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Button einparken;

    public Controller() {
        //main start Connection
    }

    public static void main(String[] args) {
        Application.launch(GUI_Ausfahrt.class);

        new Controller();
    }

    public void fahrzeugAbmelden(MouseEvent mouseEvent) {
        System.out.println("test");
    }
}
