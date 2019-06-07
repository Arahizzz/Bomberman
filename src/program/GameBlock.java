package program;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Random;

abstract public class GameBlock extends Rectangle {
    boolean breakable;
    boolean walkAllowed;
    private int horizontalIndex;
    private int verticalIndex;
    private Rectangle verticalRail;
    private Rectangle horizontalRail;
    private boolean containsEntity = false;

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
        verticalRail = new Rectangle(x + (width / 2) - 4, y - 3, 8, height + 6);
        verticalRail.setOpacity(0);
        horizontalRail = new Rectangle(x - 3, y + (height / 2) - 15, width + 6, 15);
        horizontalRail.setOpacity(0);
    }

    public double getCenterCoordinatesX(int blockSize, double width) {
        return getX() + (blockSize - width) / 2;
    }

    public double getCenterCoordinatesY(int blockSize, double height) {
        return getY() + (blockSize - height) / 2;
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
        return contains(new Point2D(bounds.getMinX(), bounds.getMaxY())) && bounds.contains(new Point2D(bounds.getMaxX(), bounds.getMaxY()));
    }

    public boolean containsCreature(Creature creature) {
        return intersects(creature.getBoundsInLocal());
    }

    public boolean containsEntity() {
        return containsEntity;
    }

    public void setContainsEntity(boolean containsEntity) {
        this.containsEntity = containsEntity;
    }

    public boolean fullyContains(Bounds bounds) {
        return contains(new Point2D(bounds.getMinX(), bounds.getMinY())) && contains(new Point2D(bounds.getMaxX(), bounds.getMaxY()));
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

    private int bonusChance = 50;
    Random random = new Random();
    public RedBrick(int x, int y, int width, int height, int horizontalIndex, int verticalIndex) {
        super(x, y, width, height, true, false, horizontalIndex, verticalIndex);
        setStroke(Color.BLACK);
        Image image = new Image("Blocks\\ExplodableBlock.png");
        setFill(new ImagePattern(image));
    }

    public Bonus generateBonus(ObservableList<Node> children) {

        if (MyRandom.randomPersantage(bonusChance)) {

            int value = random.nextInt(3);
            Bonuses bonus = Bonuses.values()[value];

            if (bonus == Bonuses.SPEED_BONUS) return new SpeedBonus(this, null, (int) getWidth(), children);
            else if (bonus == Bonuses.EXPLOSION_RANGE_BONUS)
                return new ExplosionRangeBonus(this, null, (int) getWidth(), children);
            else if (bonus == Bonuses.BOMB_AMOUNT_BONUS)
                return new BombAmountBonus(this, null, (int) getWidth(), children);
        }
        return null;

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