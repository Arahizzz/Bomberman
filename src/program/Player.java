package program;

import com.sun.javafx.sg.prism.web.NGWebView;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import jdk.nashorn.internal.ir.Block;
import sun.awt.FwDispatcher;

import java.awt.*;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Creature {
    private static HashSet<Bonus> bonuses = Bonus.getBonuses();
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;

    public double getSpeed() {
        return speed.get();
    }

    private DoubleProperty speed = new SimpleDoubleProperty(1.5);
    private static final double MAXSPEED = 3.0;

    private final IntegerProperty maxCount = new SimpleIntegerProperty(1);
    private int currentCount = -1;
    private final IntegerProperty range = new SimpleIntegerProperty(1);

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double value) {
        Platform.runLater(() -> speed.setValue(value));
    }

    Player(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) { //Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 3);
        initAnimations();
    }

    public int getMaxCount() {
        return maxCount.get();
    }

    public StringBinding maxCountProperty() {
        return maxCount.asString();
    }

    public int getRange() {
        return range.get();
    }

    public StringBinding rangeProperty() {
        return range.asString();
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void increaseRange() {
        Platform.runLater(() -> range.setValue(range.get() + 1));
    }

    public void increaseMaxCount() {
        Platform.runLater(() -> maxCount.setValue(maxCount.get() + 1));
    }

    public void increaseSpeed() {
        setSpeed(getSpeed() < MAXSPEED ? getSpeed() + 0.0125 : getSpeed());
    }

    private void initAnimations() {
        {
            new Bomb(this, null, 0, null);
        }
        setAnimationFront();
        Timer movement = new Timer();
        movement.schedule(new TimerTask() {
            double x = getX();
            double y = getY();

            @Override
            public void run() {
                updateBlock();
                checkBonuses();
                if (getSide() == Side.TOP && topIsClear()) {
                    y = getY() - getSpeed();
                } else if (getSide() == Side.BOTTOM && bottomIsClear()) {
                    y = getY() + getSpeed();
                } else if (getSide() == Side.LEFT && leftIsClear()) {
                    x = getX() - getSpeed();
                } else if (getSide() == Side.RIGHT && rightIsClear()) {
                    x = getX() + getSpeed();
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

    private void checkBonuses() {
        for (Bonus bonus : bonuses) {
            if (bonus.intersects(getBoundsInLocal())) {
                bonus.activate(this);
                break;
            }
        }
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


    public Bomb putBomb() {
        return Bomb.newBomb(this, getBlockArray(), getBlockSize(), getChildren());
    }
}
