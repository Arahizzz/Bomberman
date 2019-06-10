package program;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameOver {

    @FXML
    BorderPane GameOver;
    @FXML
    Button again;
    @FXML
    Button menu;
    @FXML
    Button exit;

    @FXML
    private BackgroundImage BIOver= new BackgroundImage(new Image("Menu/gameover.png",1920,1080,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    @FXML
    private BackgroundImage BIWinner= new BackgroundImage(new Image("Menu/congratulations.png",1920,1080,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    @FXML
    public void initBackGroundImage(boolean win){
        if (win)
        GameOver.setBackground(new Background(BIWinner));
        else
        GameOver.setBackground(new Background(BIOver));
    }

    public void initialize() {
        again.setOnAction(event -> {
            SingleGame singleGame = new SingleGame();
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> stage.setTitle("Single game"));
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        exit.setOnAction(event -> Platform.exit());
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
