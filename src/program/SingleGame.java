package program;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SingleGame extends Application {

    private static final int WIN_WIDTH = 1600;
    private static final int WIN_HEIGTH = 900;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Single game");
        Group group = new Group();
        primaryStage.setScene(new Scene(group,WIN_WIDTH,WIN_HEIGTH));
        GamePlayGround gamePlayGround = new GamePlayGround(group.getChildren(),WIN_WIDTH,WIN_HEIGTH);
        gamePlayGround.drawGrid();
        gamePlayGround.initPlayer();
        primaryStage.show();
    }

}
