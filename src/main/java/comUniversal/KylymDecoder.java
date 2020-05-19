package comUniversal;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class KylymDecoder {
    private Queue <Integer> data = new ArrayDeque<>();
    private String bufResult[] = new String[128];
    private int bufInput[] = new int[128];
    private int index = 0;
    private int indexInput = 0;
    private int symbol =0;
    int countSymbol = 0;
    int countP = 0;
    Map<Integer, String> number = new HashMap<Integer, String>();

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
    }
    class WorkingThread extends Thread{
        @Override
        public void run() {
            while (true){
                if(data.size() !=0){
                    symbolForBit();
                }
            }
        }
    }
    public void symbolForBit(){
        bufInput[indexInput] = data.poll();
        symbol<<=1;
        symbol |=  bufInput[indexInput];
        symbol&=63;
        indexInput++;
        indexInput&=128;
        countSymbol++;
        findMask(symbol);
        if ()
    }
private boolean findMask(int symbol){
        String value = number.get(symbol);
        if(value == "P"){
            countP++;
            return true;
        }
        return false;
}

    public static void main(String[] args) {
        KylymDecoder kylymDecoder = new KylymDecoder();
        kylymDecoder.addData(1);
        kylymDecoder.addData(1);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(1);

        kylymDecoder.addData(0);
        kylymDecoder.addData(1);
        kylymDecoder.addData(1);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(1);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(0);
        kylymDecoder.addData(1);

        for (int i = 0; i <20 ; i++) {
            kylymDecoder.symbolForBit();
        }

        //kylymDecoder.checkCadr();
        //System.out.println("parseData:"+);


    }
}
