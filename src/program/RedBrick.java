package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RedBrick extends Rectangle {
    boolean breakable = true;
    boolean walkAllowed=false;

    public RedBrick(int x, int y, int width, int height){
        super(x,y,width,height);
        setStroke(Color.BLACK);
        setFill(Color.ORANGE);
    }
}
