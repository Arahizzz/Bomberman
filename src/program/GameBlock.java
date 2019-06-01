package program;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;

abstract public class GameBlock extends Rectangle {
    boolean breakable;
    boolean walkAllowed;
    private int horizontalIndex;
    private int verticalIndex;
    private Rectangle verticalRail;
    private Rectangle horizontalRail;

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public void setWalkAllowed(boolean walkAllowed) {
        this.walkAllowed = walkAllowed;
    }

    public GameBlock(double x, double y, double width, double height, boolean breakable, boolean walkAllowed, int verticalIndex, int horizontalIndex) {
        super(x, y, width, height);
        this.breakable = breakable;
        this.walkAllowed = walkAllowed;
        this.horizontalIndex = horizontalIndex;
        this.verticalIndex = verticalIndex;
        verticalRail = new Rectangle(x + (width / 2) - 3, y, 5, height);
        verticalRail.setOpacity(0);
        horizontalRail = new Rectangle(x, y + (height / 2) - 3, width, 5);
        horizontalRail.setOpacity(0);
    }


    public boolean isBreakable() {
        return breakable;
    }

    public boolean isWalkAllowed() {
        return walkAllowed;
    }

    public int getHorizontalIndex() {
        return horizontalIndex;
    }

    public int getVerticalIndex() {
        return verticalIndex;
    }

    public boolean isOnVerticalRail(Shape shape) {
        return shape.intersects(verticalRail.getBoundsInLocal());
    }

    public boolean isOnHorizontalRail(Shape shape) {
        return shape.intersects(horizontalRail.getBoundsInLocal());
    }

    public boolean isInsideBlock(Bounds bounds) {
        return bounds.getMinX() >= getX() && bounds.getMinY() >= getY() && bounds.getMaxX() <= getX() + getWidth() && bounds.getMaxY() <= getY() + getHeight();
    }
}

class StoneBrick extends GameBlock {

    public StoneBrick(int x, int y, int width, int height, int horizontalIndex, int verticalIndex) {
        super(x, y, width, height, false, false, horizontalIndex, verticalIndex);
        setStroke(Color.BLACK);
        Image image = new Image("Blocks\\SolidBlock.png");
        setFill(new ImagePattern(image));
    }
}


class RedBrick extends GameBlock {

    public RedBrick(int x, int y, int width, int height, int horizontalIndex, int verticalIndex) {
        super(x, y, width, height, true, false, horizontalIndex, verticalIndex);
        setStroke(Color.BLACK);
        Image image = new Image("Blocks\\ExplodableBlock.png");
        setFill(new ImagePattern(image));
    }
}

class GrassBlock extends GameBlock {

    public GrassBlock(int x, int y, int width, int height, int horizontalIndex, int verticalIndex) {
        super(x, y, width, height, false, true, horizontalIndex, verticalIndex);
        setStroke(Color.BLACK);

        Image image = new Image("Blocks\\BackgroundTile.png");
        setFill(new ImagePattern(image));
    }
}