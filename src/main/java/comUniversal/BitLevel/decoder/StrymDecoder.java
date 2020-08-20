package comUniversal.BitLevel.decoder;

import org.apache.commons.math3.complex.Complex;

public class StrymDecoder {

    private final int[] sinchro = {0,1,1,1,1,1,1,1,1,1,1,0};
    private final int numFrame = 10; // кількість кадрів
    private final int lengthFrame = 120; // довжина кадру
    private final float frameTreshold = 0.80f; // поріг достовірності

    private int difBitArray[] = new int[numFrame * (lengthFrame + sinchro.length)];

    private String dataString = "";


    private boolean isFrameFind() {

        if (difBitArray.length == 0)
            return false;

        float countBitError = 0.f;

        for (int i = 0; i < numFrame; i++) {
            for (int j = 0; j < sinchro.length; j++) {
                if (difBitArray[(lengthFrame + sinchro.length) * i + j] != sinchro[j]) {
                    countBitError++;
                }
            }
        }

        float treshold =  1.f - (countBitError / ((float) numFrame * (float) sinchro.length));

        return treshold >= this.frameTreshold;
    }

    private void creatGroup(){



        for(int i = 0; i < 15; i++) {

            int dataByte = 0;

            for(int j = 0; j < 8; j++) {

                dataByte <<= 1;
                dataByte += difBitArray[sinchro.length + i*8 +j];

            }
            System.out.print(dataByte);
            System.out.print(" ");

        }
        System.out.println("");


//        String groupString = "";
//
//        for(int i = 0; i < lengthFrame + sinchro.length; i++){
//            int bit = difBitArray[i];
//            if(bit == 0) {groupString += "0";}
//            else if (bit == 1) {groupString += "1";}
//            else {System.out.println("fuck");}
//
//        }
//
//        System.out.println(groupString);
    }


    private int counterBit = 0;

    public void addData(int difBit, Complex sempl) {

        System.arraycopy(difBitArray, 1, difBitArray, 0, difBitArray.length - 1);
        difBitArray[difBitArray.length - 1] = difBit;

        counterBit++;

        if (isFrameFind()) {

            System.out.println("Евріка!");
            counterBit = 0;
            creatGroup();


        }

        if(counterBit >= lengthFrame + sinchro.length ){

            //System.out.println("Сіхро втрачено! counterBit = " + counterBit);
            counterBit = 0;
        }



//        if (difBit == 0) {
//            dataString += "0";
//        }
//        else if(difBit == 1) {
//            dataString += "1";
//        }
//        else {
//            System.out.println("Fatal error");
//        }
//
//        if(dataString.length() == (lengthFrame + sinchro.length)) {
//            System.out.println(dataString);
//            dataString = "";
//        }
    }


}
