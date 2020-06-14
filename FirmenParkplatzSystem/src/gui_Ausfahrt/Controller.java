package gui_Ausfahrt;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Main main;

    public Button einparken_Button;
    public TextField codeField;

    public Controller() {
        main = new Main();
        //main start Connection
    }

    public static void main(String[] args) {

        Application.launch(GUI_Ausfahrt.class);

        new Controller();
    }

    public void clicked_Einparken(MouseEvent mouseEvent) {
        main.FahrzeugAbmelden();
    }
}
