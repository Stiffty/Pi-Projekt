package gui_Admin;

import gui_Einfahrt.GUI_Einfahrt;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class Controller {
    public Button showText;
    public TextArea textField;

    private Main main;

    public Controller() {
        main = new Main();
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();
    }

    public void initialize() {
        main.starteListener(textField);
    }

    public void zeigeText(MouseEvent mouseEvent) {
    }

}
