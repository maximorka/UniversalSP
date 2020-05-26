package comUniversal.lowLevel.Debuger;

import java.util.Random;

public class RandomGeneratorBit {
    private Random random = new Random();

    public int get(){
        return random.nextInt(2);
    }
}