package program;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


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

    public GameBlock getBottomBlock() {
        return blockArray[currentBlock.getVerticalIndex() + 1][currentBlock.getHorizontalIndex()];
    }

    public GameBlock getRightBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() + 1];
    }

    public GameBlock getLeftBlock() {
        return blockArray[currentBlock.getVerticalIndex()][currentBlock.getHorizontalIndex() - 1];
    }

    public GameBlock getTopBlock() {
        return blockArray[currentBlock.getVerticalIndex() - 1][currentBlock.getHorizontalIndex()];
    }

    public GameBlock getBottomBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex() + 1][block.getHorizontalIndex()];
    }

    public GameBlock getRightBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex()][block.getHorizontalIndex() + 1];
    }

    public GameBlock getLeftBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex()][block.getHorizontalIndex() - 1];
    }

    public GameBlock getTopBlock(GameBlock block) {
        return blockArray[block.getVerticalIndex() - 1][block.getHorizontalIndex()];
    }


    public boolean northIsClear() {
        return currentBlock.isOnVerticalRail(this) && (getTopBlock().isWalkAllowed() || getY() > currentBlock.getY() + 5);
    }

    public boolean southIsClear() {
        return currentBlock.isOnVerticalRail(this) && (getBottomBlock().isWalkAllowed() || getY() + getHeight() - 10 < currentBlock.getY() + getHeight() - 2);
    }

    public boolean westIsClear() {
        return currentBlock.isOnHorizontalRail(this) && (getLeftBlock().isWalkAllowed() || getX() > currentBlock.getX() + 5);
    }

    public boolean eastIsClear() {
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
    private static Set<Creature> creatures;
    private IntegerProperty life = new SimpleIntegerProperty();

    static {
        ConcurrentHashMap<Creature, Boolean> map = new ConcurrentHashMap<>();
        creatures = map.keySet(true);
    }

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
        stopAnimations();
        Platform.runLater(() -> getChildren().remove(this));
        creatures.remove(this);
    }

    public static Set<Creature> getCreatures() {
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
            case NORTH:
                if (getTopBlock().isInsideBlock(getBounds()))
                    setCurrentBlock(getTopBlock());
                break;
            case WEST:
                if (getLeftBlock().isInsideBlock(getBounds()))
                    setCurrentBlock(getLeftBlock());
                break;
            case EAST:
                if (getRightBlock().isInsideBlock(getBounds()))
                    setCurrentBlock(getRightBlock());
                break;
            case SOUTH:
                if (getBottomBlock().isInsideBlock(getBounds()))
                    setCurrentBlock(getBottomBlock());
                break;
        }
    }

    abstract void startMovement();

    abstract void stopAnimations();

    abstract Bounds getBounds();
}

enum Side {
    WEST, EAST, NORTH, SOUTH, NONE,
    TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
}