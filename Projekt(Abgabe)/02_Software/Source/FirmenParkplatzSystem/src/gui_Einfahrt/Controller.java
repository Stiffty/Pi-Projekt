package gui_Einfahrt;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Main main;

    public Button einparken_Button;
    public TextField codeField;

    /**
     * Konstruktor erstellt ein object von main und übergibt sich.
     */
    public Controller() {
        main = new Main(this);
        //main start Connection
    }

    /**
     * Startet das Gui dann sein Konstruktor.
     *
     * @param args null
     */
    public static void main(String[] args) {

        Application.launch(gui_Einfahrt.GUI_Einfahrt.class);

        new Controller();
    }

    /**
     * Setzt code als text des textfeldes.
     *
     * @param code code als integer.
     */
    public void setTextFieldCode(int code) {
        codeField.setText(String.valueOf(code));
    }

    /**
     * Event, wenn der Button gedrückt wird.
     *
     * @param mouseEvent unwichtig
     */
    public void clicked_Einparken(MouseEvent mouseEvent) {
        main.sendeNeuesFahrzeug();
    }
}
