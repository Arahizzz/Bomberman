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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

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
        hPane.getChildren().add(group);
        addEndListeners(gamePlayGroundTwoPlayers.getPlayer1());
        addCharacterisitcs(gamePlayGroundTwoPlayers.getPlayer2());
        addEndListeners(gamePlayGroundTwoPlayers.getPlayer2());
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
        hPane.getChildren().add(characteristics);
    }

    private void addEndListeners(Player player) {
        player.isAliveProperty().addListener(observable -> {
            showEndScreen(player.getID());
        });
    }

    public void showEndScreen(int player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));

            Pane pane = loader.load();

if (player==1)player=2; else player=1;
            Media media = new  Media(new File("Textures/Menu/player" +player+".mp4").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            pane.getChildren().add(mediaView);
            mediaPlayer.play();
            Sounds.playVictory();
            mediaPlayer.setOnEndOfMedia(() -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pane.getChildren().remove(mediaView);
                    }
                });
            });



            Scene gameOver = new Scene(pane, scene.getWidth(), scene.getHeight());

            Stage stage = (Stage) scene.getWindow();
            Platform.runLater(() -> {
                stage.setTitle("Congratulations!");
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
                        gamePlayGround.getPlayer2().setSide(Side.NORTH);
                        break;
                    case DOWN:
                        gamePlayGround.getPlayer2().setSide(Side.SOUTH);
                        break;
                    case LEFT:
                        gamePlayGround.getPlayer2().setSide(Side.WEST);
                        break;
                    case RIGHT:
                        gamePlayGround.getPlayer2().setSide(Side.EAST);
                        break;
                    case P:
                        gamePlayGround.getPlayer2().putBomb();
                        break;
                    case A:
                        gamePlayGround.getPlayer1().setSide(Side.WEST);
                        break;
                    case S:
                        gamePlayGround.getPlayer1().setSide(Side.SOUTH);
                        break;
                    case D:
                        gamePlayGround.getPlayer1().setSide(Side.EAST);
                        break;
                    case W:
                        gamePlayGround.getPlayer1().setSide(Side.NORTH);
                        break;
                    case SPACE:
                        gamePlayGround.getPlayer1().putBomb();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                        gamePlayGround.getPlayer2().setSide(Side.NONE);
                        break;
                    case A:
                    case S:
                    case D:
                    case W:
                        gamePlayGround.getPlayer1().setSide(Side.NONE);
                }
            }
        });
    }


}
