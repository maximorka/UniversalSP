package comUniversal.BitLevel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GroupAdd {

    private List<RadiogramPercent> radiogramPercent = new ArrayList<>();
    public void addRadiogramPercentListener(RadiogramPercent listener){radiogramPercent.add(listener);}
    public void clearRadiogramPercentListener(){radiogramPercent.clear();}
    private void toRadiogramPercentListener(int percent) {
        if (!radiogramPercent.isEmpty())
            for (RadiogramPercent listener : radiogramPercent)
                listener.percent(percent);
    }


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
            {1, 1, 1, 1, 1, 1}, // * unknow
    };

    private Queue<Integer> bits = new ArrayDeque<Integer>();

    private int totalSize = 0;

    public void add(String string){

        // Creating start radiogram
        addToQueue('P');
        for(int i = 0; i < 5; i++){
            addToQueue('P');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
        }

        // Fuling queue
        int charCounter = 0;
        for(int i = 0; i < string.length(); i++) {
            if(charCounter % 5 == 0)
                addToQueue('P');
            addToQueue(string.charAt(i));
            charCounter++;
        }

        // Finishing last froup
        while(charCounter % 5 != 0) {
            addToQueue('*');
            charCounter++;
        }

        // Finishing radiogram
        for(int i = 0; i < 5; i++){
            addToQueue('P');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
            addToQueue('1');
        }
        addToQueue('P');

        // update size
        totalSize = bits.size();

    }

    private int oldPercent = 0;

    private void updatePercent(){
        if (totalSize != 0) {
            int currentPercent = (100 * bits.size()) / totalSize;
            int different = oldPercent - currentPercent;
            if (Math.abs(different) >= 1) {
                oldPercent = currentPercent;
                //System.out.println(currentPercent + " %");
                toRadiogramPercentListener(currentPercent);
            }
        }
    }

    public int getBit(){
        if(bits.size() == 0 || bits == null) {
            return -1;
        } else {
            updatePercent();
            return bits.poll();
        }
    }

    private void addToQueue(char digit){
        int row = 11;
        if (digit == '0') row = 0;
        else if (digit == '1') row = 1;
        else if (digit == '2') row = 2;
        else if (digit == '3') row = 3;
        else if (digit == '4') row = 4;
        else if (digit == '5') row = 5;
        else if (digit == '6') row = 6;
        else if (digit == '7') row = 7;
        else if (digit == '8') row = 8;
        else if (digit == '9') row = 9;
        else if (digit == 'P' || digit == 'p') row = 10;

        if(row == 11)
            digit = '*';

        for (int i = 0; i < 6; i++)
            bits.add(kodIrkyt[row][i]);

        //System.out.println(digit);
    }

//    public static void main(String[] args) {
//        GroupAdd groupAdd = new GroupAdd();
//        String string = "123456123456123456123456123456123456";
//    }
}
