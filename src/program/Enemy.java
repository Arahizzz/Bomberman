package program;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Creature {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;


    Enemy(GameBlock spawn, GameBlock[][] blockArray, int blockSize, ObservableList<Node> children) {//Point location - це координати блоку (лівий верхній кут)
        super(WIDTH, HEIGHT, spawn, blockArray, blockSize, children, 1);
        initAnimations();
    }

    private void initAnimations() {
        setAnimationFront();
    }

    public void setAnimationFront(){
        // javafx.scene.image.Image image = new Image("Enemy\\Front\\Creep_F_f00.png");
        setFill(new ImagePattern(Animation.getEnemyAnimationFront()));
    }

    private void startMovement() {

    }

    public void setAnimationBack(){
        setFill(new ImagePattern(Animation.getEnemyAnimationBack()));
    }

    public void setAnimationRight(){
        setFill(new ImagePattern(Animation.getEnemyAnimationRight()));
    }

    public void setAnimationLeft() {
        setFill(new ImagePattern(Animation.getEnemyAnimationLeft()));
    }

}
