package comUniversal.BitLevel.decoder;

import comUniversal.lowLevel.Debuger.Debuger;
import org.apache.commons.math3.complex.Complex;

public class FramePositionFinder {

    private Debuger debuger;
    private boolean status = false;

    private float level = 30.f;
    private int trasholdCrashCounter = 10;
    private int lengthFrame;
    private int crashCounter;
    private int[] mask;

    private int framePosition = -1;

    public FramePositionFinder(int lengthFrame, int[] mask, Debuger debuger){
        this.lengthFrame = lengthFrame;
        this.mask = mask;
        this.debuger = debuger;
    }

    public int getPosition(){
        return this.framePosition;
    }

    private void setPosition(int source){
        this.framePosition = source;
    }

    private int toNrz(int bit){
        return (bit == 0)? -1:1;
    }

    public boolean isFind(int[] bitArray){

        int numberOfFrames = bitArray.length / (lengthFrame + mask.length);

        if(numberOfFrames == 0) {
            framePosition = -1;
            return false;
        }

        float[] accum = new float[lengthFrame + mask.length];

        for (int i = 0; i < accum.length; i++) {
            for (int j = 0; j < numberOfFrames; j++)
                accum[i] += toNrz(bitArray[(lengthFrame + mask.length) * j + i]);
            accum[i] /= numberOfFrames;
        }

        float[] distance = new float[lengthFrame + 1];

        for (int i = 0; i < distance.length; i++){
            for(int j = 0; j < mask.length; j++)
                distance[i] += Math.abs(accum[j + i] - toNrz(mask[j]));
            distance[i] /= mask.length;
            distance[i] *= 50.f;
            //debuger.show(new Complex(distance[i], 0));
        }


        float minFirst = distance[0];
        int position = 0;
        for (int i = 0; i < distance.length; i++){
            if(distance[i] < minFirst) {
                minFirst = distance[i];
                position = i;
            }
        }

        distance[position] = 100.f;
        float minSecond = distance[0];
        for (int i = 0; i < distance.length; i++){
            if(distance[i] < minSecond)
                minSecond = distance[i];
        }
        distance[position] = minFirst;

        float difference = minSecond - minFirst;

        if(status) {

            if ( (minFirst >= level) || (difference < 5.f) ) {
                crashCounter++;
            } else {
                setPosition(position);
                crashCounter = 0;
            }

        } else {

            if ((minFirst < level) && (difference >= 5.f)){
                status = true;
                setPosition(position);
                crashCounter = 0;
            }

        }

        if(crashCounter >= trasholdCrashCounter){
            crashCounter = 0;
            status = false;
            setPosition(-1);
        }

//         Debug
//        System.out.print("Position = " + getPosition() + " Min = " + String.format("%.0f", minFirst) + " Min2 = " + String.format("%.0f", minSecond) + " crashCounter = " + crashCounter + "\t");
//        for(int i = 0; i < distance.length; i++) {
//            if(i == position) System.out.print("|");
//            System.out.print(String.format("%.0f", distance[i] / 10.f));
//            if(i == position) System.out.print("|");
//            debuger.show(new Complex(distance[i], 0));
//        }
//        System.out.println("");
//         Debug

        return status;
    }

}
