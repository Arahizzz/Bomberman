package program;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    Button exit;

    public void initialize() {
        exit.setOnAction(this::exitButtonAction);
    }

    private void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }
}
