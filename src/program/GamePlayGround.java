package program;

import javafx.scene.shape.Rectangle;

public class GamePlayGround {
    Rectangle[][] rectangleArray;
    int blockSize;
    GamePlayGround(int WinWidth,int WinHeight){

            rectangleArray = new Rectangle[17][13];
            blockSize = Math.min(WinWidth/17,WinHeight/13);
            for (int i=0; i<17; i++){
                rectangleArray[0][i]=new StoneBrick(blockSize*i,0,blockSize,blockSize);
                rectangleArray[16][i]=new StoneBrick(blockSize*i,0,blockSize,blockSize);
            }
            for (int i=1; i<16; i++){
            rectangleArray[i][0]=new StoneBrick(0,i*blockSize,blockSize,blockSize);
            rectangleArray[i][12]=new StoneBrick(12*blockSize,0,i*blockSize,blockSize);
            }
    }

    private void draw

}
