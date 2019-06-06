package program;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class Characteristics extends GridPane {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;


    public Characteristics(Player player){
        Image imageHeart = new Image("Powerups\\Heart.png");
        Label heartL = new Label();
        heartL.textProperty().bind(player.lifeProperty());

        Image imageFlame= new Image("Powerups\\FlamePowerup.png");
        Label flameL = new Label();
        flameL.textProperty().bind(player.rangeProperty());

        Image imageBomb = new Image("Powerups\\BombPowerup.png");
        Label bombL = new Label();
        bombL.textProperty().bind(player.maxCountProperty());

        Image imageSpeed = new Image("Powerups\\SpeedPowerup.png");
        Label speedL = new Label();
        speedL.textProperty().bind(player.speedProperty());

        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getColumnConstraints().add(new ColumnConstraints(70));
        this.getRowConstraints().add(new RowConstraints(70));
        this.getRowConstraints().add(new RowConstraints(70));
        this.getRowConstraints().add(new RowConstraints(70));
        this.getRowConstraints().add(new RowConstraints(70));


        this.add(heartL,1,0);
        this.add(new ImageView(imageHeart),0,0);

        this.add(flameL,1,1);
        this.add(new ImageView(imageFlame),0,1);

        this.add(bombL,1,2);
        this.add(new ImageView(imageBomb),0,2);

        this.add(speedL,1,3);
        this.add(new ImageView(imageSpeed),0,3);

    }

}
