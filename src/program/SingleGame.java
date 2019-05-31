package program;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SingleGame extends Application {

    private static final int WIN_WIDTH = 1600;
    private static final int WIN_HEIGTH = 900;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Single game");
        VBox pane = new VBox();
        Group group = new Group();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(group);
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setScene(new Scene(pane, WIN_WIDTH, WIN_HEIGTH));
        GamePlayGround gamePlayGround = new GamePlayGround(group.getChildren(),WIN_WIDTH,WIN_HEIGTH);
        gamePlayGround.drawGrid();
        gamePlayGround.initPlayer();
        primaryStage.show();
    }

}
