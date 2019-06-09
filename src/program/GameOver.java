package program;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOver {

    @FXML
    Text info;
    @FXML
    Button again;
    @FXML
    Button menu;
    @FXML
    Button exit;

    public void setInfo(String text) {
        info.setText(text);
    }

    public void initialize() {
        again.setOnAction(event -> {
            SingleGame singleGame = new SingleGame();
            Scene scene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(singleGame.start(scene.getWidth(), scene.getHeight()));
        });
        exit.setOnAction(event -> Platform.exit());
        menu.setOnAction(event -> {
            try {
                Pane pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                Scene scene = ((Node) event.getSource()).getScene();
                Stage stage = (Stage) scene.getWindow();
                stage.setScene(new Scene(pane, scene.getWidth(), scene.getHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
