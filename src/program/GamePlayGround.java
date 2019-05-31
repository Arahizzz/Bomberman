package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.Random;

public class GamePlayGround {
    GameBlock[][] blockArray;
    int blockSize;
    int blockNumberY = 13;
    int blockNumberX = 17;
    Point spawnCoordinates;
    Random random = new Random();

    GamePlayGround(int WinWidth, int WinHeight) {

        blockArray = new GameBlock[blockNumberY][blockNumberX];
        blockSize = Math.min(WinWidth / blockNumberX, WinHeight / blockNumberY);
        initStoneBlocks();
        generateBlocks(50); //set grass persantage
        spawnCoordinates = generateSpawnPoint();
        generateSpawnArea(spawnCoordinates.x, spawnCoordinates.y);
    }

    private void generateSpawnArea(int row, int column) {

        ///////////case Corners
        if (row == 1) { // top corners

            if (column == 1) { //left top
                blockArray[row][column + 1] = new GrassBrick((column + 1) * blockSize, row * blockSize, blockSize, blockSize);
                blockArray[row + 1][column] = new GrassBrick(column * blockSize, (row + 1) * blockSize, blockSize, blockSize);
                return;
            }

            if (column == 15) {//right top
                blockArray[row][column - 1] = new GrassBrick((column - 1) * blockSize, row * blockSize, blockSize, blockSize);
                blockArray[row + 1][column] = new GrassBrick(column * blockSize, (row + 1) * blockSize, blockSize, blockSize);
                return;
            }

        }

        if (row == 11) {//bottom corners

            if (column == 1) {//left bottom
                blockArray[row - 1][column] = new GrassBrick(column * blockSize, (row - 1) * blockSize, blockSize, blockSize);
                blockArray[row][column + 1] = new GrassBrick((column + 1) * blockSize, row * blockSize, blockSize, blockSize);
                return;
            }

            if (column == 15) {//right bottom
                blockArray[row - 1][column] = new GrassBrick(column * blockSize, (row - 1) * blockSize, blockSize, blockSize);
                blockArray[row][column - 1] = new GrassBrick((column - 1) * blockSize, row * blockSize, blockSize, blockSize);
                return;
            }

        }

        ///////////// case near stone block
        if (row == 1 || row == 11) {//horizontal
            if (column % 2 == 0) {
                blockArray[row][column - 1] = new GrassBrick((column - 1) * blockSize, row * blockSize, blockSize, blockSize);
                blockArray[row][column + 1] = new GrassBrick((column + 1) * blockSize, row * blockSize, blockSize, blockSize);
                return;
            }
        }

        if (column == 1 || column == 15) {//vertical
            if (row % 2 == 0) {
                blockArray[row - 1][column] = new GrassBrick(column * blockSize, (row - 1) * blockSize, blockSize, blockSize);
                blockArray[row + 1][column] = new GrassBrick(column * blockSize, (row + 1) * blockSize, blockSize, blockSize);
                return;
            }
        }

        /////////////// case odd cells (between stones)
        if (row == 1 || row == 11) {//horizontal
            if (column % 2 == 1) {

                if (MyRandom.randomPersantage(33)) {
                    blockArray[row][column - 1] = new GrassBrick((column - 1) * blockSize, row * blockSize, blockSize, blockSize);
                    blockArray[row][column + 1] = new GrassBrick((column + 1) * blockSize, row * blockSize, blockSize, blockSize);
                    return;
                } else {
                    //задаємо блок в центр поля
                    if (row == 1)
                        blockArray[row + 1][column] = new GrassBrick(column * blockSize, (row + 1) * blockSize, blockSize, blockSize);
                    else
                        blockArray[row - 1][column] = new GrassBrick(column * blockSize, (row - 1) * blockSize, blockSize, blockSize);

                    // визначаємо 2 блок
                    if (random.nextBoolean())
                        blockArray[row][column + 1] = new GrassBrick((column + 1) * blockSize, row * blockSize, blockSize, blockSize);
                    else
                        blockArray[row][column - 1] = new GrassBrick((column - 1) * blockSize, row * blockSize, blockSize, blockSize);
                    return;
                }

            }
        }

        if (column == 1 || column == 15) {//vertical
            if (row % 2 == 1) {
                if (MyRandom.randomPersantage(33)) {
                    blockArray[row - 1][column] = new GrassBrick(column * blockSize, (row - 1) * blockSize, blockSize, blockSize);
                    blockArray[row + 1][column] = new GrassBrick(column * blockSize, (row + 1) * blockSize, blockSize, blockSize);
                    return;
                }else {
                    //задаємо блок в центр поля
                    if (column == 1)
                        blockArray[row][column+1] = new GrassBrick((column+1) * blockSize, row * blockSize, blockSize, blockSize);
                    else
                        blockArray[row][column-1] = new GrassBrick((column-1) * blockSize, row * blockSize, blockSize, blockSize);

                    // визначаємо 2 блок
                    if (random.nextBoolean())
                        blockArray[row+1][column] = new GrassBrick(column * blockSize, (row+1) * blockSize, blockSize, blockSize);
                    else
                        blockArray[row-1][column] = new GrassBrick(column * blockSize, (row-1) * blockSize, blockSize, blockSize);
                    return;
                }
            }
        }


    }

    private Point generateSpawnPoint() {

        int row;
        int column;

        int line = random.nextInt(4);

        if (line % 2 == 0) { //vertical

            if (line == 0) row = 1;
            else row = 11;

            column = random.nextInt(15) + 1;

        } else { // horizontal

            if (line == 1) column = 1;
            else column = 15;

            row = random.nextInt(11) + 1;

        }

        blockArray[row][column] = new GrassBrick(column * blockSize, row * blockSize, blockSize, blockSize);
        blockArray[row][column].setFill(Color.BLACK); //spawn point
        return new Point(row, column);
    }

    //generates Grass/Brick blocks
    private void generateBlocks(int grassPersantage) {

        //рожева цегла непарні сплошні рядки
        for (int i = 1; i < blockNumberY - 1; i += 2) {
            for (int j = 1; j < blockNumberX - 1; j += 1) {
                blockArray[i][j] = blockArray[i][j] = randomBrickOrGrass(grassPersantage, i, j);
            }
        }

        //рожева цегла парні через один
        for (int i = 2; i < blockNumberY - 2; i += 2) {
            for (int j = 1; j < blockNumberX - 1; j += 2) {
                blockArray[i][j] = randomBrickOrGrass(grassPersantage, i, j);
            }
        }

    }

    //persantage of grass
    private GameBlock randomBrickOrGrass(int grassPersantage, int i, int j) {
        if (MyRandom.randomPersantage(grassPersantage))
            return new GrassBrick(blockSize * j, i * blockSize, blockSize, blockSize);

        return new RedBrick(blockSize * j, i * blockSize, blockSize, blockSize);
    }

    private void initStoneBlocks() {
        //задаємо бетонну рамку
        // верх і низ
        for (int i = 0; i < blockNumberX; i++) {
            blockArray[0][i] = new StoneBrick(blockSize * i, 0, blockSize, blockSize);
            blockArray[12][i] = new StoneBrick(blockSize * i, blockSize * 12, blockSize, blockSize);
        }

        //ліво і право
        for (int i = 1; i < blockNumberY - 1; i++) {
            blockArray[i][0] = new StoneBrick(0, i * blockSize, blockSize, blockSize);
            blockArray[i][16] = new StoneBrick(blockSize * 16, i * blockSize, blockSize, blockSize);
        }


        //сітка через один ряд бетон
        for (int i = 2; i < blockNumberY - 2; i += 2) {
            for (int j = 2; j < blockNumberX - 2; j += 2) {
                blockArray[i][j] = new StoneBrick(blockSize * j, i * blockSize, blockSize, blockSize);
            }
        }

    }

    public void drawGrid(ObservableList<Node> children) {

        for (int i = 0; i < blockNumberY; i++)
            for (int j = 0; j < blockNumberX; j++)
                children.add(getElementAt(i, j));

    }

    public Rectangle getElementAt(int row, int column) {
        return blockArray[row][column];
    }

}
