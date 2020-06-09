package gui_Admin;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Controller {
    public Button showText;
    public TextArea textField;
    public TableView Parkplätze;

    public ObservableList<Parkplatz> data = FXCollections.observableArrayList();

    private Main main;

    public Controller() {
        System.out.println("test");
        main = new Main(this);
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();
    }

    public void initialize() {
        main.starteListener(textField);

        var tb = new TableColumn("Status");
        tb.setMinWidth(200);
        tb.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("status")
        );
        var id = new TableColumn("ID");
        id.setMinWidth(200);
        id.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("id")
        );
        var mietet = new TableColumn("IsMietet");
        mietet.setMinWidth(200);
        mietet.setCellValueFactory(
                new PropertyValueFactory<Parkplatz, String>("isMietet")
        );
        Parkplätze.getColumns().addAll(id, tb, mietet);
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

