package program;

import java.util.Random;

public class RandomBoolean {
    private static Random random = new Random();

    public static boolean get(double probability) {
        return random.nextDouble() < probability;
    }
}
