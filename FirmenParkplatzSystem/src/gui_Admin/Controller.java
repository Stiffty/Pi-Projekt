package gui_Admin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Button showText;
    public TextArea textField;
    public Label frei;
    public Label belegt;
    public TableView Parkplätze;

    public ObservableList<Parkplatz> data = FXCollections.observableArrayList();

    private Main main;

    public Controller() {
        main = new Main(this);
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();

    }

    public void initialize() {
        main.starteListener(textField);

        var tb = new TableColumn("Status");
        tb.setMinWidth(20);
        tb.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("status")
        );
        var id = new TableColumn("ID");
        id.setPrefWidth(2);
        id.setMinWidth(20);
        id.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("id")
        );
        var mietet = new TableColumn("IsMietet");
        mietet.setMinWidth(20);
        mietet.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("isMietet")
        );
        Parkplätze.getColumns().addAll(id, tb, mietet);
    }


    public  void setfrei(int num){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                frei.setText(String.valueOf(num));
            }
        });
    }
    public void setBelegt(int num){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                belegt.setText(String.valueOf(num));
            }
        });
    }

    public void zeigeText(MouseEvent mouseEvent) {
    }

    public void addParkplatz(Parkplatz p) {
        data.add(p);
        Parkplätze.setItems(data);
    }


    public static class Parkplatz {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty status;
        private final SimpleBooleanProperty isMietet;

        Parkplatz(int id, String status, boolean isMietet) {
            this.id = new SimpleIntegerProperty(id);
            this.status = new SimpleStringProperty(status);
            this.isMietet = new SimpleBooleanProperty(isMietet);
        }

        public int getId() {
            return id.get();
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public boolean getIsMietet() {
            return isMietet.get();
        }

        public void setIsMietet(boolean isMietet) {
            this.isMietet.set(isMietet);
        }
    }
}

