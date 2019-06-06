package program;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.*;

public class SingleGame extends Application {

    private static final int WIN_WIDTH = 1600;
    private static final int WIN_HEIGTH = 900;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Single game");
        HBox hPane = new HBox();
        VBox vPane = new VBox();
        Group group = new Group();
        hPane.setAlignment(Pos.CENTER);
        vPane.getChildren().add(group);
        hPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        GamePlayGround gamePlayGround = new GamePlayGround(group.getChildren(),WIN_WIDTH,WIN_HEIGTH);
        scene = new Scene(hPane, WIN_WIDTH, WIN_HEIGTH);
        primaryStage.setScene(scene);
        gamePlayGround.drawGrid();
        gamePlayGround.initPlayer();
        initListeners(gamePlayGround);

        Characteristics characteristics = new Characteristics(gamePlayGround.getPlayer());
        characteristics.setAlignment(Pos.CENTER);
        hPane.getChildren().addAll(characteristics, vPane);

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    private void initListeners(GamePlayGround gamePlayGround) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        gamePlayGround.getPlayer().setSide(Side.TOP);
                        break;
                    case DOWN:
                        gamePlayGround.getPlayer().setSide(Side.BOTTOM);
                        break;
                    case LEFT:
                        gamePlayGround.getPlayer().setSide(Side.LEFT);
                        break;
                    case RIGHT:
                        gamePlayGround.getPlayer().setSide(Side.RIGHT);
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
