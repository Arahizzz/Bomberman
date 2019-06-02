package program;

import javafx.scene.image.Image;

public class Animation {

    public static int currentFront = 0;
    public static int currentBack = 0;
    public static int currentSideRight = 0;
    public static int currentSideLeft = 0;
    public static Image[] playerAnimationFront = new Image[8];
    public static Image[] playerAnimationBack = new Image[8];
    public static Image[] playerAnimationLeft = new Image[8];
    public static Image[] playerAnimationRight = new Image[8];
    static {
        for (int i=0;i<playerAnimationFront.length;i++) {
            playerAnimationFront[i] = new Image("Bomberman\\Front\\Bman_F_f0" + i + ".png");
            playerAnimationBack[i] = new Image("Bomberman\\Back\\Bman_B_f0" + i + ".png");
            playerAnimationRight[i] = new Image("Bomberman\\Right\\Bman_F_f0" + i + ".png");
            playerAnimationLeft[i] = new Image("Bomberman\\Left\\Bman_F_f0" + i + ".png");
        }
    }

    public static Image getPlayerAnimationBack(){
        Image image = playerAnimationBack[currentBack];
        currentBack++;
        if (currentBack==8) currentBack=0;
        return image;
    }

    public static Image getPlayerAnimationRight(){
        Image image = playerAnimationRight[currentSideRight];
        currentSideRight++;
        if (currentSideRight==8) currentSideRight=0;
        return image;
    }

    public static Image getPlayerAnimationFront(){
        Image image = playerAnimationFront[currentFront];
        currentFront++;
        if (currentFront==8) currentFront=0;
        return image;
    }

    public static Image getPlayerAnimationLeft() {
        Image image = playerAnimationLeft[currentSideLeft];
        currentSideLeft++;
        if (currentSideLeft == 8) currentSideLeft = 0;
        return image;
    }

}
