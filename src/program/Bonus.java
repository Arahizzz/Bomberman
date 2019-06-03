package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public abstract class Bonus extends Entity {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public Bonus(GameBlock currentBlock) {
    super(WIDTH, HEIGHT, currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children);
}

}

class BombAmountBonus extends Bonus{
    public BombAmountBonus(){
        setFill(new ImagePattern(new Image("Powerups\\BombPowerup.png")));
    }
}

class ExplosionRangeBonus extends Bonus{
    public ExplosionRangeBonus(){
        setFill(new ImagePattern(new Image("Powerups\\FlamePowerup.png")));
    }
}

class SpeedBonus extends Bonus {

    public SpeedBonus(GameBlock currentBlock){
        super(currentBlock);
        setFill(new ImagePattern(new Image("Powerups\\SpeedPowerup.png")));
    }

}
