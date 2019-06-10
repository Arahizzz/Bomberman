package program;

public class Difficulty {

    int life;
    double mobSpeed;
    double playerSpeed;
    int averageMobNumber;
    int grassPersantage;
    double turnProbabilty;
    int bonusPersantage;
    public static final Difficulty VERAYEASY = new Difficulty(10,3,1,0.5,0.01,90,70);
    public static final Difficulty EASY = new Difficulty(5,2,2,0.7,0.05,70,50);
    public static final Difficulty NORMAL = new Difficulty(3,1.75,3,1.2,0.1,35,35);
    public static final Difficulty HARD = new Difficulty(2,1.5,5,1.75,0.3,70,20);
    public static final Difficulty INSANE = new Difficulty(1,1.2,7,2.5,0.5,90,7);
    public static Difficulty current;


    public Difficulty(int life, double playerSpeed, int averageMobNumber, double mobSpeed, double turnProbabilty, int grassPersantage, int bonusPersantage){
        this.life=life;
        this.playerSpeed=playerSpeed;
        this.averageMobNumber=averageMobNumber;
        this.mobSpeed=mobSpeed;
        this.grassPersantage=grassPersantage;
        this.bonusPersantage=bonusPersantage;
        this.turnProbabilty=turnProbabilty;
    }

    public double getMobSpeed() {
        return mobSpeed;
    }

    public double getPlayerSpeed() {
        return playerSpeed;
    }

    public void setMobSpeed(double mobSpeed) {
        this.mobSpeed = mobSpeed;
    }

    public void setPlayerSpeed(double playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public double getTurnProbabilty() {
        return turnProbabilty;
    }

    public void setTurnProbabilty(double turnProbabilty) {
        this.turnProbabilty = turnProbabilty;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getAverageMobNumber() {
        return averageMobNumber;
    }

    public void setAverageMobNumber(int averageMobNumber) {
        this.averageMobNumber = averageMobNumber;
    }

    public int getGrassPersantage() {
        return grassPersantage;
    }

    public void setGrassPersantage(int grassPersantage) {
        this.grassPersantage = grassPersantage;
    }

    public int getBonusPersantage() {
        return bonusPersantage;
    }

    public void setBonusPersantage(int bonusPersantage) {
        this.bonusPersantage = bonusPersantage;
    }

}
