package comUniversal.BitLevel.decoder;

import comUniversal.lowLevel.Debuger.Debuger;
import comUniversal.util.reedsolomon.reedsolomon.ReedSolomonEncoderDecoder;
import comUniversal.util.reedsolomon.zxing.common.reedsolomon.ReedSolomonEncoder;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class StrymDecoder {

    private MessageListener messageListener;
    public void addMessageListener(MessageListener listener){
        this.messageListener = listener;
    }

    private int[] mask = {1,1,1,1,1,0,0,1,0,1,0,0,0};//{1,1,1,0,0,0,1,0,0,1,0};// {0,1,1,1,1,1,1,1,1,1,0};
    private int numFrame = 8; // кількість кадрів
    private int lengthFrame = 120; // довжина кадру
    private int difBitArray[] = new int[numFrame * (lengthFrame + mask.length)];
    private int bitCounter = 0;


    private FramePositionFinder framePositionFinder;

    private String dataString = "";

    public StrymDecoder (Debuger debuger){
        framePositionFinder = new FramePositionFinder(lengthFrame, mask, debuger);
    }

    private boolean isFrameFindTest(){



        if (difBitArray.length == 0)
            return false;

        boolean result = false;
        int majoritary;
        int bit;

        for (int i = 0; i < mask.length; i++) {

            majoritary = 0;

            for (int j = 0; j < numFrame; j++)
                majoritary += difBitArray[(lengthFrame + mask.length) * j + i];

            bit = (majoritary > numFrame / 2)? 1: 0;

            result = (bit == mask[i]);

            if(!result)
                break;

        }

        return result;

    }

    private byte toSymbol(int[] src){
        byte symbol = 0;
        for(int i = 0; i < src.length; i++) {
            symbol <<= 1;
            symbol |= (src[i] & 0x01);
        }
        return symbol;
    }

    private byte[] deInterLiving(byte[] symbols) {
        byte[] result = new byte[symbols.length];

        int index = 0;
        for(int i = 0; i < 6; i++)
            for(int j = 0; j < 5; j++)
                result[index++] = symbols[i + 6 * j];

        return result;
    }




    private void creatGroup(int position){

        int shift = mask.length + position;

        byte[] allSymbols = new byte[5*6];

        for(int i = 0; i < allSymbols.length; i++) {
            int[] s = new int[4];
            System.arraycopy(difBitArray, 4*i + shift, s, 0, s.length);
            allSymbols[i] = toSymbol(s);
        }

        byte[] deInterLivingData = deInterLiving(allSymbols);

        String message = "";

        for(int i = 0; i < 6; i++){
            byte[] symbols = new byte[5];
            System.arraycopy(deInterLivingData, 5*i, symbols, 0, symbols.length);

            String symbol;
            try {
                symbol = String.format("%01X", ReedSolomonEncoderDecoder.doDecode(symbols, 4)[0]);
            } catch(Exception e) {
                symbol = "*";
            }

            if(symbol.equals("A"))
                symbol = "0";

            message += symbol;


        }

        if (messageListener != null)
            messageListener.setSymbol(message);

        // Debug
        System.out.print("Фрейм знайдено, позиція " + framePositionFinder.getPosition() + ", Біти: ");
        for(int i = 0; i < mask.length; i++)
            System.out.print(difBitArray[position + i]);
        System.out.print(" ");
        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 20; j++)
                System.out.print(difBitArray[mask.length + position + i * 20 + j]);
            System.out.print(" ");
        }
        System.out.println("Дані: " + message);
        // Debug
    }


    public void addData(int difBit, Complex sempl) {

        shiftBitArray(difBit);

        // debug
        dataString += Integer.toString(difBit);

        // cutting on frame
        if(bitCounter % (lengthFrame + mask.length) != 0 ) return;

        // find frame
        if (framePositionFinder.isFind(difBitArray)){
            creatGroup(framePositionFinder.getPosition());
        } else {
            System.out.println("Пошук фрейму");
        }
        dataString = "";

    }

    private void shiftBitArray(int bit){
        System.arraycopy(difBitArray, 1, difBitArray, 0, difBitArray.length - 1);
        difBitArray[difBitArray.length - 1] = bit;
        bitCounter++;
    }

}
