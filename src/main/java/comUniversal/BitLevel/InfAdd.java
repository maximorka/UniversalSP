package comUniversal.BitLevel;

import comUniversal.util.reedsolomon.reedsolomon.ReedSolomonEncoderDecoder;

import java.util.*;

public class InfAdd {

    private int frameLength = 120;
    private boolean lastBit;
    private int index;
    private boolean[] mask = {true,true,true,true,true,false,false,true,false,true,false,false,false};// {true,true,true,false,false,false,true,false,false,true,false};// {false, true, true, true, true, true, true, true, true, true, false};
    private boolean[] data = new boolean[mask.length + frameLength];
    private Random random = new Random();

    public InfAdd(){
        dataUpdate();
    }

    private boolean[] symbolToBooleanArray(byte symbol){
        symbol &= 0xF;
        boolean[] result = new boolean[4];
        result[0] = (((symbol >> 3) & 0x01) == 1)? true: false;
        result[1] = (((symbol >> 2) & 0x01) == 1)? true: false;
        result[2] = (((symbol >> 1) & 0x01) == 1)? true: false;
        result[3] = (((symbol >> 0) & 0x01) == 1)? true: false;
        return result;
    }

    private boolean[] toBooleanArray(byte[] rsCode) {
        boolean[] result = new boolean[4 * rsCode.length];
        for(int i = 0; i < rsCode.length; i++){
            boolean[] bits = symbolToBooleanArray(rsCode[i]);
            System.arraycopy(bits, 0, result, 4*i, bits.length);
        }
        return result;
    }

    private byte[] interLiving(byte[] symbols) {
        byte[] result = new byte[symbols.length];

        int index = 0;
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 6; j++)
                result[index++] = symbols[i + 5 * j];

        return result;
    }

    private void dataUpdate(){

        // mask insert
        System.arraycopy(mask, 0, data, 0, mask.length);

        // data insert
        byte[] rs6 = getRs6();
        byte[] rs6Inter = interLiving(rs6);
        boolean[] bits = toBooleanArray(rs6Inter);
        System.arraycopy(bits, 0, data, mask.length, bits.length);

    }

    private byte[] getRs6(){
        byte[] result = new byte[6 * 5];
        for(int i = 0; i < 6; i++) {
            byte[] rs = rsGet();
            System.arraycopy(rs, 0, result,  i * rs.length, rs.length);
        }
        return result;
    }

    private byte[] rsGet(){
        byte[] symbol = {(byte)random.nextInt(15)};
        return ReedSolomonEncoderDecoder.doEncode(symbol, 4);
    }


    private void dataRandomUpdate(){
        for(int i = 0; i < data.length; i++)
            data[i] = random.nextBoolean();
        System.arraycopy(mask, 0, data, 0, mask.length);
    }

    private int booleanToInt(boolean src){
        return src? 1:0;
    }

    private int difCoder(boolean bit){
        lastBit ^= bit;
        return booleanToInt(lastBit);
    }

    public int getBit(){
        if(index % data.length == 0)
            dataUpdate();
        return difCoder(data[index++ % data.length]);
    }

}
