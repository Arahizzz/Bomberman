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

public class TwoPlayersGame {


    HBox hPane;
    VBox vPane;
    Group group;
    GamePlayGroundTwoPlayers gamePlayGroundTwoPlayers;
    Scene scene;

    public Scene start(double winWidth, double winHeigth) {
        setGameField(winWidth, winHeigth);
        gamePlayGroundTwoPlayers.initPlayer1();
        gamePlayGroundTwoPlayers.initPlayer2();
        scene = new Scene(hPane, winWidth, winHeigth);
        initListeners(gamePlayGroundTwoPlayers);

        addCharacterisitcs(gamePlayGroundTwoPlayers.getPlayer1());
        addEndListeners(gamePlayGroundTwoPlayers.getPlayer1());
        ///////////////////////2 player
       // Exit.hasBeenCollectedProperty().addListener(observable -> {
       //     showEndScreen("You have won.");
      //  });

        return scene;
    }

    private void setGameField(double winWidth, double winHeigth) {
        hPane = new HBox();
        vPane = new VBox();
        group = new Group();
        hPane.setAlignment(Pos.CENTER);
        vPane.getChildren().add(group);
        hPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gamePlayGroundTwoPlayers = new GamePlayGroundTwoPlayers(group.getChildren(), winWidth, winHeigth);
        gamePlayGroundTwoPlayers.drawGrid();
    }

    private void addCharacterisitcs(Player player) {
        Characteristics characteristics = new Characteristics(player);
        characteristics.setAlignment(Pos.CENTER);
        hPane.getChildren().addAll(characteristics, vPane);
    }

    private void addEndListeners(Player player) {
        player.isAliveProperty().addListener(observable -> {
            showEndScreen("You have lost.");
        });
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

    private void initListeners(GamePlayGroundTwoPlayers gamePlayGround) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        gamePlayGround.getPlayer1().setSide(Side.NORTH);
                        break;
                    case DOWN:
                        gamePlayGround.getPlayer1().setSide(Side.SOUTH);
                        break;
                    case LEFT:
                        gamePlayGround.getPlayer1().setSide(Side.WEST);
                        break;
                    case RIGHT:
                        gamePlayGround.getPlayer1().setSide(Side.EAST);
                        break;
                    case SPACE:
                        gamePlayGround.putBomb();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                gamePlayGround.getPlayer1().setSide(Side.NONE);
            }
        });
    }


}
