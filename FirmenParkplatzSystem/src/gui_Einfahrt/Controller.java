package gui_Einfahrt;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Main main;

    public Button einparken_Button;
    public TextField codeField;

    public Controller() {
        main = new Main(this);
        //main start Connection
    }

    public static void main(String[] args) {

        Application.launch(gui_Einfahrt.GUI_Einfahrt.class);

        new Controller();
    }

    public void setTextFieldCode(int code){
        codeField.setText(String.valueOf(code));
    }

    public void clicked_Einparken(MouseEvent mouseEvent) {
        main.sendeNeuesFahrzeug();
    }
}
