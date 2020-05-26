package comUniversal.lowLevel.Debuger;

import java.util.Random;

public class BitError {

    private Random random = new Random();
    private float probability = 0.f;

    public BitError(float probability){
        this.probability = probability;
    }

    public int get(int bit) {
        int result = bit;
        float number = (float) random.nextInt(1000000) / 1000000.f;
        if (number < probability)
            result = inverse(result);
        return result;
    }

    private int inverse(int data){
        int out = -1;
        if (data == 0) out = 1;
        else if (data == 1) out = 0;
        return out;
    }

}