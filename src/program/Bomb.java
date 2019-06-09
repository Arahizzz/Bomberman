package program;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.UnaryOperator;

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

    public void activate() {
        Task<HashSet<GameBlock>> calculateDamage = new DamageCalculator();
        Thread thread = new Thread(calculateDamage);
        thread.setDaemon(true);
        thread.start();

        final Timeline bombCountDown = new Timeline();
        final KeyValue value1 = new KeyValue(fillProperty(), new ImagePattern(animation[0]));
        final KeyFrame frame1 = new KeyFrame(Duration.ZERO, value1);
        final KeyValue value2 = new KeyValue(fillProperty(), new ImagePattern(animation[1]));
        final KeyFrame frame2 = new KeyFrame(Duration.millis(1000), value2);
        final KeyValue value3 = new KeyValue(fillProperty(), new ImagePattern(animation[2]));
        final KeyFrame frame3 = new KeyFrame(Duration.millis(1750), value3);
        final KeyFrame frame4 = new KeyFrame(Duration.millis(2500), value3);
        bombCountDown.getKeyFrames().addAll(frame1, frame2, frame3, frame4);

        bombCountDown.setOnFinished(e -> {
            Sounds.playExplosion();

            Thread flames = new Thread(new Flames(calculateDamage.getValue(), getChildren()));
            flames.setDaemon(true);
            flames.start();

            Thread killer = new Thread(new Killer(calculateDamage.getValue()));
            killer.setDaemon(true);
            killer.start();

            Thread explosion = new Thread(new Explosion(calculateDamage.getValue(), getChildren()));
            explosion.setDaemon(true);
            explosion.start();
        });

        bombCountDown.play();
    }

    private class DamageCalculator extends Task<HashSet<GameBlock>> {
        HashSet<GameBlock> damagedZone = new HashSet<>();

        @Override
        protected HashSet<GameBlock> call() throws Exception {
            calculateDamage();
            return damagedZone;
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
    }

    private class Flames extends Task<Void> {
        private HashSet<GameBlock> damagedZone;
        private ObservableList<Node> children;

        public Flames(HashSet<GameBlock> damagedZone, ObservableList<Node> children) {
            this.damagedZone = damagedZone;
            this.children = children;
        }

        @Override
        protected Void call() throws Exception {
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
    }

    private class Explosion extends Task<Void> {
        private HashSet<GameBlock> damagedZone;
        private ObservableList<Node> children;

        public Explosion(HashSet<GameBlock> damagedZone, ObservableList<Node> children) {
            this.damagedZone = damagedZone;
            this.children = children;
        }

        @Override
        protected Void call() throws Exception {
            UnaryOperator<Node> operator = new UnaryOperator<Node>() {
                @Override
                public Node apply(Node node) {
                    if (node instanceof RedBrick) {
                        RedBrick block = (RedBrick) node;
                        if (damagedZone.contains(block)) {
                            Bonus bonus = block.generateBonus(children);
                            final GrassBlock grassBlock = new GrassBlock((int) block.getX(), (int) block.getY(), (int) block.getWidth(),
                                    (int) block.getHeight(), block.getVerticalIndex(), block.getHorizontalIndex());
                            getBlockArray()[block.getVerticalIndex()][block.getHorizontalIndex()] = grassBlock;
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
            return null;
        }
    }

    private class Killer extends Task<Void> {
        Set<GameBlock> damagedZone;

        public Killer(Set<GameBlock> damagedZone) {
            this.damagedZone = damagedZone;
        }

        @Override
        protected Void call() throws Exception {
            try {
                for (GameBlock block : damagedZone) {
                    for (Creature creature : Creature.getCreatures()) {
                        if (block.containsCreature(creature))
                            creature.decreaseLife();
                    }
                }
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
