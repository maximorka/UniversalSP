package comUniversal.BitLevel.decoder;

import comUniversal.util.Params;

import java.util.*;


public class KylymDecoder {

    private final int windowLength = 294;
    private final float bitErrorRate = 0.1f;

    private Queue<Integer> data = new ArrayDeque<>();
    private int bufInput[] = new int[windowLength+150];

    private boolean running = false;
    private int maxGroupValue;
    private int bitCounter;
    private boolean startReceive = false;

    private int speed;
    private int alroritm = 0;
    private int oldAlroritm = 0;

    private MessageListener messageListener;
    public void addMessageListener(MessageListener listener){
        this.messageListener = listener;
    }


    // Listeners DdcFrequency
    private List<AlgoritmListener> algoritmListeners= new ArrayList<>();

    public void addAlgoritmListener(AlgoritmListener listener){algoritmListeners.add(listener);}

    public void clearAlgoritmListener(){algoritmListeners.clear();}

    private void toListenersAlgoritm(int algorit, int speed){
        if(!algoritmListeners.isEmpty())
            for(AlgoritmListener listener: algoritmListeners)
                listener.algoritm(algorit, speed);
    }
    private final int[] N1 = {1,0,0,1,0,0};
    private final int[] N2 = {1,0,1,1,1,0};
    private final int[] N3 = {1,0,1,0,1,0};
    private final int[] N4 = {1,1,1,0,1,0};
    private final int[] N5 = {1,1,1,1,1,0};
    private final int[] N6 = {1,1,0,1,0,0};
    private final int[] N7 = {0,0,1,0,0,0};
    private final int[] N8 = {0,1,0,0,1,0};
    private final int[] N9 = {0,1,0,1,1,0};
    private final int[] N0 = {0,1,1,1,0,0};

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

    private boolean compareToStandardSequence(int[] bitData) {
        if (bitData.length == 0)
            return false;
        float countBitError = 0;
        float countBitTotal = 0;
        for (int i = 150; i < bitData.length; i++) {
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

        number[5]=0;
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
        for (int i = 0; i < 5; i++) {
            System.arraycopy(group, 6 * i, number, 0, 6);
            result[i] = toNumber(number);
        }
        return result;
    }

    public void addData(int input) {
        //data.add(input);
        symbolForBit(input);
        //data.add(randomGeneratorBit.get());
        //data.add(bitError.get(input));
    }

    public KylymDecoder(int speed) {
        this.speed = speed;
        maxGroupValue = Integer.parseInt(Params.SETTINGS.getString("group_print", "40"));

    }



    private void creatGroup(){
        int[] groupArray = new int[30];
        System.arraycopy(bufInput, 0, groupArray, 0, 30);
        int[] group = toGroup(groupArray);
        for (int number : group) {
            if (messageListener != null)
                messageListener.setSymbol(number);
        }
            //Core.getCore().informationWindow.setTextMessage(number);
    }

    public void symbolForBit(int input) {
        System.arraycopy(bufInput, 1, bufInput, 0, bufInput.length - 1);
        bufInput[bufInput.length - 1] = input;

        if (compareToStandardSequence(bufInput)) {
            alroritm = 1;
            startReceive = true;
            bitCounter = 0;
            creatGroup();
        } else if (startReceive) {
            bitCounter++;
            if (bitCounter % 36 == 0) {
                alroritm = 2;
                creatGroup();
            }
            if (bitCounter == maxGroupValue * 36) {
                startReceive = false;
                alroritm = 0;
            }
        }
        if(oldAlroritm != alroritm) {
            oldAlroritm = alroritm;
            toListenersAlgoritm(alroritm, speed);
        }
    }
}
