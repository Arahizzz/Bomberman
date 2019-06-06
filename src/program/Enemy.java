package program;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Creature {

    private static HashSet<Enemy> enemies = new HashSet<>();
    private static double speed = 1.5;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private boolean activated = false;

    Enemy(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {//Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 1);
        enemies.add(this);
        initAnimations();
    }

    private void initAnimations() {
        setSide(Side.NONE);
        setAnimationFront();
        freeMob();
    }

    public void setAnimationFront(){
        // javafx.scene.image.Image image = new Image("Enemy\\Front\\Creep_F_f00.png");
        setFill(new ImagePattern(Animation.getEnemyAnimationFront()));
    }

    void startMovement() {
        Timer movement = new Timer();
        movement.schedule(new TimerTask() {
            @Override
            public void run() {
                updateBlock();
                if (frontIsClear()) {
                    moveForward();
                } else
                    turnBack();
            }
        }, 1000, 10);

        Timer animation = new Timer();
        animation.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (getSide()) {
                    case TOP:
                        Platform.runLater(() -> setAnimationBack());
                        break;
                    case BOTTOM:
                        Platform.runLater(() -> setAnimationFront());
                        break;
                    case RIGHT:
                        Platform.runLater(() -> setAnimationRight());
                        break;
                    case LEFT:
                        Platform.runLater(() -> setAnimationLeft());
                        break;
                }
            }
        }, 1000, 100);
    }

    private void moveForward() {
        switch (getSide()) {
            case TOP:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setY(getY() - speed);
                    }
                });
                break;
            case BOTTOM:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setY(getY() + speed);
                    }
                });
                break;
            case RIGHT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(getX() + speed);
                    }
                });
                break;
            case LEFT:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(getX() - speed);
                    }
                });
                break;
        }
    }

    private void turnBack() {
        switch (getSide()) {
            case LEFT:
                setSide(Side.RIGHT);
                break;
            case RIGHT:
                setSide(Side.LEFT);
                break;
            case BOTTOM:
                setSide(Side.TOP);
                break;
            case TOP:
                setSide(Side.BOTTOM);
                break;
        }
    }

    public void setAnimationBack(){
        setFill(new ImagePattern(Animation.getEnemyAnimationBack()));
    }

    public void setAnimationRight(){
        setFill(new ImagePattern(Animation.getEnemyAnimationRight()));
    }

    public void setAnimationLeft() {
        setFill(new ImagePattern(Animation.getEnemyAnimationLeft()));
    }

    @Override
    public void kill() {
        enemies.remove(this);
        super.kill();
    }

    public static void updateMobs() {
        for (Enemy enemy : enemies) {
            enemy.checkIfFree();
        }
    }

    public void checkIfFree() {
        if (!activated) {
            freeMob();
        }
    }

    void freeMob() {
        if (getTopBlock().isWalkAllowed())
            setSide(Side.TOP);
        else if (getBottomBlock().isWalkAllowed())
            setSide(Side.BOTTOM);
        else if (getLeftBlock().isWalkAllowed())
            setSide(Side.LEFT);
        else if (getRightBlock().isWalkAllowed())
            setSide(Side.RIGHT);
        if (getSide() != Side.NONE) {
            startMovement();
            activated = true;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public static HashSet<Enemy> getEnemies() {
        return enemies;
    }
}
