package comUniversal.BitLevel.decoder;


import comUniversal.Core;
import comUniversal.util.Params;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;


public class KylymDecoder {
    //private BitError bitError = new BitError(0.05f);
    //private RandomGeneratorBit randomGeneratorBit = new RandomGeneratorBit();
    private final int windowLength = 294;
    private final float bitErrorRate = 0.1f;

    private Queue<Integer> data = new ArrayDeque<>();
    private int bufInput[] = new int[windowLength];
    WorkingThread workingThread = new WorkingThread();
    private boolean running = false;
    private int maxGroupValue;
    private int bitCounter;
    private boolean startReceive = false;
    private int speed = 0;
private boolean receive = false;

//    private final int[] N1 = {1,0,0,1,0,0};
//    private final int[] N2 = {1,0,1,1,1,0};
//    private final int[] N3 = {1,0,1,0,1,1};
//    private final int[] N4 = {1,1,1,0,1,0};
//    private final int[] N5 = {1,1,1,1,1,1};
//    private final int[] N6 = {1,1,0,1,0,1};
//    private final int[] N7 = {0,0,1,0,0,1};
//    private final int[] N8 = {0,1,0,0,1,0};
//    private final int[] N9 = {0,1,0,1,1,1};
//    private final int[] N0 = {0,1,1,1,0,1};



    private final int[] N1_rx = {1,0,0,1,0,0};
    private final int[] N2_rx = {1,0,1,1,1,0};
    private final int[] N3_rx = {1,0,1,0,1,0};
    private final int[] N4_rx = {1,1,1,0,1,0};
    private final int[] N5_rx = {1,1,1,1,1,0};
    private final int[] N6_rx = {1,1,0,1,0,0};
    private final int[] N7_rx = {0,0,1,0,0,0};
    private final int[] N8_rx = {0,1,0,0,1,0};
    private final int[] N9_rx = {0,1,0,1,1,0};
    private final int[] N0_rx = {0,1,1,1,0,0};
//
//    private final int[] N1 = {0,0,0,1,1,1};
//    private final int[] N2 = {0,0,1,0,1,1};
//    private final int[] N3 = {0,0,1,1,0,1};
//    private final int[] N4 = {0,1,0,0,1,1};
//    private final int[] N5 = {0,1,0,1,0,1};
//    private final int[] N6 = {0,1,1,0,0,1};
//    private final int[] N7 = {1,1,0,0,0,1};
//    private final int[] N8 = {1,0,0,0,1,1};
//    private final int[] N9 = {1,0,0,1,0,1};
//    private final int[] N0 = {1,0,1,0,0,1};


    private final int[] standardSequence = {
            -1, -1, -1, -1, -1, -1, // anything
            -1, -1, -1, -1, -1, -1, // anything
            -1, -1, -1, -1, -1, -1, // anything
            -1, -1, -1, -1, -1, -1, // anything
            -1, -1, -1, -1, -1, -1, // anything
             1, 0, 0, 0, 0, 1, // P
    };

    private int bitCount = 0;

    private boolean compareToStandardSequence(int[] bitData) {

        if (bitData.length == 0)
            return false;
        float countBitError = 0;
        float countBitTotal = 0;
        for (int i = 0; i < bitData.length; i++) {
            if (standardSequence[i % standardSequence.length] != -1) {
                countBitTotal++;
                if (standardSequence[i % standardSequence.length] != bitData[i])
                    countBitError++;
            }
        }

        float bitErrorRate = countBitError / countBitTotal;
        return bitErrorRate <= this.bitErrorRate;
    }

    private int toNumber(int[] number) {
        int result = 10; // '*'

        //int[] numberRecovery = recovery(number);
        number[5]=0;
        if(Arrays.equals(number, N0_rx)) {result = 0;}
        else if(Arrays.equals(number, N1_rx)) {result = 1;}
        else if(Arrays.equals(number, N2_rx)) {result = 2;}
        else if(Arrays.equals(number, N3_rx)) {result = 3;}
        else if(Arrays.equals(number, N4_rx)) {result = 4;}
        else if(Arrays.equals(number, N5_rx)) {result = 5;}
        else if(Arrays.equals(number, N6_rx)) {result = 6;}
        else if(Arrays.equals(number, N7_rx)) {result = 7;}
        else if(Arrays.equals(number, N8_rx)) {result = 8;}
        else if(Arrays.equals(number, N9_rx)) {result = 9;}


        return  result;
    }


    private int[] recovery(int[] data){
        int[] out = new int[data.length];
        out[0] = inversBit(data[0]);

        for(int i = 1; i < data.length; i++)
            out[i] = difCompare(out[i - 1], data[i]);

        out[out.length - 1] = 1;
        return out;
    }

    private int inversBit(int bit){
        if (bit == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int difCompare(int x, int y){
        if(x != y){
            return 1;
        } else {
            return 0;
        }
    }


    private int[] toGroup(int[] group) {
        int[] result = new int[5];
        int[] number = new int[6];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(group, 6 * i, number, 0, 6);
            result[i] = toNumber(number);
        }
        return result;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addData(int input) {
        data.add(input);
        //data.add(randomGeneratorBit.get());
        //data.add(bitError.get(input));
    }

    public KylymDecoder(int speed) {
        this.speed = speed;
        maxGroupValue = Integer.parseInt(Params.SETTINGS.getString("group_print", "40"));
        workingThread.start();
    }

    class WorkingThread extends Thread {
        @Override
        public void run() {
            while (running) {
                if (data.size() != 0) {
                    symbolForBit();
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void creatGroup(){
        int[] groupArray = new int[30];
        System.arraycopy(bufInput, 0, groupArray, 0, 30);
        int[] group = toGroup(groupArray);
        for (int number : group)
            Core.getCore().informationWindow.setTextMessage(number,Integer.toString(speed));
    }

    public void symbolForBit() {
        System.arraycopy(bufInput, 1, bufInput, 0, bufInput.length - 1);
        bufInput[bufInput.length - 1] = data.poll();
        //bitCount++;
        if (compareToStandardSequence(bufInput)) {
            //Core.getCore().informationWindow.setReceiveFlag(true,false,Integer.toString(speed));
                //System.out.println("Bit counter: "+bitCount);
           // bitCount=0;

            startReceive = true;
            bitCounter = 0;
            creatGroup();
        } else if (startReceive) {

            bitCounter++;
            if (bitCounter % 36 == 0)

                creatGroup();
            if (bitCounter == maxGroupValue * 36) {
                startReceive = false;

            }
        }
    }
}

