package program;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;

import javax.swing.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.UnaryOperator;
import java.io.File;
import javafx.scene.media.MediaPlayer;

public class Bomb extends Entity {
    private static Image[] animation = new Image[3];
    private static Image[] flames = new Image[5];
    private Bomb bomb = this;
    private Player player;

    static {
        for (int i = 0; i < animation.length; i++) {
            animation[i] = new Image(String.format("Bomb/Bomb_f0%d.png", i + 1));
        }
        for (int i = 0; i < flames.length; i++) {
            flames[i] = new Image(String.format("Flame/Flame_f0%d.png", i));
        }
    }

    public static Bomb newBomb(Player player, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        GameBlock currentBlock = player.getCurrentBlock();
        if (!currentBlock.containsEntity() & player.getCurrentCount() < player.getMaxCount())
            return new Bomb(player, blockArray, blockSize, children);
        else
            return null;
    }

    public Bomb(Player player, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(30, 30, player.getCurrentBlock(), blockArray, blockSize, children);
        setFill(new ImagePattern(animation[0]));
        player.setCurrentCount(player.getCurrentCount() + 1);
        this.player = player;
    }

    public void start(ObservableList<Node> children) {
        new BombHandler(children).execute();
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
            Media media = new Media(new File("Sounds\\Fire.wav").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
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
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    children.replaceAll(operator);
                    Enemy.updateMobs();
                }
            });
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
            for (int i = 0; i < player.getRange(); i++) {
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
            for (int i = 0; i < player.getRange(); i++) {
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
            for (int i = 0; i < player.getRange(); i++) {
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
            for (int i = 0; i < player.getRange(); i++) {
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
                player.setCurrentCount(player.getCurrentCount() - 1);
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
