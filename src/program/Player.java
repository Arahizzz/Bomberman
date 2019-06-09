package program;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;

import java.util.HashSet;
import java.util.Set;

public class Player extends Creature {
    private BooleanProperty isAlive = new SimpleBooleanProperty(true);
    private static HashSet<Bonus> bonuses = Bonus.getBonuses();
    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;

    private AnimationTimer animation;

    public double getSpeed() {
        return speed.get();
    }

    private DoubleProperty speed = new SimpleDoubleProperty(0.65);
    private static final double MAXSPEED = 1.5;

    private final IntegerProperty maxCount = new SimpleIntegerProperty(1);
    private int currentCount = -1;
    private final IntegerProperty range = new SimpleIntegerProperty(1);
    private Task<Void> checker;

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double value) {
        Platform.runLater(() -> speed.setValue(value));
    }

    Player(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) { //Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 3);
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
        setSpeed(getSpeed() < MAXSPEED ? getSpeed() + 0.02 : getSpeed());
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
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                updateBlock();
                checkBonuses();
                if (getSide() == Side.NORTH && northIsClear()) {
                    setY(getY() - getSpeed());
                } else if (getSide() == Side.SOUTH && southIsClear()) {
                    setY(getY() + getSpeed());
                } else if (getSide() == Side.WEST && westIsClear()) {
                    setX(getX() - getSpeed());
                } else if (getSide() == Side.EAST && eastIsClear()) {
                    setX(getX() + getSpeed());
                }
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
                    lastUpdate = now;
                }
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
            if (bonus.intersects(getBoundsInLocal())) {
                bonus.activate(this);
                break;
            }
        }
    }

    @Override
    public void kill() {
        super.kill();
        checker.cancel();
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


    public Bomb putBomb() {
        return Bomb.newBomb(this, getBlockArray(), getBlockSize(), getChildren());
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
