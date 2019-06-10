package program;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Player extends Creature {
    private BooleanProperty isAlive = new SimpleBooleanProperty(true);
    private static HashSet<Bonus> bonuses = Bonus.getBonuses();
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private double physX;
    private double physY;
    private int ID;

    private AnimationTimer animation;

    public double getSpeed() {
        return speed.get();
    }

    public int getID() {
        return ID;
    }

    private DoubleProperty speed = new SimpleDoubleProperty(1.75);
    private static final double MAXSPEED = 3.5;

    private final IntegerProperty maxCount = new SimpleIntegerProperty(1);
    private int currentCount;
    private final IntegerProperty range = new SimpleIntegerProperty(1);
    private Task<Void> checker;

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double value) {
        Platform.runLater(() -> speed.setValue(value));
    }

    Player(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children, int ID) { //Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 1);
        physX = getX();
        physY = getY();
        this.ID = ID;
        startMovement();
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
        setSpeed(getSpeed() < MAXSPEED ? getSpeed() + 0.025 : getSpeed());
    }

    public boolean isIsAlive() {
        return isAlive.get();
    }

    public BooleanProperty isAliveProperty() {
        return isAlive;
    }

    void startMovement() {
        setAnimationFront();
        animation = new AnimationTimer() {
            private Runnable updater;
            private Runnable renderer;
            private Runnable animator;
            private Consumer<Float> interpolator;

            {
                interpolator = new Consumer<Float>() {
                    @Override
                    public void accept(Float alpha) {
                        if (getSide() == Side.NORTH && northIsClear())
                            setTranslateY(-getSpeed() * alpha);
                        else if (getSide() == Side.SOUTH && southIsClear())
                            setTranslateY(getSpeed() * alpha);
                        else if (getSide() == Side.WEST && westIsClear())
                            setTranslateX(-getSpeed() * alpha);
                        else if (getSide() == Side.EAST && eastIsClear())
                            setTranslateX(getSpeed() * alpha);
                    }
                };

                updater = new Runnable() {
                    @Override
                    public void run() {
                        updateBlock();
                        checkBonuses();
                        if (getSide() == Side.NORTH && northIsClear())
                            physY = physY - getSpeed();
                        else if (getSide() == Side.SOUTH && southIsClear())
                            physY = physY + getSpeed();
                        else if (getSide() == Side.WEST && westIsClear())
                            physX = physX - getSpeed();
                        else if (getSide() == Side.EAST && eastIsClear())
                            physX = physX + getSpeed();
                    }
                };

                renderer = new Runnable() {
                    @Override
                    public void run() {
                        setTranslateX(0);
                        setTranslateY(0);
                        setX(physX);
                        setY(physY);
                    }
                };

                animator = new Runnable() {
                    @Override
                    public void run() {
                        switch (getSide()) {
                            case NORTH:
                                setAnimationBack();
                                break;
                            case WEST:
                                setAnimationLeft();
                                break;
                            case SOUTH:
                                setAnimationFront();
                                break;
                            case EAST:
                                setAnimationRight();
                        }
                    }
                };
            }

            private static final float timeStep = 0.0166f;
            private float accumulatedTime = 0;
            private long previousTime = 0;
            private long lastUpdate;

            @Override
            public void handle(long currentTime) {
                if (currentTime - lastUpdate >= 150_000_000) {
                    animator.run();
                    lastUpdate = currentTime;
                }

                if (previousTime == 0) {
                    previousTime = currentTime;
                    return;
                }

                float secondsElapsed = (currentTime - previousTime) / 1e9f;
                float secondsElapsedCapped = Math.min(secondsElapsed, 0.05f);
                accumulatedTime += secondsElapsedCapped;
                previousTime = currentTime;

                if (accumulatedTime < timeStep) {
                    float remainderOfTimeStepSincePreviousInterpolation =
                            timeStep - (accumulatedTime - secondsElapsed);
                    float alphaInRemainderOfTimeStep =
                            secondsElapsed / remainderOfTimeStepSincePreviousInterpolation;
                    interpolator.accept(alphaInRemainderOfTimeStep);
                    return;
                }

                while (accumulatedTime >= 2 * timeStep) {
                    updater.run();
                    accumulatedTime -= timeStep;
                }
                renderer.run();
                updater.run();
                accumulatedTime -= timeStep;
                float alpha = accumulatedTime / timeStep;
                interpolator.accept(alpha);
            }

            @Override
            public void stop() {
                previousTime = 0;
                accumulatedTime = 0;
                super.stop();
            }
        };
        animation.start();
        checker = new EnemyChecker();
        Thread enemyChecker = new Thread(checker);
        enemyChecker.setDaemon(true);
        enemyChecker.start();
    }

    @Override
    public void decreaseLife() {
        super.decreaseLife();
        Sounds.playHit();
    }

    private void checkBonuses() {
        for (Bonus bonus : bonuses) {
            if (bonus.intersects(getBounds())) {
                bonus.activate(this);
                break;
            }
        }
    }

    public Bounds getBounds() {
        return new Rectangle(physX, physY, WIDTH, HEIGHT).getBoundsInLocal();
    }

    @Override
    public void kill() {
        super.kill();
        checker.cancel();
        isAliveProperty().set(false);
        Sounds.playLost();
    }

    @Override
    void stopAnimations() {
        animation.stop();
    }

    public void setAnimationFront() {
        // javafx.scene.image.Image image = new Image("Bomberman\\Front\\Bman_F_f00.png");
        setFill(new ImagePattern(Animation.getPlayerAnimationFront()));
    }

    public void setAnimationBack() {
        setFill(new ImagePattern(Animation.getPlayerAnimationBack()));
    }

    public void setAnimationRight() {
        setFill(new ImagePattern(Animation.getPlayerAnimationRight()));
    }

    public void setAnimationLeft() {
        setFill(new ImagePattern(Animation.getPlayerAnimationLeft()));
    }


    public void putBomb() {
        Bomb bomb = Bomb.newBomb(this, getBlockArray(), getBlockSize(), getChildren());
        if (bomb != null) {
            getChildren().add(bomb);
            bomb.activate();
        }
    }

    class EnemyChecker extends Task<Void> {
        Set<Enemy> enemies = Enemy.getEnemies();

        @Override
        protected Void call() throws Exception {
            while (!isCancelled()) {
                for (Enemy enemy : enemies) {
                    if (intersects((enemy.getBoundsInParent()))) {
                        decreaseLife();
                        Thread.sleep(2500);
                    }
                }
                Thread.sleep(100);
            }
            return null;
        }
    }
}
