package program;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameOver {

    @FXML
    Text info;
    @FXML
    Button again;
    @FXML
    Button menu;

    public void setInfo(String text) {
        info.setText(text);
    }

    public void initialize() {
    }
}
