package program;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DifficultySettings {

    @FXML
    Button veryEasy;
    @FXML
    Button easy;
    @FXML
    Button normal;
    @FXML
    Button hard;
    @FXML
    Button insane;
    @FXML
    Button menu;

    public void initialize() {
        veryEasy.setOnAction(event -> {
            SingleGame singleGame = new SingleGame(Difficulty.VERAYEASY);
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        easy.setOnAction(event -> {
            SingleGame singleGame = new SingleGame(Difficulty.EASY);
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        normal.setOnAction(event -> {
            SingleGame singleGame = new SingleGame(Difficulty.NORMAL);
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        hard.setOnAction(event -> {
            SingleGame singleGame = new SingleGame(Difficulty.HARD);
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        insane.setOnAction(event -> {
            SingleGame singleGame = new SingleGame(Difficulty.INSANE);
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        menu.setOnAction(event -> {
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                Stage stage = (Stage) scene.getWindow();
                Platform.runLater(() -> stage.setTitle("Bomberman"));
                stage.setScene(new Scene(pane, scene.getWidth(), scene.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
