package gui_Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI_Admin extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Admin Panel");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);

        GridPane root = FXMLLoader.load(getClass().getResource("Admin_GUI.fxml"));

        Scene s = new Scene(root, 800, 500);
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
