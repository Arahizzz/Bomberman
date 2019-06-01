package program;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Player extends Rectangle {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final int SPEED = 5;
    Point centerLocation;// координати центру гравця
    GameBlock[][] blockArray;
    int blockSize;

    // додати характеристи
    Player(Point location, GameBlock[][] blockArray, int blockSize) { //Point location - це координати блоку (лівий верхній кут)
        this.blockSize = blockSize;
        this.centerLocation = new Point((int) location.getY(), (int) location.getX());
        this.blockArray = blockArray;

        super.setWidth(WIDTH);
        super.setHeight(HEIGHT);

        super.setX(location.getX() + (blockSize - WIDTH) / 2); //встановлюємо гравця по цетрі квадратика
        super.setY(location.getY() + (blockSize - HEIGHT) / 2);

        setFill(Color.RED);

    }

    private Point getArrayCoordinates() {// повертає координати блока в масиві (рядок і стовчик)
        return new Point((int) centerLocation.getX() / blockSize, (int) centerLocation.getY() / blockSize);
    }


    public void moveUp() {
        super.setY(super.getY() - SPEED);
//        if (topClear()) {
//            super.setY(super.getY() - SPEED);
//            centerLocation.setLocation(centerLocation.getX(),centerLocation.getY()-SPEED);
//        }
    }

    public void moveDown() {
        super.setY(super.getY() + SPEED);
//        if (bottomClear()) {
//            super.setY(super.getY() + SPEED);
//            centerLocation.setLocation(centerLocation.getX(),centerLocation.getY()+SPEED);
//        }
    }

    public void moveLeft() {
        super.setX(super.getX() - SPEED);
//        if (leftClear()) {
//            super.setX(super.getX() - SPEED);
//centerLocation.setLocation(centerLocation.getX()-SPEED,centerLocation.getY());
//        }
    }

    public void moveRight() {
        super.setX(super.getX() + SPEED);
//        if (rightClear()) {
//            super.setX(super.getX() + SPEED);
//            centerLocation.setLocation(centerLocation.getX()+SPEED,centerLocation.getY());
//        }
    }

    public boolean topClear() {

        return blockArray[(int) getArrayCoordinates().getX() - 1][(int) getArrayCoordinates().getY()].isWalkAllowed();
    }

    public boolean bottomClear() {
        return blockArray[(int) getArrayCoordinates().getX() + 1][(int) getArrayCoordinates().getY()].isWalkAllowed();
    }

    public boolean leftClear() {
        return blockArray[(int) getArrayCoordinates().getX()][(int) getArrayCoordinates().getY() - 1].isWalkAllowed();
    }

    public boolean rightClear() {
        return blockArray[(int) getArrayCoordinates().getX()][(int) getArrayCoordinates().getY() + 1].isWalkAllowed();
    }
}
