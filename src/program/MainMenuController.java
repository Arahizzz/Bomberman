package program;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    Button exit;

    @FXML
    Button single;

    public void initialize() {
        exit.setOnAction(this::exitButtonAction);
        single.setOnAction(this::singleButtonAction);
    }

    private void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void singleButtonAction(ActionEvent event) {
        Stage stage = (Stage) single.getScene().getWindow();
        stage.setTitle("Single game");
        stage.setScene(new SingleGame().start(stage.getWidth(), stage.getHeight()));
        stage.centerOnScreen();
        stage.setOnCloseRequest((e) -> System.exit(0));
    }
}
