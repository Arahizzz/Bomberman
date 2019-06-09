package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Sounds.init();
        Bomb.init();
        Animation.init();
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        primaryStage.setTitle("Bomberman");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setMaximized(true);
        ScreenController screenController = new ScreenController(primaryStage.getScene());
        screenController.addScreen("MainMenu", FXMLLoader.load(getClass().getResource("mainMenu.fxml")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
