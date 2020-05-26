package comUniversal;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class KylymDecoder {
    //private BitError bitError = new BitError(0.05f);
    //private RandomGeneratorBit randomGeneratorBit = new RandomGeneratorBit();
    private final int windowLength = 150;
    private final float bitErrorRate = 0.1f;

    private Queue <Integer> data = new ArrayDeque<>();
    private int bufInput[] = new int[windowLength];
    WorkingThread workingThread = new WorkingThread();
    private boolean running = false;

    private final int[] N0 = {0,1,1,1,0,1};
    private final int[] N1 = {1,0,0,1,0,0};
    private final int[] N2 = {1,0,1,1,1,0};
    private final int[] N3 = {1,0,1,0,1,1};
    private final int[] N4 = {1,1,1,0,1,0};
    private final int[] N5 = {1,1,1,1,1,1};
    private final int[] N6 = {1,1,0,1,0,1};
    private final int[] N7 = {0,0,1,0,0,1};
    private final int[] N8 = {0,1,0,0,1,0};
    private final int[] N9 = {0,1,0,1,1,1};
    private final int[] P = {1,0,0,0,0,1};

    private final int[] standardSequence = {
            -1,-1,-1,-1,-1,-1, // anything
            -1,-1,-1,-1,-1,-1, // anything
            -1,-1,-1,-1,-1,-1, // anything
            -1,-1,-1,-1,-1,-1, // anything
            -1,-1,-1,-1,-1,-1, // anything
            1,0,0,0,0,1, // P
    };

    private boolean compareToStandardSequence(int[] bitData){
        if(bitData.length == 0)
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
        //System.out.println(countBitError+" ," + countBitTotal);
        float bitErrorRate = countBitError / countBitTotal;
        return bitErrorRate <= this.bitErrorRate;
    }

    private int toNumber(int[] number){
        int result = 10; // '*'
        if(Arrays.equals(number, N0)) {result = 0;}
        else if(Arrays.equals(number, N1)) {result = 1;}
        else if(Arrays.equals(number, N2)) {result = 2;}
        else if(Arrays.equals(number, N3)) {result = 3;}
        else if(Arrays.equals(number, N4)) {result = 4;}
        else if(Arrays.equals(number, N5)) {result = 5;}
        else if(Arrays.equals(number, N6)) {result = 6;}
        else if(Arrays.equals(number, N7)) {result = 7;}
        else if(Arrays.equals(number, N8)) {result = 8;}
        else if(Arrays.equals(number, N9)) {result = 9;}
        return  result;
    }

    private int[] toGroup(int[] group) {
        int[] result = new int[5];
        int[] number = new int[6];
        for(int i = 0; i < 5; i++) {
            System.arraycopy(group, 6*i, number, 0, 6);
            result[i] = toNumber(number);
        }
        return result;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addData(int input){
        data.add(input);
        //data.add(randomGeneratorBit.get());
        //data.add(bitError.get(input));
    }
    public KylymDecoder(){
        workingThread.start();
    }

    class WorkingThread extends Thread{
        @Override
        public void run() {
            while (running){

                if(data.size() !=0){
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

    public void symbolForBit() {
        System.arraycopy(bufInput, 1, bufInput, 0, bufInput.length - 1);
        bufInput[bufInput.length - 1] = data.poll();
        if(compareToStandardSequence(bufInput)) {
            int[] groupArray = new int[30];
            System.arraycopy(bufInput, 0, groupArray, 0, 30);
            int[] group = toGroup(groupArray);
            for(int number: group)
                Core.getCore().informationWindow.setTextMessage(number);
        }
    }

    public static void main(String[] args) {
        String txt = new String("123 134 53453 ");
        String ne = new String();
        ne= txt.replaceAll("\\s","");
        System.out.println(ne);
    }
}