package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public abstract class Bonus extends Entity {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public Bonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {
    super(WIDTH, HEIGHT, currentBlock, blockArray, blockSize, children);
}

}

class BombAmountBonus extends Bonus{
    public BombAmountBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children){
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\BombPowerup.png")));
    }
}

class ExplosionRangeBonus extends Bonus{
    public ExplosionRangeBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children){
        super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\FlamePowerup.png")));
    }
}

class SpeedBonus extends Bonus {

     public SpeedBonus(GameBlock currentBlock, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children){
         super(currentBlock, blockArray, blockSize, children);
        setFill(new ImagePattern(new Image("Powerups\\SpeedPowerup.png")));
    }

}
