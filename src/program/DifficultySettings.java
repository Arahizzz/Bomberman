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

public class DifficultySettings {

        @FXML
        BorderPane DifficultySettings;
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

        @FXML
        private BackgroundImage Background= new BackgroundImage(new Image("Menu/Bomberman.jpg",1920,1080,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        @FXML
        public void initBackGroundImage(){
                DifficultySettings.setBackground(new Background(Background));
        }

        public void initialize() {
            veryEasy.setOnAction(event -> {
                SingleGame singleGame = new SingleGame();
                Scene scene = ((Node) event.getSource()).getScene();
                Stage stage = (Stage) scene.getWindow();
                Platform.runLater(() -> stage.setTitle("Single game"));
                stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
            });
            easy.setOnAction(event -> Platform.exit());
            normal.setOnAction(event -> {
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
            hard.setOnAction(event -> Platform.exit());
            insane.setOnAction(event -> Platform.exit());
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
