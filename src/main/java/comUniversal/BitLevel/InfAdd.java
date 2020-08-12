package comUniversal.BitLevel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InfAdd {
    static boolean lastbit = false;
    private int mod = 2;
    private List<RadiogramPercent> radiogramPercent = new ArrayList<>();
    public void addRadiogramPercentListener(RadiogramPercent listener){radiogramPercent.add(listener);}
    public void clearRadiogramPercentListener(){radiogramPercent.clear();}
    private void toRadiogramPercentListener(int percent) {
        if (!radiogramPercent.isEmpty())
            for (RadiogramPercent listener : radiogramPercent)
                listener.percent(percent);
    }
    private Queue<Integer> bits = new ArrayDeque<Integer>();
    boolean[] inputD = new boolean[128];
    private int totalSize = 0;
    int index = 0;
    int indexD = 0;

boolean[]outPut = new boolean[]{true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true,
            true,true,true,false,false,false,true,true,false,true
,false,true,true,true,true,true,true,false};

    public void add(String string) {

        byte[] input;
        boolean res[];
        input = string.getBytes();
        for (int i = 0; i <input.length ; i++) {
            input[i]=(byte) Character.getNumericValue(input[i]);
        }
        input = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9,0,1,2,3,4,5};
//        while(true){
//            //System.out.println("send");
//            res = getBits(input);
//            creatFrame(res);
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }
    private int oldPercent = 0;

    private void updatePercent(){
        if (totalSize != 0) {
            int currentPercent = (100 * bits.size()) / totalSize;
            int different = oldPercent - currentPercent;
            if (Math.abs(different) >= 1) {
                oldPercent = currentPercent;
                //System.out.println(currentPercent + " %");
                toRadiogramPercentListener(currentPercent);
            }
        }
    }

    public int getBit(){
//        if(bits.size() == 0 || bits == null) {
//            System.out.println("Data nulll");
//            return -1;
//        } else {
            updatePercent();
//            float ou = (float) Math.random();
//            boolean r = ou>0.5?true:false;
//
//            boolean defData = lastbit ^ r;
//            lastbit = defData;
//            return defData==true?1:0;
boolean data = outPut[indexD++];
if(indexD == 128){
    indexD=0;
}
            //boolean data = bits.poll()==1?true:false;
            boolean defData = lastbit ^ data;
            lastbit = defData;
            return defData==true?1:0;
       // }
    }

    private void creatFrame(boolean[] data){

    int len = data.length;
    boolean[] sinch = new boolean[]{false,true,true,true,true,true,true,false,false,true,true,true,true,true,true,false};

        for (int i = 0; i <len ; i++) {
            if(index<120){
                inputD[index]=data[i];
                index++;
            }
            else {
                System.arraycopy(sinch,0,inputD,index,8);
                addQue(inputD);
                index = 0;
                inputD[index++]=data[i];
            }

        }

    }

    private void addQue(boolean[] data){

        for (int i = 0; i <data.length ; i++) {
            bits.add(data[i]==true?1:0);
        }
        totalSize = bits.size();
    }
}
