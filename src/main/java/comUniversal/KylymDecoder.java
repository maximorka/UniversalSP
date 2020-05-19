package comUniversal;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class KylymDecoder {
    private Queue <Integer> data = new ArrayDeque<>();
    private String bufResult[] = new String[128];
    private int index = 0;
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
        symbol<<=1;
        symbol |= data.poll();
        symbol&=63;

        String value = number.get(symbol);
        if(value!= null){
            countSymbol++;
            if(value == "P" ){
                System.out.println("Find mask");
                countP++;
                countSymbol=0;
            }
            bufResult[index++] = value;
            System.out.println(bufResult[index-1]);
        }

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

        for (int i = 0; i <8 ; i++) {
            kylymDecoder.symbolForBit();
        }

        //kylymDecoder.checkCadr();
        //System.out.println("parseData:"+);


    }
}
