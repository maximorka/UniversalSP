package comUniversal;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class KylymDecoder {
    private Queue <Integer> data = new ArrayDeque<>();

    private int[] P = {0,1,1,1,1,0};
    private int[] N0 = {1,0,0,0,1,0};
    private int[] N1 = {0,1,1,0,1,1};
    private int[] N2 = {0,1,0,0,0,1};
    private int[] N3 = {0,1,0,1,0,0};
    private int[] N4 = {0,0,0,1,0,1};
    private int[] N5 = {0,0,0,0,0,0};
    private int[] N6 = {0,0,1,0,1,0};
    private int[] N7 = {1,1,0,1,1,0};
    private int[] N8 = {1,0,1,1,0,1};
    private int[] N9 = {1,0,1,0,0,0};

    private int bufInput[] = new int[78];
    private int index = 0;
    private int indexInput = 0;
    private int symbol =0;
    private int symbolNumber[] = new int[10];
    int countSymbol = 0;
    int countP[] = new int[2];
    Map<Integer, String> number = new HashMap<Integer, String>();
    WorkingThread workingThread = new WorkingThread();
    private boolean running = false;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addData(int input){
        data.add(input);
    }
    public KylymDecoder(){

        number.put(7,"1");
        number.put(11,"2");
        number.put(13,"3");
        number.put(19,"4");
        number.put(21,"5");
        number.put(25,"6");
        number.put(49,"7");
        number.put(35,"8");
        number.put(37,"9");
        number.put(41,"0");
        number.put(1,"P");

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

        for (int i = 0; i < 6; i++) {
            if (bufInput[(i)] == P[i]) {
                symbol++;
            }
        }
        countP[0] += (symbol == 6) ? 1 : 0;
        countP[1] += (symbol >= 5) ? 1 : 0;
        symbol = 0;

        for (int i = 0; i < 6; i++) {
            if (bufInput[i + 36] == P[i]) {
                symbol++;
            }
        }
        countP[0] += (symbol == 6) ? 1 : 0;
        countP[1] += (symbol >= 5) ? 1 : 0;
        symbol = 0;

        for (int i = 0; i < 6; i++) {
            if (bufInput[i + 72] == P[i]) {
                symbol++;
            }
        }

        countP[0] += (symbol == 6) ? 1 : 0;
        countP[1] += (symbol >= 5) ? 1 : 0;
        symbol = 0;



        if (countP[0] >= 2 || ((countP[1] == 3) && (countP[0]>1 ))) {
            System.out.println(countP[0]+", "+countP[1]);
            for (int j = 0; j < 5; j++) {
                int[] number = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

                for (int i = 6 * (j + 1); i < (6 * (j + 1) + 6); i++) {
                    if (bufInput[i] == N0[i - (6 * (j + 1))]) {
                        number[0]++;
                    }
                    if (bufInput[i] == N1[i - (6 * (j + 1))]) {
                        number[1]++;
                    }
                    if (bufInput[i] == N2[i - (6 * (j + 1))]) {
                        number[2]++;
                    }
                    if (bufInput[i] == N3[i - (6 * (j + 1))]) {
                        number[3]++;
                    }
                    if (bufInput[i] == N4[i - (6 * (j + 1))]) {
                        number[4]++;
                    }
                    if (bufInput[i] == N5[i - (6 * (j + 1))]) {
                        number[5]++;
                    }
                    if (bufInput[i] == N6[i - (6 * (j + 1))]) {
                        number[6]++;
                    }
                    if (bufInput[i] == N7[i - (6 * (j + 1))]) {
                        number[7]++;
                    }
                    if (bufInput[i] == N8[i - (6 * (j + 1))]) {
                        number[8]++;
                    }
                    if (bufInput[i] == N9[i - (6 * (j + 1))]) {
                        number[9]++;
                    }
                }
                int res = 10;
                for (int i = 0; i < 10; i++) {
                    if (number[i] == 6) {
                        res = i;
                        continue;
                    }
                }
                Core.getCore().informationWindow.setTextMessage(res);
            }
        }
        countP[0] = 0;
        countP[1] = 0;
    }



}
