package program;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import jdk.nashorn.internal.ir.Block;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Creature {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final double SPEED = 1.5;

    // додати характеристи
    Player(GameBlock spawn, GameBlock[][] blockArray, int blockSize) { //Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize);

        initAnimations();

    }

    private void initAnimations() {
        {
            new Bomb(getCurrentBlock(), null, 0);
        }
        setAnimationFront();
        Timer movement = new Timer();
        movement.schedule(new TimerTask() {
            double x = getX();
            double y = getY();

            @Override
            public void run() {
                updateBlock();
                if (getSide() == Side.TOP && topIsClear()) {
                    y = getY() - SPEED;
                } else if (getSide() == Side.BOTTOM && bottomIsClear()) {
                    y = getY() + SPEED;
                } else if (getSide() == Side.LEFT && leftIsClear()) {
                    x = getX() - SPEED;
                } else if (getSide() == Side.RIGHT && rightIsClear()) {
                    x = getX() + SPEED;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(x);
                        setY(y);
                    }
                });
            }
        }, 0, 10);

        Timer animation = new Timer();
        animation.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        switch (getSide()) {
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
                });
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

    public void updateBlock() {
        switch (getSide()) {
            case TOP:
                if (getTopBlock().isInsideBlock(this.getBoundsInLocal()))
                    setCurrentBlock(getTopBlock());
                break;
            case LEFT:
                if (getLeftBlock().isInsideBlock(this.getBoundsInLocal()))
                    setCurrentBlock(getLeftBlock());
                break;
            case RIGHT:
                if (getRightBlock().isInsideBlock(this.getBoundsInLocal()))
                    setCurrentBlock(getRightBlock());
                break;
            case BOTTOM:
                if (getBottomBlock().isInsideBlock(this.getBoundsInLocal()))
                    setCurrentBlock(getBottomBlock());
                break;
        }
    }

    public Bomb putBomb() {
        return new Bomb(getCurrentBlock(), getBlockArray(), getBlockSize());
    }
}
