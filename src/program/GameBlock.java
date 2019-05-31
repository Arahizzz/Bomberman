package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

abstract public class GameBlock extends Rectangle {
    boolean breakable;
    boolean walkAllowed;

    public GameBlock(double x, double y, double width, double height, boolean breakable, boolean walkAllowed) {
        super(x, y, width, height);
        this.breakable = breakable;
        this.walkAllowed = walkAllowed;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public boolean isWalkAllowed() {
        return walkAllowed;
    }
}

class StoneBrick extends GameBlock {

    public StoneBrick(int x, int y, int width, int height){
        super(x, y, width, height, false, false);
        setStroke(Color.BLACK);
        setFill(Color.LIGHTGRAY);
    }
}


class RedBrick extends GameBlock {

    public RedBrick(int x, int y, int width, int height) {
        super(x, y, width, height, true, false);
        setStroke(Color.BLACK);
        setFill(Color.ORANGE);
    }
}

class GrassBrick extends GameBlock {

    public GrassBrick(int x, int y, int width, int height) {
        super(x, y, width, height, false, true);
        setStroke(Color.BLACK);
        setFill(Color.GREEN);
    }
}