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
        main = new Main(this);
        //main start Connection
    }

    /**
     * Setze text in Textfeld.
     *
     * @param text Text.
     */
    public void setCodeFieldText(String text) {
        codeField.setText(text);
    }

    public static void main(String[] args) {

        Application.launch(GUI_Ausfahrt.class);

        new Controller();
    }

    /**
     * Event, wenn Einparken Button gedrückt wurde.
     *
     * @param mouseEvent unwichtig
     */
    public void clicked_Einparken(MouseEvent mouseEvent) {
        main.FahrzeugAbmelden();
    }
}
