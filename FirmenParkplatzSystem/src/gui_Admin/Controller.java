package gui_Admin;

import javafx.application.Application;
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

    private ObservableList<data> data =  FXCollections.observableArrayList();

    private Main main;

    public Controller() {
        main = new Main();
    }

    public static void main(String[] args) {

        Application.launch(GUI_Admin.class);

        new Controller();
    }

    public void initialize() {
        main.starteListener(textField);
        data d = new data();
        d.setStatus("HI");
        d.setNummer("1");
        data.add(d);

        var tb = new TableColumn("columnName.getText()");
        tb.setMinWidth(200);
        tb.setCellValueFactory(
                new PropertyValueFactory<data, String>("status")
        );
        Parkplätze.getColumns().addAll(tb);
        Parkplätze.setItems(data);
    }

    public void zeigeText(MouseEvent mouseEvent) {
    }

}

class data {
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty nummer = new SimpleStringProperty();

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getNummer() {
        return nummer.get();
    }

    public SimpleStringProperty nummerProperty() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer.set(nummer);
    }
}
