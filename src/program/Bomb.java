package program;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.function.UnaryOperator;

public class Bomb extends Entity {
    private static Image[] animation = new Image[3];
    private static Image[] flames = new Image[5];
    private static final IntegerProperty maxCount = new SimpleIntegerProperty(1);
    private static int currentCount = -1;
    private static final IntegerProperty range = new SimpleIntegerProperty(1);
    private Bomb bomb = this;

    static {
        for (int i = 0; i < animation.length; i++) {
            animation[i] = new Image(String.format("Bomb/Bomb_f0%d.png", i + 1));
        }
        for (int i = 0; i < flames.length; i++) {
            flames[i] = new Image(String.format("Flame/Flame_f0%d.png", i));
        }
    }

    public static Bomb newBomb(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        if (!currentBlock.containsEntity() & currentCount < getMaxCount())
            return new Bomb(currentBlock, blockArray, blockSize, children);
        else
            return null;
    }

    public Bomb(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(30, 30, currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(animation[0]));
        currentCount++;
    }

    public void start(ObservableList<Node> children) {
        new BombHandler(children).execute();
    }

    public static void increaseRange() {
        Platform.runLater(() -> range.setValue(range.get() + 1));
    }

    public static void incraseAmount() {
        Platform.runLater(() -> maxCount.setValue(maxCount.get() + 1));
    }

    public static int getMaxCount() {
        return maxCount.get();
    }

    public static int getRange() {
        return range.get();
    }

    public static StringBinding maxCountProperty() {
        return maxCount.asString();
    }

    public static StringBinding rangeProperty() {
        return range.asString();
    }

    public void explode(ObservableList<Node> children) {
        children.remove(this);
    }

    class BombHandler extends SwingWorker<Void, Void> {
        ObservableList<Node> children;
        GameBlock[][] blocks = getBlockArray();
        HashSet<GameBlock> damagedZone = new HashSet<>();

        public BombHandler(ObservableList<Node> children) {
            this.children = children;
        }

        @Override
        protected void done() {
            new ExplosionHandler().execute();
            new Killer().execute();
        }

        private void explode() {
            UnaryOperator<Node> operator = new UnaryOperator<Node>() {
                @Override
                public Node apply(Node node) {
                    if (node instanceof RedBrick) {
                        RedBrick block = (RedBrick) node;
                            if (damagedZone.contains(block)) {
                                Bonus bonus = block.generateBonus(children);
                                final GrassBlock grassBlock = new GrassBlock((int) block.getX(), (int) block.getY(), (int) block.getWidth(),
                                        (int) block.getHeight(), block.getVerticalIndex(), block.getHorizontalIndex());
                                blocks[block.getVerticalIndex()][block.getHorizontalIndex()] = grassBlock;
                                if (bonus != null)
                                    Platform.runLater(() -> children.add(bonus));
                                return grassBlock;
                            }
                        return block;
                    }
                    return node;
                }
            };
            Platform.runLater(() -> children.replaceAll(operator));
        }

        @Override
        protected Void doInBackground() throws Exception {
            calculateDamage();
            Thread.sleep(1000);
            Platform.runLater(() -> setFill(new ImagePattern(animation[1])));
            Thread.sleep(1000);
            Platform.runLater(() -> setFill(new ImagePattern(animation[2])));
            Thread.sleep(1000);
            return null;
        }

        private void calculateDamage() {
            GameBlock block = getCurrentBlock();
            damagedZone.add(block);
            //Top
            for (int i = 0; i < getRange(); i++) {
                block = getTopBlock(block);
                if (block.isBreakable()) {
                    damagedZone.add(block);
                    break;
                } else if (!block.isWalkAllowed())
                    break;
                else
                    damagedZone.add(block);
            }
            //Left
            block = getCurrentBlock();
            for (int i = 0; i < getRange(); i++) {
                block = getLeftBlock(block);
                if (block.isBreakable()) {
                    damagedZone.add(block);
                    break;
                } else if (!block.isWalkAllowed())
                    break;
                else
                    damagedZone.add(block);
            }
            //Right
            block = getCurrentBlock();
            for (int i = 0; i < getRange(); i++) {
                block = getRightBlock(block);
                if (block.isBreakable()) {
                    damagedZone.add(block);
                    break;
                } else if (!block.isWalkAllowed())
                    break;
                else
                    damagedZone.add(block);
            }
            //Bottom
            block = getCurrentBlock();
            for (int i = 0; i < getRange(); i++) {
                block = getBottomBlock(block);
                if (block.isBreakable()) {
                    damagedZone.add(block);
                    break;
                } else if (!block.isWalkAllowed())
                    break;
                else
                    damagedZone.add(block);
            }
        }

        private class ExplosionHandler extends SwingWorker<Void, Void> {

            @Override
            protected Void doInBackground() throws Exception {
                LinkedList<Node> images = new LinkedList<>();
                Platform.runLater(() -> children.remove(bomb));
                currentCount--;
                getCurrentBlock().setContainsEntity(false);
                for (int i = 0; i < flames.length; i++) {
                    for (GameBlock block : damagedZone) {
                        ImageView imageView = new ImageView(flames[i]);
                        imageView.setX(block.getX());
                        imageView.setY(block.getY());
                        imageView.setFitWidth(block.getWidth());
                        imageView.setFitHeight(block.getHeight());
                        Platform.runLater(() -> children.add(imageView));
                        images.add(imageView);
                    }
                    Thread.sleep(100);
                    Platform.runLater(() -> children.removeAll(images));
                }
                return null;
            }

            @Override
            protected void done() {
                explode();
            }
        }

        private class Killer extends SwingWorker<Void, Void> {
            @Override
            protected Void doInBackground() throws Exception {
                for (GameBlock block : damagedZone) {
                    for (Creature creature : Creature.getCreatures()) {
                        if (block.containsCreature(creature))
                            creature.decreaseLife();
                    }
                }
                return null;
            }
        }
    }
}
