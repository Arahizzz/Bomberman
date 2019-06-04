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

    public Bonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(WIDTH, HEIGHT, currentBlock, blockArray, blockSize, children);
        bonuses.add(this);
    }

    abstract void activate(Player player);

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
        Bomb.incraseAmount();
        Platform.runLater(() -> getChildren().remove(this));
    }
}

class ExplosionRangeBonus extends Bonus {
    public ExplosionRangeBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\FlamePowerup.png")));
    }

    @Override
    void activate(Player player) {
        Bomb.increaseRange();
        Platform.runLater(() -> getChildren().remove(this));
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
        Platform.runLater(() -> getChildren().remove(this));
    }
}

enum Bonuses {

    SPEED_BONUS,
    EXPLOSION_RANGE_BONUS,
    BOMB_AMOUNT_BONUS,
    HEART_BONUS, // має менший шанс випасти
    //ці бонуси робити окремо, бо вони мають випади один раз за гру (матимуть малий шанс дропу)
    DETONATOR, // бомби вибухають не по таймеру, за натиском кнопки гравцем
    WALING_THROUGH_WALLS // гравець може ходити крізь блоки(трава та redbrick)


}
