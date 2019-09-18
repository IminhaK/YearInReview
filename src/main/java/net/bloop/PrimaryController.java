package net.bloop;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PrimaryController implements Initializable {

    public static String user;
    public static String region;

    @FXML
    public TextField usernameBox = new TextField();

    @FXML
    public ComboBox comboBox = new ComboBox();

    @FXML
    public void switchToSecondary() throws IOException {
        region = comboBox.getValue().toString();
        user = usernameBox.getText();
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){
        comboBox.getItems().setAll("NA", "EUW", "EUNE", "TR");
    }

    public static String getCombo(){
        return region;
    }

    public static String getUsername(){
        return user;
    }
}
