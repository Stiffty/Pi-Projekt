package gui_Admin;

import javafx.scene.control.TextArea;

public class Console {
    private String text;


    public void print(String in,TextArea textArea){
        text = textArea.getText() + "\n";
        textArea.setText(text + in);
    }
}
