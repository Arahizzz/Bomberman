package program;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GamePlayGround {
    private GameBlock[][] blockArray;

    private int blockSize;
    private int blockNumberY = 13;
    private int blockNumberX = 17;
    private GameBlock spawn;
    private Random random = new Random();
    private Player player;
    private int grassPercentage = 95;
    private int averageMobsNumber = 5; // приблизна кількість кількість мобів
    private int countGrassBlocks = 0;
    int blocksPerMob = 130 * grassPercentage / 100 / averageMobsNumber;
    int perBlockChance = 100 / blocksPerMob;
    int mobsCreated = 0;
    Difficulty difficulty;
    ArrayList<GrassBlock> mobBlocks = new ArrayList<GrassBlock>();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    ObservableList<Node> children;

    GamePlayGround(ObservableList<Node> children, double WinWidth, double WinHeight, Difficulty difficulty) {
        this.children=children;
        this.difficulty = difficulty;
        Difficulty.current = difficulty;
        RedBrick.bonusChance = difficulty.getBonusPersantage();
        grassPercentage = difficulty.getGrassPersantage();
        averageMobsNumber = difficulty.getAverageMobNumber();
        mobsCreated = 0;
        countGrassBlocks = 0;
        blocksPerMob = 130 * grassPercentage / 100 / averageMobsNumber;
        perBlockChance = 100 / blocksPerMob;

        blockArray = new GameBlock[blockNumberY][blockNumberX];
        blockSize = (int) Math.min(WinWidth / blockNumberX, WinHeight / blockNumberY);
        initStoneBlocks();
        generateBlocks(grassPercentage); //set grass persantage
        spawn = generateSpawnPoint();
        generateSpawnArea((int) spawn.getY() / blockSize, (int) spawn.getX() / blockSize);
        generateExit();
        initMobs();
        Enemy.updateMobs();
    }

    public void initMobs(GrassBlock spawn) {
        Enemy.getEnemies().clear();
        Platform.runLater(() -> {
            final Enemy enemy = new Enemy(spawn, blockArray, blockSize, children);
            Enemy.setSpeed(difficulty.getMobSpeed());
            Enemy.setTurnProbability(difficulty.getTurnProbabilty());
            children.add(enemy);
        });
    }

    public void initPlayer() {
        Creature.getCreatures().clear();
        player = new Player(spawn, blockArray, blockSize, children, 1);
        player.setLife(difficulty.getLife());
        player.setSpeed(difficulty.getPlayerSpeed());
        children.add(player);
    }

    private boolean checkMobNearPlayer(GameBlock mobSpawn) {
        int rangeCheck = 3; // кількість блоків вверх вниз вправо вліво від гравця без мобів (спавн не включно)

        //spawn

        if (mobSpawn == spawn) return false;

        //left
        for (int i = 1; i < rangeCheck + 1; i++) {

            if (!blockArray[spawn.getVerticalIndex()][spawn.getHorizontalIndex() - i].isWalkAllowed())
                break;
            if (blockArray[spawn.getVerticalIndex()][spawn.getHorizontalIndex() - i] == mobSpawn)
                return false;
        }

        //right
        for (int i = 1; i < rangeCheck + 1; i++) {
            if (!blockArray[spawn.getVerticalIndex()][spawn.getHorizontalIndex() + i].isWalkAllowed())
                break;
            if (blockArray[spawn.getVerticalIndex()][spawn.getHorizontalIndex() + i] == mobSpawn)
                return false;
        }

        //top
        for (int i = 1; i < rangeCheck + 1; i++) {
            if (!blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex()].isWalkAllowed())
                break;
            if (blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex()] == mobSpawn)
                return false;
        }

        //bottom
        for (int i = 1; i < rangeCheck + 1; i++) {
            if (!blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex()].isWalkAllowed())
                break;
            if (blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex()] == mobSpawn)
                return false;
        }

        ////////////////////////////DIAGONAL
        if (spawn.getHorizontalIndex() % 2 == 0 || spawn.getVerticalIndex() % 2 == 0) {

            if (spawn.getHorizontalIndex() == 1) {
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() - 1][spawn.getHorizontalIndex() + i].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() - 1][spawn.getHorizontalIndex() + i] == mobSpawn)
                        return false;
                }
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() + 1][spawn.getHorizontalIndex() + i].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() + 1][spawn.getHorizontalIndex() + i] == mobSpawn)
                        return false;
                }
            }

            if (spawn.getHorizontalIndex() == 15) {
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() - 1][spawn.getHorizontalIndex() - i].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() - 1][spawn.getHorizontalIndex() - i] == mobSpawn)
                        return false;
                }
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() + 1][spawn.getHorizontalIndex() - i].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() + 1][spawn.getHorizontalIndex() - i] == mobSpawn)
                        return false;
                }
            }

            if (spawn.getVerticalIndex() == 1) {
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex() - 1].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex() - 1] == mobSpawn)
                        return false;
                }
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex() + 1].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() + i][spawn.getHorizontalIndex() + 1] == mobSpawn)
                        return false;
                }
            }

            if (spawn.getVerticalIndex() == 11) {
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex() - 1].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex() - 1] == mobSpawn)
                        return false;
                }
                for (int i = 0; i < rangeCheck + 1; i++) {
                    if (!blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex() + 1].isWalkAllowed())
                        break;
                    if (blockArray[spawn.getVerticalIndex() - i][spawn.getHorizontalIndex() + 1] == mobSpawn)
                        return false;
                }
            }

        }


        return true;
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
                blockArray[i][j] = randomBrickOrGrass(grassPersantage, i, j);
            }
        }

        //рожева цегла парні через один
        for (int i = 2; i < blockNumberY - 2; i += 2) {
            for (int j = 1; j < blockNumberX - 1; j += 2) {
                blockArray[i][j] = randomBrickOrGrass(grassPersantage, i, j);
            }
        }

    }

    private void initMobs() {

        for (GameBlock[] gameBlock : blockArray) {
            for (GameBlock gameBlock2 : gameBlock) {


                if (gameBlock2.walkAllowed) {

                    countGrassBlocks++;

                    if (countGrassBlocks > mobsCreated * blocksPerMob) {

                        if (MyRandom.randomPersantage(perBlockChance) && checkMobNearPlayer(gameBlock2)) {
                            initMobs((GrassBlock) gameBlock2);//spawnMob//////////////////////////////////////////////////////////////
                            mobBlocks.add((GrassBlock) gameBlock2);
                            perBlockChance = 100 / blocksPerMob;
                            mobsCreated++;
                        } else
                            perBlockChance += 100 / blocksPerMob;

                    }
                }
            }
        }
    }

    //persantage of grass

    private GameBlock randomBrickOrGrass(int grassPersantage, int i, int j) {
        if (MyRandom.randomPersantage(grassPersantage)) {
            GrassBlock grassBlock = new GrassBlock(blockSize * j, i * blockSize, blockSize, blockSize, i, j);

            return grassBlock;
        }
        return new RedBrick(blockSize * j, i * blockSize, blockSize, blockSize, i, j);
    }

    private void generateExit() {
        int verticalIndex;
        int horizontalIndex;
        do {
            verticalIndex = random.nextInt(blockNumberY - 2) + 1;
            horizontalIndex = random.nextInt(blockNumberX - 2) + 1;
        } while (verticalIndex % 2 == 0 & horizontalIndex % 2 == 0 & verticalIndex != spawn.getVerticalIndex() & horizontalIndex != spawn.getHorizontalIndex());
        RedBrick brick = new RedBrick(blockSize * horizontalIndex, verticalIndex * blockSize, blockSize, blockSize, verticalIndex, horizontalIndex);
        blockArray[verticalIndex][horizontalIndex] = brick;
        brick.setExit(new Exit(brick, blockArray, blockSize, children));
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

}
