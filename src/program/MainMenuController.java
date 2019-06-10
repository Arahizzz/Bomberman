package program;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
        single.setOnAction(event -> {
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("DifficultySettings.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                Stage stage = (Stage) scene.getWindow();
                Platform.runLater(() -> stage.setTitle("Difficulty"));
                stage.setScene(new Scene(pane, scene.getWidth(), scene.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        multi.setOnAction(this::multiButtonAction);
    }

    private void exitButtonAction(ActionEvent event) {
        Platform.exit();
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
