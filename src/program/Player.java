package program;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Rectangle {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 50;
    private static final int SPEED = 2;
    Point centerLocation;// координати центру гравця
    GameBlock[][] blockArray;
    int blockSize;
    Side side;

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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            double x = getX();
            double y = getY();

            @Override
            public void run() {
                if (side == Side.TOP)
                    y = getY() - SPEED;
                else if (side == Side.BOTTOM)
                    y = getY() + SPEED;
                else if (side == Side.LEFT)
                    x = getX() - SPEED;
                else if (side == Side.RIGHT)
                    x = getX() + SPEED;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setX(x);
                        setY(y);
                    }
                });
            }
        }, 0, 10);

    }

    private Point getArrayCoordinates() {// повертає координати блока в масиві (рядок і стовчик)
        return new Point((int) centerLocation.getX() / blockSize, (int) centerLocation.getY() / blockSize);
    }


    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
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

enum Side {
    LEFT, RIGHT, TOP, BOTTOM, NONE
}
