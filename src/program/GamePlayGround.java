package program;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GamePlayGround {
    GameBlock[][] rectangleArray;
    int blockSize;
    int blockNumberY=13;
    int blockNumberX=17;
    GamePlayGround(int WinWidth,int WinHeight){

            rectangleArray = new GameBlock[blockNumberY][blockNumberX];
            blockSize = Math.min(WinWidth/blockNumberX,WinHeight/blockNumberY);
            initStoneBlocks();
            generateBlocks(20); //set grass persantage
    }

    //generates Grass/Brick blocks
    private void generateBlocks(int grassPersantage){

        //рожева цегла непарні сплошні рядки
        for (int i=1; i<blockNumberY-1; i+=2){
            for (int j=1; j<blockNumberX-1; j+=1) {
                rectangleArray[i][j] =    rectangleArray[i][j] = randomBrickOrGrass(grassPersantage,i,j);
            }
        }

        //рожева цегла парні через один
        for (int i=2; i<blockNumberY-2; i+=2){
            for (int j=1; j<blockNumberX-1; j+=2) {
                    rectangleArray[i][j] = randomBrickOrGrass(grassPersantage,i,j);
            }
        }

    }
//persantage of grass
    private GameBlock randomBrickOrGrass(int grassPersantage, int i,int j){
        if (MyRandom.randomPersantage(grassPersantage))
           return new GrassBrick(blockSize*j, i * blockSize, blockSize, blockSize);

           return new RedBrick(blockSize*j, i * blockSize, blockSize, blockSize);
    }

    private void initStoneBlocks(){
        //задаємо бетонну рамку
        // верх і низ
        for (int i=0; i<blockNumberX; i++){
            rectangleArray[0][i]=new StoneBrick(blockSize*i,0,blockSize,blockSize);
            rectangleArray[12][i]=new StoneBrick(blockSize*i,blockSize*12,blockSize,blockSize);
        }

        //ліво і право
        for (int i=1; i<blockNumberY-1; i++){
            rectangleArray[i][0]=new StoneBrick(0,i*blockSize,blockSize,blockSize);
            rectangleArray[i][16]=new StoneBrick(blockSize*16,i*blockSize,blockSize,blockSize);
        }


        //сітка через один ряд бетон
        for (int i=2; i<blockNumberY-2; i+=2){
            for (int j=2; j<blockNumberX-2; j+=2) {
                rectangleArray[i][j] = new StoneBrick(blockSize*j, i * blockSize, blockSize, blockSize);
            }
        }

    }

    public void drawGrid(ObservableList<Node> children){

        for (int i =0; i<blockNumberY;i++)
            for (int j=0;j<blockNumberX;j++)
        children.add(getElementAt(i,j));

    }

    public Rectangle getElementAt(int row, int column){
        return rectangleArray[row][column];
    }

}
