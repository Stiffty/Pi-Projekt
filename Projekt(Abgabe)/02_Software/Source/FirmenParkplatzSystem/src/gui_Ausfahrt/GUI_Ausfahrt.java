package gui_Ausfahrt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI_Ausfahrt extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("Ausfahrt");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setOnCloseRequest((windowEvent) -> {
            System.exit(0);
        });

        GridPane root = FXMLLoader.load(getClass().getResource("Ausfahrt_GUI.fxml"));

        Scene s = new Scene(root, 800, 500);
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
