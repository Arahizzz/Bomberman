package program;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOver implements Initializable {
    private StringProperty text = new SimpleStringProperty();

    @FXML
    TextField info;
    @FXML
    Button again;
    @FXML
    Button menu;


    public GameOver(boolean hasWon) {
        if (hasWon)
            text.set("You have won!");
        else
            text.set("You have lost.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        info.setText(text.getValue());
    }
}
