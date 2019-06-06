package program;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;


abstract public class Entity extends Rectangle {
    private GameBlock currentBlock;
    private GameBlock[][] blockArray;
    private int blockSize;
    private ObservableList<Node> children;

    public Entity(double width, double height, GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(width, height);
        super.setX(currentBlock.getCenterCoordinatesX(blockSize, width));
        super.setY(currentBlock.getCenterCoordinatesY(blockSize, height));
        this.currentBlock = currentBlock;
        this.blockArray = blockArray;
        this.blockSize = blockSize;
        this.children = children;
    }

    public GameBlock getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(GameBlock currentBlock) {
        this.currentBlock = currentBlock;
    }

    public GameBlock[][] getBlockArray() {
        return blockArray;
    }

    public void setBlockArray(GameBlock[][] blockArray) {
        this.blockArray = blockArray;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    GameBlock getBottomBlock() {
        return blockArray[currentBlock.getVerticalIndex() + 1][currentBlock.getHorizontalIndex()];
    }

    GameBlock getRightBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() + 1];
    }

    GameBlock getLeftBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() - 1];
    }

    GameBlock getTopBlock() {
        return blockArray[currentBlock.getVerticalIndex() - 1][currentBlock.getHorizontalIndex()];
    }

    GameBlock getBottomBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex() + 1][block.getHorizontalIndex()];
    }

    GameBlock getRightBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex()][block.getHorizontalIndex() + 1];
    }

    GameBlock getLeftBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex()][block.getHorizontalIndex() - 1];
    }

    GameBlock getTopBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex() - 1][block.getHorizontalIndex()];
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

    public ObservableList<Node> getChildren() {
        return children;
    }

    public void setChildren(ObservableList<Node> children) {
        this.children = children;
    }
}

abstract class Creature extends Entity {
    private Side side = Side.NONE;
    private static HashSet<Creature> creatures = new HashSet<>();
    private IntegerProperty life = new SimpleIntegerProperty();
    private boolean activated = false;

    public int getLife() {
        return life.get();
    }

    public void setLife(int life) {
        Platform.runLater(() -> this.life.setValue(life));
    }

    public Creature(double width, double height, GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children, int life) {
        super(width, height, currentBlock, blockArray, blockSize, children);
        this.life.setValue(life);
        creatures.add(this);
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public void kill() {
        Platform.runLater(() -> getChildren().remove(this));
        creatures.remove(this);
    }

    public static HashSet<Creature> getCreatures() {
        return creatures;
    }

    public void decreaseLife() {
        if (getLife() == 1)
            kill();
        else
            setLife(getLife() - 1);
    }

    public StringBinding lifeProperty() {
        return life.asString();
    }

    public void increaseLife() {
        setLife(getLife() + 1);
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

    public boolean frontIsClear() {
        switch (getSide()) {
            case LEFT:
                return leftIsClear();
            case RIGHT:
                return rightIsClear();
            case BOTTOM:
                return bottomIsClear();
            case TOP:
                return topIsClear();
            default:
                return false;
        }
    }

    public static void updateMobs() {
        for (Creature creature : getCreatures()) {
            creature.checkIfFree();
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

    abstract void startMovement();
}

enum Side {
    LEFT, RIGHT, TOP, BOTTOM, NONE,
    TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
}