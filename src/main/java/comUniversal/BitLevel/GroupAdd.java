package comUniversal.BitLevel;

import comUniversal.util.Complex;

import java.util.ArrayDeque;
import java.util.Queue;

public class GroupAdd {

    private int[][] kodIrkyt = {
            {1, 0, 1, 0, 0, 1}, // 0
            {0, 0, 0, 1, 1, 1}, // 1
            {0, 0, 1, 0, 1, 1}, // 2
            {0, 0, 1, 1, 0, 1}, // 3
            {0, 1, 0, 0, 1, 1}, // 4
            {0, 1, 0, 1, 0, 1}, // 5
            {0, 1, 1, 0, 0, 1}, // 6
            {1, 1, 0, 0, 0, 1}, // 7
            {1, 0, 0, 0, 1, 1}, // 8
            {1, 0, 0, 1, 0, 1}, // 9
            {0, 0, 0, 0, 0, 1}, // P
    };

    private Queue<Integer> bits = new ArrayDeque<Integer>();

    public void add(String string){

        for(int i = 0; i < string.length(); i++){
            char digit = string.charAt(i);
            int index = -1;
            if (digit == '0') index = 0;
            else if (digit == '1') index = 1;
            else if (digit == '2') index = 2;
            else if (digit == '3') index = 3;
            else if (digit == '4') index = 4;
            else if (digit == '5') index = 5;
            else if (digit == '6') index = 6;
            else if (digit == '7') index = 7;
            else if (digit == '8') index = 8;
            else if (digit == '9') index = 9;
            else if (digit == 'P') index = 10;
            else if (digit == 'p') index = 10;
            else{
                System.out.println("It is non correct symbol");
            }

            if(index != -1) {
                for (int j = 0; j < 6; j++) {
                    bits.add(kodIrkyt[index][j]);
                }
            }
        }
    }

    public int getBit(){
        if(bits.size() == 0) return 0;
        return bits.poll();
    }

}
