package program;

import javafx.scene.image.Image;

public class Animation {

    private static int currentPlayerFront = 0;
    private static int currentPlayerBack = 0;
    private static int currentSidePlayerRight = 0;
    private static int currentSidePlayerLeft = 0;
    private static Image[] playerAnimationFront = new Image[8];
    private static Image[] playerAnimationBack = new Image[8];
    private static Image[] playerAnimationLeft = new Image[8];
    private static Image[] playerAnimationRight = new Image[8];

    private static int currentEnemyFront = 0;
    private static int currentEnemyBack = 0;
    private static int currentSideEnemyRight = 0;
    private static int currentSideEnemyLeft = 0;
    private static Image[] enemyAnimationFront = new Image[6];
    private static Image[] enemyAnimationBack = new Image[6];
    private static Image[] enemyAnimationLeft = new Image[6];
    private static Image[] enemyAnimationRight = new Image[6];

    static {
        for (int i=0;i<playerAnimationFront.length;i++) {
            playerAnimationFront[i] = new Image("Bomberman\\Front\\Bman_F_f0" + i + ".png");
            playerAnimationBack[i] = new Image("Bomberman\\Back\\Bman_B_f0" + i + ".png");
            playerAnimationRight[i] = new Image("Bomberman\\Right\\Bman_F_f0" + i + ".png");
            playerAnimationLeft[i] = new Image("Bomberman\\Left\\Bman_F_f0" + i + ".png");
        }
        for (int i=0;i<enemyAnimationFront.length;i++) {
            enemyAnimationFront[i] = new Image("Enemy\\Front\\Creep_F_f0" + i + ".png");
            enemyAnimationBack[i] = new Image("Enemy\\Back\\Creep_B_f0" + i + ".png");
            enemyAnimationRight[i] = new Image("Enemy\\Right\\Creep_S_f0" + i + ".png");
            enemyAnimationLeft[i] = new Image("Enemy\\Left\\Creep_S_f0" + i + ".png");
        }
    }

    public static Image getPlayerAnimationBack(){
        Image image = playerAnimationBack[currentPlayerBack];
        currentPlayerBack++;
        if (currentPlayerBack ==8) currentPlayerBack =0;
        return image;
    }

    public static Image getPlayerAnimationRight(){
        Image image = playerAnimationRight[currentSidePlayerRight];
        currentSidePlayerRight++;
        if (currentSidePlayerRight ==8) currentSidePlayerRight =0;
        return image;
    }

    public static Image getPlayerAnimationFront(){
        Image image = playerAnimationFront[currentPlayerFront];
        currentPlayerFront++;
        if (currentPlayerFront ==8) currentPlayerFront =0;
        return image;
    }

    public static Image getPlayerAnimationLeft() {
        Image image = playerAnimationLeft[currentSidePlayerLeft];
        currentSidePlayerLeft++;
        if (currentSidePlayerLeft == 8) currentSidePlayerLeft = 0;
        return image;
    }

    public static Image getEnemyAnimationBack(){
        Image image = enemyAnimationBack[currentEnemyBack];
        currentEnemyBack++;
        if (currentEnemyBack ==8) currentEnemyBack =0;
        return image;
    }

    public static Image getEnemyAnimationRight(){
        Image image = enemyAnimationRight[currentSideEnemyRight];
        currentSideEnemyRight++;
        if (currentSideEnemyRight ==8) currentSideEnemyRight =0;
        return image;
    }

    public static Image getEnemyAnimationFront(){
        Image image = enemyAnimationFront[currentEnemyFront];
        currentEnemyFront++;
        if (currentEnemyFront ==8) currentEnemyFront =0;
        return image;
    }

    public static Image getEnemyAnimationLeft() {
        Image image = enemyAnimationLeft[currentSideEnemyLeft];
        currentSideEnemyLeft++;
        if (currentSideEnemyLeft == 8) currentSideEnemyLeft = 0;
        return image;
    }

}
