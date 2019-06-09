package program;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;

import java.util.HashSet;

public class Enemy extends Creature {

    private static HashSet<Enemy> enemies = new HashSet<>();
    private static double speed = 0.65;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private boolean activated = false;
    private static double turnProbability = 0.05;
    private AnimationTimer movement;
    private AnimationTimer animation;

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
        movement = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBlock();
                if (frontIsClear()) {
                    moveForward();
                } else
                    turnBack();
            }
        };
        movement.start();
        animation = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 150_000_000) {
                    switch (getSide()) {
                        case NORTH:
                            setAnimationBack();
                            break;
                        case SOUTH:
                            setAnimationFront();
                            break;
                        case EAST:
                            setAnimationRight();
                            break;
                        case WEST:
                            setAnimationLeft();
                            break;
                    }
                    turn();
                    lastUpdate = now;
                }
            }
        };
        animation.start();
    }

    private void moveForward() {
        switch (getSide()) {
            case NORTH:
                setY(getY() - speed);
                break;
            case SOUTH:
                setY(getY() + speed);
                break;
            case EAST:
                setX(getX() + speed);
                break;
            case WEST:
                setX(getX() - speed);
                break;
        }
    }

    private void turn() {
        if (leftIsClear() && RandomBoolean.get(turnProbability))
            turnLeft();
        else if (rightIsClear() && RandomBoolean.get(turnProbability))
            turnRight();
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

    public void cancelAnimations() {
        movement.stop();
        animation.stop();
    }

    public static void updateMobs() {
        if (enemies.size() != 0) {
            for (Enemy enemy : enemies) {
                enemy.checkIfFree();
            }
        } else
            Exit.activatePortal();
    }

    public void checkIfFree() {
        if (!activated) {
            freeMob();
        }
    }

    @Override
    public void updateBlock() {
        super.updateBlock();
        try {
            if (!getCurrentBlock().isWalkAllowed())
                throw new WrongBlockException();
        } catch (WrongBlockException e) {
            System.err.println("Mob in wrong block.");
            e.printStackTrace();
            kill();
        }
    }

    public void Repositioning() {
        if (getTopBlock().isWalkAllowed())
            Platform.runLater(() -> {
                setX(getTopBlock().getX() + 10);
                setY(getTopBlock().getY() + 10);
            });
        else if (getBottomBlock().isWalkAllowed())
            Platform.runLater(() -> {
                setX(getBottomBlock().getX() + 10);
                setY(getBottomBlock().getY() + 10);
            });
        else if (getLeftBlock().isWalkAllowed())
            Platform.runLater(() -> {
                setX(getLeftBlock().getX() + 10);
                setY(getLeftBlock().getY() + 10);
            });
        else if (getRightBlock().isWalkAllowed())
            Platform.runLater(() -> {
                setX(getRightBlock().getX() + 10);
                setY(getRightBlock().getY() + 10);
            });
        else
            kill();
    }

    void freeMob() {
        if (getTopBlock().isWalkAllowed())
            setSide(Side.NORTH);
        else if (getBottomBlock().isWalkAllowed())
            setSide(Side.SOUTH);
        else if (getLeftBlock().isWalkAllowed())
            setSide(Side.WEST);
        else if (getRightBlock().isWalkAllowed())
            setSide(Side.EAST);
        if (getSide() != Side.NONE) {
            startMovement();
            activated = true;
        }
    }

    public boolean frontIsClear() {
        switch (getSide()) {
            case WEST:
                return getLeftBlock().isWalkAllowed() || getX() > getCurrentBlock().getX() + 5;
            case EAST:
                return getRightBlock().isWalkAllowed() || getX() < getCurrentBlock().getX() + getWidth() - 5;
            case SOUTH:
                return getBottomBlock().isWalkAllowed() || getY() + getHeight() - 10 < getCurrentBlock().getY() + getHeight() - 2;
            case NORTH:
                return getTopBlock().isWalkAllowed() || getY() > getCurrentBlock().getY() + 5;
            default:
                return false;
        }
    }

    public boolean leftIsClear() {
        if (getCurrentBlock().fullyContains(getBoundsInLocal())) {
            switch (getSide()) {
                case NORTH:
                    return getLeftBlock().isWalkAllowed();
                case SOUTH:
                    return getRightBlock().isWalkAllowed();
                case WEST:
                    return getBottomBlock().isWalkAllowed();
                case EAST:
                    return getTopBlock().isWalkAllowed();
            }
        }
        return false;
    }

    public boolean rightIsClear() {
        if (getCurrentBlock().fullyContains(getBoundsInLocal())) {
            switch (getSide()) {
                case NORTH:
                    return getRightBlock().isWalkAllowed();
                case SOUTH:
                    return getLeftBlock().isWalkAllowed();
                case WEST:
                    return getTopBlock().isWalkAllowed();
                case EAST:
                    return getBottomBlock().isWalkAllowed();
            }
        }
        return false;
    }

    public boolean backIsClear() {
        switch (getSide()) {
            case NORTH:
                return getBottomBlock().isWalkAllowed();
            case SOUTH:
                return getTopBlock().isWalkAllowed();
            case WEST:
                return getRightBlock().isWalkAllowed();
            case EAST:
                return getLeftBlock().isWalkAllowed();
            default:
                return false;
        }
    }

    public void turnBack() {
        switch (getSide()) {
            case WEST:
                setSide(Side.EAST);
                break;
            case EAST:
                setSide(Side.WEST);
                break;
            case SOUTH:
                setSide(Side.NORTH);
                break;
            case NORTH:
                setSide(Side.SOUTH);
                break;
        }
    }

    public void turnLeft() {
        switch (getSide()) {
            case WEST:
                setSide(Side.SOUTH);
                break;
            case EAST:
                setSide(Side.NORTH);
                break;
            case SOUTH:
                setSide(Side.EAST);
                break;
            case NORTH:
                setSide(Side.WEST);
                break;
        }
    }

    public void turnRight() {
        switch (getSide()) {
            case WEST:
                setSide(Side.NORTH);
                break;
            case EAST:
                setSide(Side.SOUTH);
                break;
            case SOUTH:
                setSide(Side.WEST);
                break;
            case NORTH:
                setSide(Side.EAST);
                break;
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

class WrongBlockException extends Exception {

}
