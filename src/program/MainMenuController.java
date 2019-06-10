package program;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    Button exit;

    @FXML
    Button single;

    @FXML
    Button multi;

    public void initialize() {
        exit.setOnAction(this::exitButtonAction);
        single.setOnAction(this::singleButtonAction);
        multi.setOnAction(this::multiButtonAction);
    }

    private void exitButtonAction(ActionEvent event) {
        Platform.exit();
    }

    private void singleButtonAction(ActionEvent event) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Stage stage = (Stage) single.getScene().getWindow();
        stage.setTitle("Single game");
        stage.setScene(new SingleGame().start(screenBounds.getWidth(), screenBounds.getHeight() - 80));
        stage.centerOnScreen();
        stage.setOnCloseRequest((e) -> Platform.exit());
    }

    private void multiButtonAction(ActionEvent event) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Stage stage = (Stage) multi.getScene().getWindow();
        stage.setTitle("Two players game");
        stage.setScene(new TwoPlayersGame().start(screenBounds.getWidth(), screenBounds.getHeight() - 80));
        stage.centerOnScreen();
        stage.setOnCloseRequest((e) -> Platform.exit());
    }

}
