package program;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.HashSet;

public abstract class Bonus extends Entity {
    private static HashSet<Bonus> bonuses = new HashSet<>();
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    public static final int SPEED_BONUS = 2;
    public static final int EXPLOSION_RANGE_BONUS=3;
    public static final int BOMB_AMOUNT_BONUS=4;
    //HEART_BONUS, // має менший шанс випасти
    //ці бонуси робити окремо, бо вони мають випади один раз за гру (матимуть малий шанс дропу)
    //DETONATOR, // бомби вибухають не по таймеру, за натиском кнопки гравцем
    //WALING_THROUGH_WALLS // гравець може ходити крізь блоки(трава та redbrick)



    public Bonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(WIDTH, HEIGHT, currentBlock, blockArray, blockSize, children);
        bonuses.add(this);
    }

    void activate(Player player) {
        Platform.runLater(() -> getChildren().remove(this));
        bonuses.remove(this);
    }

    public static HashSet<Bonus> getBonuses() {
        return bonuses;
    }
}

class BombAmountBonus extends Bonus {
    public BombAmountBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\BombPowerup.png")));
    }

    @Override
    void activate(Player player) {
        player.increaseMaxCount();
        super.activate(player);
    }
}

class ExplosionRangeBonus extends Bonus {
    public ExplosionRangeBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\FlamePowerup.png")));
    }

    @Override
    void activate(Player player) {
        player.increaseRange();
        super.activate(player);
    }
}

class SpeedBonus extends Bonus {

    public SpeedBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\SpeedPowerup.png")));
    }

    @Override
    void activate(Player player) {
        player.increaseSpeed();
        super.activate(player);
    }
}

