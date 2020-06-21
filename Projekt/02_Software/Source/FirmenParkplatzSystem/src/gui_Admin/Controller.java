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

public class Controller {
    public TextArea textField;
    public Label frei;
    public Label belegt;
    public TableView Parkplätze;

    public ObservableList<Parkplatz> data = FXCollections.observableArrayList();

    public ChoiceBox boxStatus;
    public ChoiceBox boxIsMietet;
    public Button addButton;

    public Button removeButton;
    public TextField idBox;

    public Button changeButton;
    public ChoiceBox choiceBoxStatus;
    public ChoiceBox choiseboxIsMietet;
    public TextField idField;

    private Main main;

    public Controller() {
        main = new Main(this);
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();

    }

    /**
     * Initialisiere Gui und die Verbindung mit dem Server.
     */
    public void initialize() {
        main.starteListener(textField);
        ObservableList<String> status = FXCollections.observableArrayList("FREI", "BELEGT", "DEFEKT");
        ObservableList<String> isMietet = FXCollections.observableArrayList("true", "false");

        boxStatus.setItems(status);
        boxStatus.setValue("FREI");
        boxIsMietet.setItems(isMietet);
        boxIsMietet.setValue("false");

        choiceBoxStatus.setItems(status);
        choiceBoxStatus.setValue("FREI");
        choiseboxIsMietet.setItems(isMietet);
        choiseboxIsMietet.setValue("false");

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

    /**
     * Setzt eine Nummer als Text von dem frei Label.
     *
     * @param num Nummer
     */
    public void setfrei(int num) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                frei.setText(String.valueOf(num));
            }
        });
    }

    /**
     * Setzt eine Nummer als Text von dem belegt Label.
     *
     * @param num Nummer
     */
    public void setBelegt(int num) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                belegt.setText(String.valueOf(num));
            }
        });
    }

    /**
     * Event, wenn add Button gedrückt wird.
     */
    public void addParkplatz() {
        main.addParkplatz(String.valueOf(boxStatus.getValue()), String.valueOf(boxIsMietet.getValue()));
    }

    /**
     * Event, wenn remove gedrückt wird.
     */
    public void removeParkplatz() {
        main.removeParkplatz(idBox.getText());
    }

    /**
     * Event, wenn change gedrückt wird.
     */
    public void changeParkplatz() {
        main.changeParkplatz(idField.getText(), String.valueOf(choiceBoxStatus.getValue()), String.valueOf(choiseboxIsMietet.getValue()));
    }

    /**
     * Klasse für den Tabelview also die Tabelle.
     */
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

