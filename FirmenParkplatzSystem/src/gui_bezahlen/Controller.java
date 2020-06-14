package gui_bezahlen;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;

public class Controller {
    public Main main;

    public Button einparken_Button;
    public TextField codeField;
    public Label zeit;
    public Label preis;

    public Controller() {
        main = new Main(this);
        //main start Connection
    }

    public static void main(String[] args) {

        Application.launch(GUI_Bezahlen.class);

        new Controller();
    }

    public void setPreis(String preis){
        this.preis.setText(preis);
    }

    public void setZeit(String time){
        zeit.setText(time);
    }

    public void clicked_Einparken(MouseEvent mouseEvent) {
        if(codeField.getText()  != null) {
            try {
                Integer.valueOf(codeField.getText());
                main.bezahlen();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
