package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Player extends Rectangle {

    private static final int WIDTH=30;
    private static final int HEIGHT=50;

    int locationX;
    int locationY;
    // додати характеристи
    Player(Point location){

        super.setWidth(WIDTH);
        super.setHeight(HEIGHT);

        locationX=location.x;
        locationY=location.y;

        setFill(Color.RED);

    }

}
