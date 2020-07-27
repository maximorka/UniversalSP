package comUniversal.BitLevel.decoder;

import comUniversal.util.Params;
import org.apache.commons.math3.complex.Complex;

import java.util.*;
import java.util.Arrays;

public class KylymDecoder {

    private final int numR = 10; // кількість розділів аналізу
    private final float frameTreshold = 0.9f; // поріг достовірності

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
    private final int[] R = {1,0,0,0,0,1};

    private int difBitArray[] = new int[numR * 36];
    private Complex semplArray[] = new Complex[numR * 36];
    private boolean reversFlag = false;


    private boolean running = false;
    private int maxGroupValue;
    private int bitCounter;
    private boolean startReceive = false;
    private int softDecoding = 0;
    private int showRecovery = 0;


    private int speed;
    private int alroritm = 0;
    private int oldAlroritm = 0;

    private MessageListener messageListener;
    private StartRadiogram startRadiogramListener;
    public void addStartRadiogramListener(StartRadiogram listener) {
        this.startRadiogramListener = listener;
    }

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

    private final Complex[] N0_constellation = {
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N1_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N2_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N3_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N4_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N5_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N6_constellation = {
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N7_constellation = {
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N8_constellation = {
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(+1.f, 0.f),
    };
    private final Complex[] N9_constellation = {
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
            new Complex(-1.f, 0.f),
            new Complex(+1.f, 0.f),
    };

    private boolean isFrameFind() {

        if (difBitArray.length == 0)
            return false;

        float countBitError = 0.f;

        for (int i = 0; i < numR; i++) {
            for (int j = 0; j < R.length; j++) {
                if (difBitArray[36 * i + j + 30] != R[j]) {
                    countBitError++;
                }
            }
        }

        float treshold =  1.f - (countBitError / ((float) numR * (float) R.length));

        return treshold >= this.frameTreshold;
    }

    private String toSymbol(int[] bits, Complex[] sempls) {
        String symbol = "";

        if(Arrays.equals(bits, N0)) {symbol = "0";}
        else if(Arrays.equals(bits, N1)) {symbol = "1";}
        else if(Arrays.equals(bits, N2)) {symbol = "2";}
        else if(Arrays.equals(bits, N3)) {symbol = "3";}
        else if(Arrays.equals(bits, N4)) {symbol = "4";}
        else if(Arrays.equals(bits, N5)) {symbol = "5";}
        else if(Arrays.equals(bits, N6)) {symbol = "6";}
        else if(Arrays.equals(bits, N7)) {symbol = "7";}
        else if(Arrays.equals(bits, N8)) {symbol = "8";}
        else if(Arrays.equals(bits, N9)) {symbol = "9";}
        else { symbol = "*"; };

        if ( symbol == "*") {
            if (softDecoding == 1) {
                symbol = recovery(sempls);
            }
        }
//        String hardChar = (resultHard == 10)? "*" : String.valueOf(resultHard);
//        String softChar = (resultSoft == 10)? "*" : String.valueOf(resultSoft);;
//
//        System.out.print("|   " + hardChar + "  |   " + softChar + "  | ");
//        for (int i = 0; i < 11; i++) {
//            System.out.format("%.3f", this.level[i]); System.out.print(" | ");
//        }
//        System.out.println(" ");
//
//        counter++;
//        if(counter == 5){
//            System.out.println("|------+------+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------|");
//            System.out.println("| Hard | Soft |   0   |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |min dis|");
//            System.out.println("|------+------+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------|");
//            counter = 0;
//        }



        return symbol;
    }

    private int counter = 0;



    private float[] level;

    private String recovery(Complex[] sempls){
        String symbol = "";

        float[] compare = new float[10+1];
        compare[0] = softCompare(sempls, N0_constellation);
        compare[1] = softCompare(sempls, N1_constellation);
        compare[2] = softCompare(sempls, N2_constellation);
        compare[3] = softCompare(sempls, N3_constellation);
        compare[4] = softCompare(sempls, N4_constellation);
        compare[5] = softCompare(sempls, N5_constellation);
        compare[6] = softCompare(sempls, N6_constellation);
        compare[7] = softCompare(sempls, N7_constellation);
        compare[8] = softCompare(sempls, N8_constellation);
        compare[9] = softCompare(sempls, N9_constellation);

//        // normalization
//        float sum = 0.f;
//        for (int i = 0; i < compare.length; i++)
//            sum += compare[i];
//        for (int i = 0; i < compare.length; i++)
//            compare[i] /= sum;

        // find min
        float min = compare[0];
        for (int i = 0; i < 10; i++){
            if(compare[i] <= min){
                min = compare[i];
                if(showRecovery == 1) {
                    symbol = "\u0332"; // _
                    symbol += String.valueOf(i);
                } else {
                    symbol = String.valueOf(i);
                }

            }
        }



        float[] test = new float[10];
        for (int i = 0; i < test.length ; i++)
            test[i] = compare[i];
        Arrays.sort(test);
        compare[10] = test[1] - test[0];
        if(compare[10] < 0.9f)
            symbol = "*";

        this.level = compare;

        // find second min
//        compare[result] = 100000.f;
//
//        float minSecond = compare[0];
//        int resultSecond = 0;
//        for (int i = 0; i < 10; i++){
//            if(compare[i] < minSecond){
//                minSecond = compare[i];
//                resultSecond = i;
//            }
//        }
//
//        compare[result] = min;


//        if(minSecond - min < 2.f) {
//            result = 10;
//        }
//
//        if(min > 5.f) {
//            result = 10;
//        }

//        float avg = 0;
//        for (int i = 0; i < 10 ; i++)
//            avg += compare[i];
//
//        compare[10] = avg / 10.f;
//
//        compare[11] = compare[10] - min;

//        System.out.println("Symbol recovery = " + symbol);

        return symbol;
    }

    private float softCompare(Complex[] sempls, Complex[] constellation){

        float k = 1.f;
        if(reversFlag)
            k = -1.f;

        float result = 0;
        float re, im;
        for(int i = 0; i < 5; i++){ // only 5 bits !!!
            re = k * (float)constellation[i].getReal() - (float)sempls[i].getReal();
            im = (float)constellation[i].getImaginary() - (float)sempls[i].getImaginary();
            result += Math.sqrt(re*re+im*im);
        }

        return result;
    }

    private void reversCheck(){

        int counterOne = 0;
        int counterZero = 0;
        int bit;
        for (int k = 5; k < difBitArray.length;) {
            bit = (semplArray[k].getReal() > 0.f)? 1 : 0;
            if(bit == 1){counterOne++;}
            if(bit == 0){counterZero++;}
            k += 6;
        }

        this.reversFlag = counterZero > counterOne;
    }


    private String[] toGroup(int[] groupBits, Complex[] groupSamples) {
        String[] group = new String[5];
        for (int i = 0; i < group.length; i++) {
            int[] symbolBits = new int[6];
            System.arraycopy(groupBits, 6 * i, symbolBits, 0, 6);
            Complex[] symbolSamples = new Complex[6];
            System.arraycopy(groupSamples, 6 * i, symbolSamples, 0, 6);
            group[i] = toSymbol(symbolBits, symbolSamples);
        }
        return group;
    }

    public void addData(int difBit, Complex sempl) {


        System.arraycopy(difBitArray, 1, difBitArray, 0, difBitArray.length - 1);
        difBitArray[difBitArray.length - 1] = difBit;
        System.arraycopy(semplArray, 1, semplArray, 0, semplArray.length - 1);
        semplArray[semplArray.length - 1] = sempl;

        if (isFrameFind()) {



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

    public KylymDecoder(int speed) {
        for(int i = 0; i < semplArray.length; i++) {
            semplArray[i] = new Complex(0.f, 0.f);
        }

        this.speed = speed;
        maxGroupValue = Integer.parseInt(Params.SETTINGS.getString("group_print", "40"));
        softDecoding = Integer.parseInt(Params.SETTINGS.getString("soft_decoding", "0"));
        showRecovery = Integer.parseInt(Params.SETTINGS.getString("show_recovery", "0"));
    }



    private void creatGroup(){
        reversCheck();

        if((oldAlroritm == 0) && (alroritm == 1)){
            if(startRadiogramListener != null) {
                startRadiogramListener.start();
            }
        }

        int[] groupBits = new int[30];
        Complex[] groupSampls = new Complex[30];
        System.arraycopy(difBitArray, 0, groupBits, 0, 30);
        System.arraycopy(semplArray, 0, groupSampls, 0, 30);
        String[] group = toGroup(groupBits, groupSampls);

//        int counterError = 0;
//        int counterRecovery = 0;
//        for (int i = 0; i < result.length; i++) {
//            if((result[i] == 10) || (result[i] == 110)){
//                counterError++;
//            }
//            if((result[i] > 10) && (result[i] != 110)){
//                counterRecovery++;
//            }
//        }
//
//        if(counterError >= 3){
//            for (int i = 0; i < result.length; i++) {
//                result[i] = 10;
//            }
//        }
//
//        if(counterError == 2 & counterRecovery >= 2){
//            for (int i = 0; i < result.length; i++) {
//                result[i] = 10;
//            }
//        }
//
//        if(counterError == 1 & counterRecovery >= 4){
//            for (int i = 0; i < result.length; i++) {
//                result[i] = 10;
//            }
//        }

        String message = "";

        int countError = 0;

        for (int i = 0; i < group.length ; i++) {
            message += group[i];
            if(group[i] == "*") {
                countError++;
            }
        }

        if(countError >= 3){
            message = "*****";
        }

        message += " ";

        if (messageListener != null) {
//            for (String symbol : group) {
                messageListener.setSymbol(message);
//            }
        }

//        if (messageListener != null)
//            messageListener.setSymbol(result);
    }


}
