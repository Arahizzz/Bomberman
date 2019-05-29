package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StoneBrick extends Rectangle {
    boolean breakable = false;

    public StoneBrick(int x, int y, int width, int height){
        super(x,y,width,height);
        setFill(Color.GREY);
    }
}
