package program;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SingleGame {

    Scene scene;

    public Scene start(double winWidth, double winHeigth) {
        HBox hPane = new HBox();
        VBox vPane = new VBox();
        Group group = new Group();
        hPane.setAlignment(Pos.CENTER);
        vPane.getChildren().add(group);
        hPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        GamePlayGround gamePlayGround = new GamePlayGround(group.getChildren(), winWidth, winHeigth);
        gamePlayGround.drawGrid();
        gamePlayGround.initPlayer();
        scene = new Scene(hPane, winWidth, winHeigth);
        initListeners(gamePlayGround);

        Characteristics characteristics = new Characteristics(gamePlayGround.getPlayer());
        characteristics.setAlignment(Pos.CENTER);
        hPane.getChildren().addAll(characteristics, vPane);

        gamePlayGround.getPlayer().isAliveProperty().addListener(observable -> {
            showEndScreen("You have lost.");
        });
        Exit.hasBeenCollectedProperty().addListener(observable -> {
            showEndScreen("You have won.");
        });

        return scene;
    }

    public void showEndScreen(String text) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameOver.fxml"));

            Pane pane = loader.load();
            GameOver controller = loader.getController();
            controller.setInfo(text);
            Scene gameOver = new Scene(pane, scene.getWidth(), scene.getHeight());

            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> {
                stage.setTitle("Game over");
                stage.setScene(gameOver);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners(GamePlayGround gamePlayGround) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        gamePlayGround.getPlayer().setSide(Side.NORTH);
                        break;
                    case DOWN:
                        gamePlayGround.getPlayer().setSide(Side.SOUTH);
                        break;
                    case LEFT:
                        gamePlayGround.getPlayer().setSide(Side.WEST);
                        break;
                    case RIGHT:
                        gamePlayGround.getPlayer().setSide(Side.EAST);
                        break;
                    case SPACE:
                        gamePlayGround.putBomb();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                gamePlayGround.getPlayer().setSide(Side.NONE);
            }
        });
    }

}
