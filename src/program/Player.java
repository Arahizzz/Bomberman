package program;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Rectangle {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final double SPEED = 1.5;
    private GameBlock currentBlock;
    private GameBlock[][] blockArray;
    private int blockSize;
    private Side side = Side.NONE;
    private Rectangle bounds;

    // додати характеристи
    Player(GameBlock spawn, GameBlock[][] blockArray, int blockSize) { //Point location - це координати блоку (лівий верхній кут)
        this.blockSize = blockSize;
        this.blockArray = blockArray;

        super.setWidth(WIDTH);
        super.setHeight(HEIGHT);

        super.setX(spawn.getX() + (blockSize - WIDTH) / 2); //встановлюємо гравця по цетрі квадратика
        super.setY(spawn.getY() + (blockSize - HEIGHT) / 2);

        currentBlock = spawn;
        bounds = new Rectangle(((getLayoutX() + getWidth()) / 2) - 3, ((getLayoutY() + getHeight()) / 2) - 3, 5, 5);
        bounds.setOpacity(0);

        setFill(Color.RED);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            double x = getX();
            double y = getY();

            @Override
            public void run() {
                if (side == Side.TOP && topIsClear())
                    y = getY() - SPEED;
                else if (side == Side.BOTTOM && bottomIsClear())
                    y = getY() + SPEED;
                else if (side == Side.LEFT && leftIsClear())
                    x = getX() - SPEED;
                else if (side == Side.RIGHT && rightIsClear())
                    x = getX() + SPEED;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(x);
                        setY(y);
                        updateBounds();
                        updateBlock();
                    }
                });
            }
        }, 0, 10);

    }

    private Point getArrayCoordinates() {// повертає координати блока в масиві (рядок і стовчик)
        return new Point((int) (getX() / blockSize) + 1, (int) (getY() / blockSize) + 1);
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void updateBlock() {
        switch (side) {
            case TOP:
                if (getTopBlock().isInsideBlock(getBoundsInLocal()))
                    currentBlock = getTopBlock();
                break;
            case LEFT:
                if (getLeftBlock().isInsideBlock(getBoundsInLocal()))
                    currentBlock = getLeftBlock();
                break;
            case RIGHT:
                if (getRightBlock().isInsideBlock(getBoundsInLocal()))
                    currentBlock = getRightBlock();
                break;
            case BOTTOM:
                if (getBottomBlock().isInsideBlock(getBoundsInLocal()))
                    currentBlock = getBottomBlock();
                break;
        }
    }

    public void updateBounds() {
        bounds.setX(getX());
        bounds.setY(getY());
    }

    private GameBlock getBottomBlock() {
        return blockArray[currentBlock.getVerticalIndex() + 1][currentBlock.getHorizontalIndex()];
    }

    private GameBlock getRightBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() + 1];
    }

    private GameBlock getLeftBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() - 1];
    }

    private GameBlock getTopBlock() {
        return blockArray[currentBlock.getVerticalIndex() - 1][currentBlock.getHorizontalIndex()];
    }

    public boolean topIsClear() {
        return currentBlock.isOnVerticalRail(this) && (getTopBlock().isWalkAllowed() || getY() > currentBlock.getY() + 5);
    }

    public boolean bottomIsClear() {
        return currentBlock.isOnVerticalRail(this) && (getBottomBlock().isWalkAllowed() || getY() + getHeight() - 10 < currentBlock.getY() + getHeight() - 2);
    }

    public boolean leftIsClear() {
        return currentBlock.isOnHorizontalRail(this) && (getLeftBlock().isWalkAllowed() || getX() > currentBlock.getX() + 5);
    }

    public boolean rightIsClear() {
        return currentBlock.isOnHorizontalRail(this) && (getRightBlock().isWalkAllowed() || getX() < currentBlock.getX() + getWidth() - 5);
    }
}

enum Side {
    LEFT, RIGHT, TOP, BOTTOM, NONE
}
