package gui_bezahlen;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Main main;

    public Button einparken_Button;
    public TextField codeField;
    public Label zeit;
    public Label preis;

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

        Application.launch(GUI_Bezahlen.class);

        new Controller();
    }

    /**
     * Setzte Text als text des Preis Labels.
     *
     * @param preis text.
     */
    public void setPreis(String preis) {
        this.preis.setText(preis);
    }

    /**
     * Setzte Text als text des Zeit Labels.
     *
     * @param time text.
     */
    public void setZeit(String time) {
        zeit.setText(time);
    }

    /**
     * Event, wenn der Button gedrückt wird.
     *
     * @param mouseEvent unwichtig
     */
    public void clicked_Einparken(MouseEvent mouseEvent) {
        if (codeField.getText() != null) {
            try {
                Integer.valueOf(codeField.getText());
                main.bezahlen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
