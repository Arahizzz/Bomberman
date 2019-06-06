package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Enemy extends Creature {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;


    Enemy(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {//Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 1);
       // initAnimations();
    }

}
