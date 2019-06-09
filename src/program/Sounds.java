package program;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sounds {
    private static MediaPlayer explosion = new MediaPlayer(new Media(new File("Sounds\\Fire.wav").toURI().toString()));
    private static MediaPlayer tick = new MediaPlayer(new Media(new File("Sounds\\BombTick.wav").toURI().toString()));
    private static MediaPlayer bonus = new MediaPlayer(new Media(new File("Sounds\\Bonus.wav").toURI().toString()));
    private static MediaPlayer hit = new MediaPlayer(new Media(new File("Sounds\\GetHit.wav").toURI().toString()));
    private static MediaPlayer lost = new MediaPlayer(new Media(new File("Sounds\\GameLost.wav").toURI().toString()));
    private static MediaPlayer victory = new MediaPlayer(new Media(new File("Sounds\\Vıctory_Normal_Convert.wav").toURI().toString()));

    public static void playTick() {
        tick.seek(Duration.ZERO);
        tick.play();
    }

    public static void playExplosion() {
        explosion.seek(Duration.ZERO);
        explosion.play();
    }

    public static void playBonus() {
        bonus.seek(Duration.ZERO);
        bonus.play();
    }

    public static void playHit() {
        hit.seek(Duration.ZERO);
        hit.play();
    }

    public static void playLost() {
        lost.seek(Duration.ZERO);
        lost.play();
    }

    public static void playVictory() {
        victory.seek(Duration.ZERO);
        victory.play();
    }

    public static void init() {
    }
}
