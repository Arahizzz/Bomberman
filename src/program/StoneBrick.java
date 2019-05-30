package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class StoneBrick extends Rectangle {
    boolean breakable = false;
    boolean walkAllowed=false;

    public StoneBrick(int x, int y, int width, int height){
        super(x,y,width,height);
        setStroke(Color.BLACK);
        setFill(Color.LIGHTGRAY);
    }
}
