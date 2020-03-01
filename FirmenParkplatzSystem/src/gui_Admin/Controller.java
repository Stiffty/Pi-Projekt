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

    private int counter;
    public Controller() {
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();
    }
    public void zeigeText(MouseEvent mouseEvent) {
        for (int i = 0; i < 500; i++) {
            print(String.valueOf(counter) + "   " + String.valueOf(text.length()));
        }
    }

    private String text = new String();
    public void print(String in){
        if (counter++ == 1000) {
            textField.setText(text.substring(text.length()/2,text.length()));
            counter = 500;
        }
        
        text = textField.getText() + "\n";
        textField.setText(text + "> " +in);
        textField.setScrollTop(100000000);
    }
}
