package program;

import java.util.Random;

public class MyRandom {

   private static Random random = new Random();

    public static boolean randomPersantage(int truePersantage) {

        if (truePersantage>100) truePersantage=100;
        else if (truePersantage<0) truePersantage=0;

        return random.nextInt(101) <= truePersantage;

    }
}

