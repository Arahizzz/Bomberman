package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GamePlayGround {
    private GameBlock[][] blockArray;

    private int blockSize;

    private int blockNumberY = 13;
    private int blockNumberX = 17;
    private GameBlock spawn;
    private Random random = new Random();
    private Player player;
    private int grassPersantage = 40;
    private int averageMobsNumber = 3; // приблизна кількість кількість мобів
    private int countGrassBlocks = 0;
    int blocksPerMob = 130 * grassPersantage / 100 / averageMobsNumber;
    int perBlockChance = 100/blocksPerMob;
    int mobsCreated =0;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    ObservableList<Node> children;

    GamePlayGround(ObservableList<Node> children, int WinWidth, int WinHeight) {
        this.children = children;
        blockArray = new GameBlock[blockNumberY][blockNumberX];
        blockSize = Math.min(WinWidth / blockNumberX, WinHeight / blockNumberY);
        initStoneBlocks();
        generateBlocks(grassPersantage); //set grass persantage
        spawn = generateSpawnPoint();
        generateSpawnArea((int) spawn.getY() / blockSize, (int) spawn.getX() / blockSize);
    }

    public void initMobs(GrassBlock spawn) {
        System.out.println("Mob");
        children.add(new Enemy(spawn,blockArray,blockSize,children));
    }

    public void initPlayer() {
        player = new Player(spawn, blockArray, blockSize, children);
        children.add(player);
    }

    private void generateSpawnArea(int row, int column) {

        ///////////case Corners
        if (row == 1) { // top corners

            if (column == 1) { //left top
                blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column);
                blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                return;
            }

            if (column == 15) {//right top
                blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column);
                blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                return;
            }

        }

        if (row == 11) {//bottom corners

            if (column == 1) {//left bottom
                blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);
                blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column + 1);
                return;
            }

            if (column == 15) {//right bottom
                blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);
                blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column - 1);
                return;
            }

        }

        ///////////// case near stone block
        if (row == 1 || row == 11) {//horizontal
            if (column % 2 == 0) {
                blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column - 1);
                blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column + 1);
                return;
            }
        }

        if (column == 1 || column == 15) {//vertical
            if (row % 2 == 0) {
                blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);
                blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                return;
            }
        }

        /////////////// case odd cells (between stones)
        if (row == 1 || row == 11) {//horizontal
            if (column % 2 == 1) {

                if (MyRandom.randomPersantage(33)) {
                    blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column - 1);
                    blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column + 1);
                    return;
                } else {
                    //задаємо блок в центр поля
                    if (row == 1)
                        blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                    else
                        blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);

                    // визначаємо 2 блок
                    if (random.nextBoolean())
                        blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column + 1);
                    else
                        blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column - 1);
                    return;
                }

            }
        }

        if (column == 1 || column == 15) {//vertical
            if (row % 2 == 1) {
                if (MyRandom.randomPersantage(33)) {
                    blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);
                    blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                    return;
                } else {
                    //задаємо блок в центр поля
                    if (column == 1)
                        blockArray[row][column + 1] = new GrassBlock((column + 1) * blockSize, row * blockSize, blockSize, blockSize, row, column + 1);
                    else
                        blockArray[row][column - 1] = new GrassBlock((column - 1) * blockSize, row * blockSize, blockSize, blockSize, row, column - 1);

                    // визначаємо 2 блок
                    if (random.nextBoolean())
                        blockArray[row + 1][column] = new GrassBlock(column * blockSize, (row + 1) * blockSize, blockSize, blockSize, row + 1, column);
                    else
                        blockArray[row - 1][column] = new GrassBlock(column * blockSize, (row - 1) * blockSize, blockSize, blockSize, row - 1, column);
                    return;
                }
            }
        }


    }

    private GameBlock generateSpawnPoint() {

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

        blockArray[row][column] = new GrassBlock(column * blockSize, row * blockSize, blockSize, blockSize, row, column);
        //spawn point
        return blockArray[row][column];
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
        if (MyRandom.randomPersantage(grassPersantage)) {
            GrassBlock grassBlock = new GrassBlock(blockSize * j, i * blockSize, blockSize, blockSize, i, j);
            countGrassBlocks++;

            if (countGrassBlocks > mobsCreated*blocksPerMob) {

                if (MyRandom.randomPersantage(perBlockChance)) {
                    initMobs(grassBlock);//spawnMob//////////////////////////////////////////////////////////////
                    perBlockChance = 100/blocksPerMob;
                    mobsCreated++;
                }else
                    perBlockChance+=100/blocksPerMob;

            }
            return grassBlock;
        }
        return new RedBrick(blockSize * j, i * blockSize, blockSize, blockSize, i, j);
    }

    private void initStoneBlocks() {
        //задаємо бетонну рамку
        // верх і низ
        for (int i = 0; i < blockNumberX; i++) {
            blockArray[0][i] = new StoneBrick(blockSize * i, 0, blockSize, blockSize, 0, i);
            blockArray[12][i] = new StoneBrick(blockSize * i, blockSize * 12, blockSize, blockSize, 12, i);
        }

        //ліво і право
        for (int i = 1; i < blockNumberY - 1; i++) {
            blockArray[i][0] = new StoneBrick(0, i * blockSize, blockSize, blockSize, i, 0);
            blockArray[i][16] = new StoneBrick(blockSize * 16, i * blockSize, blockSize, blockSize, i, 16);
        }


        //сітка через один ряд бетон
        for (int i = 2; i < blockNumberY - 2; i += 2) {
            for (int j = 2; j < blockNumberX - 2; j += 2) {
                blockArray[i][j] = new StoneBrick(blockSize * j, i * blockSize, blockSize, blockSize, i, j);
            }
        }

    }

    public void drawGrid() {

        for (int i = 0; i < blockNumberY; i++)
            for (int j = 0; j < blockNumberX; j++)
                children.add(getElementAt(i, j));

    }

    public Rectangle getElementAt(int row, int column) {
        return blockArray[row][column];
    }

    public void putBomb() {
        Bomb bomb = player.putBomb();
        if (bomb != null) {
            children.add(bomb);
            bomb.start(children);
        }
    }

}
