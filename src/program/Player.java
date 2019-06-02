package program;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Rectangle {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final double SPEED = 1.5;
    public static final int INNERSIZE = 25;
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
        bounds = new Rectangle((spawn.getX() + (blockSize - INNERSIZE) / 2), spawn.getY() + (blockSize - INNERSIZE) / 2, INNERSIZE, INNERSIZE);
        bounds.setOpacity(0);

        setAnimationFront();
        Timer movement = new Timer();
        movement.schedule(new TimerTask() {
            double x = getX();
            double y = getY();
            double boundsX = bounds.getX();
            double boundsY = bounds.getY();

            @Override
            public void run() {
                if (side == Side.TOP && topIsClear()) {
                    y = getY() - SPEED;
                    boundsY = boundsY - SPEED;
                } else if (side == Side.BOTTOM && bottomIsClear()) {
                    y = getY() + SPEED;
                    boundsY = boundsY + SPEED;
                } else if (side == Side.LEFT && leftIsClear()) {
                    x = getX() - SPEED;
                    boundsX = boundsX - SPEED;
                } else if (side == Side.RIGHT && rightIsClear()) {
                    x = getX() + SPEED;
                    boundsX = boundsX + SPEED;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(x);
                        setY(y);
                        bounds.setX(boundsX);
                        bounds.setY(boundsY);
                        updateBlock();
                    }
                });
            }
        }, 0, 10);
        Timer animation = new Timer();
        animation.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (side) {
                    case TOP:
                        setAnimationBack();
                        break;
                    case BOTTOM:
                        setAnimationFront();
                        break;
                    case RIGHT:
                        setAnimationRight();
                        break;
                    case LEFT:
                        setAnimationLeft();
                        break;
                }
            }
        }, 0, 100);

    }

    public void setAnimationFront(){
        // javafx.scene.image.Image image = new Image("Bomberman\\Front\\Bman_F_f00.png");
        setFill(new ImagePattern(Animation.getPlayerAnimationFront()));
    }

    public void setAnimationBack(){
        setFill(new ImagePattern(Animation.getPlayerAnimationBack()));
    }

    public void setAnimationRight(){
        setFill(new ImagePattern(Animation.getPlayerAnimationRight()));
    }

    public void setAnimationLeft() {
        setFill(new ImagePattern(Animation.getPlayerAnimationLeft()));
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
                if (getTopBlock().isInsideBlock(getBounds().getBoundsInLocal()))
                    currentBlock = getTopBlock();
                break;
            case LEFT:
                if (getLeftBlock().isInsideBlock(getBounds().getBoundsInLocal()))
                    currentBlock = getLeftBlock();
                break;
            case RIGHT:
                if (getRightBlock().isInsideBlock(getBounds().getBoundsInLocal()))
                    currentBlock = getRightBlock();
                break;
            case BOTTOM:
                if (getBottomBlock().isInsideBlock(getBounds().getBoundsInLocal()))
                    currentBlock = getBottomBlock();
                break;
        }
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
        return currentBlock.isOnVerticalRail(getBounds()) && (getTopBlock().isWalkAllowed() || getY() > currentBlock.getY() + 5);
    }

    public boolean bottomIsClear() {
        return currentBlock.isOnVerticalRail(getBounds()) && (getBottomBlock().isWalkAllowed() || getY() + getHeight() - 10 < currentBlock.getY() + getHeight() - 2);
    }

    public boolean leftIsClear() {
        return currentBlock.isOnHorizontalRail(getBounds()) && (getLeftBlock().isWalkAllowed() || getX() > currentBlock.getX() + 5);
    }

    public boolean rightIsClear() {
        return currentBlock.isOnHorizontalRail(getBounds()) && (getRightBlock().isWalkAllowed() || getX() < currentBlock.getX() + getWidth() - 5);
    }
}

enum Side {
    LEFT, RIGHT, TOP, BOTTOM, NONE
}
