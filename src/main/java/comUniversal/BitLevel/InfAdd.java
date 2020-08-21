package comUniversal.BitLevel;

import comUniversal.util.reedsolomon.reedsolomon.ReedSolomonEncoderDecoder;

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
    private Queue<Byte> data = new ArrayDeque<Byte>();
    private Queue<byte[]> encodByte = new ArrayDeque<byte[]>();
    int[] inputD = new int[133];
    int[] interlivBuf = new int[30];
    private int totalSize = 0;
    int index = 0;
    int index1 = 0;
    int indexD = 0;
    int print =0;
    int bit = 0;
    private String numberCorespondent;
    private int command1;
    private int command2;
    private int command3;
    private int command4;
    private boolean readyFlag = false;
    private int[] sinch = new int[]{1,1,1,1,1,0,0,1,0,1,0,0,0};

    public String getNumberCorespondent() {
        return numberCorespondent;
    }

    public void setNumberCorespondent(String numberCorespondent) {
        this.numberCorespondent = numberCorespondent;
    }
    public void setCommand1(int command1)
    {
        this.command1 = command1;
        System.out.println(command1);
    }
    public void setCommand2(int command2)
    {
        this.command2 = command2;
        System.out.println(command2);
    }
    public void setCommand3(int command3)
    {
        this.command3 = command3;
        System.out.println(command3);
    }
    public void setCommand4(int command4)
    {
        this.command4 = command4;
        System.out.println(command4);
    }
    public void add(String string, int tab) {


//        for (int i = 0; i < size; i++) {
//
//            System.out.println(data.poll());
//        }
//        while(true){
//            //System.out.println("send");
//           // res = getBits(input);
//            for (int i = 0; i < 10; i++) {
//                float ou = (float) Math.random();
//                int r = ou>0.5?1:0;
//                res[i]=r;
//            }
//
//            createFrame(res);
//            try {
//                Thread.sleep(0,1);
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
        //updatePercent();
//            float ou = (float) Math.random();
//            boolean r = ou>0.5?true:false;
//
//            boolean defData = lastbit ^ r;
//            lastbit = defData;
//            return defData==true?1:0;
//boolean data = outPut[indexD++];
//if(indexD == 130){
//    indexD=0;
//}

        if (index1%133==0) {
            readyFlag = update();
            //System.out.println("new");
        }
        if (readyFlag == false)
            return -1;
        boolean bit = inputD[index1%133]==1;
        //System.out.print(inputD[index1%132]);
        // boolean data = bits.poll()==1;

        boolean defData = lastbit ^ bit;
        lastbit = defData;
        index1++;
        return defData==true?1:0;
        //}
    }

    private void createFrame(int[] data){


        int len = data.length;
        // boolean[] sinch = new boolean[]{false,true,true,true,true,true,true,false};

        for (int i = 0; i <len ; i++) {
            if(index<120){
                inputD[index]=data[i];
                index++;
            }
            else {
                //System.arraycopy(sinch,0,inputD,index,10);
                inputD[120]=0;
                inputD[121]=1;
                inputD[122]=1;
                inputD[123]=1;
                inputD[124]=1;
                inputD[125]=1;
                inputD[126]=1;
                inputD[127]=0;
                //inputD[128]=0;
                //inputD[129]=0;

                addQue(inputD);
                index = 0;
                inputD[index++]=data[i];
            }

        }

    }

    private void addQue(int[] data){
        //System.out.println(data.length);
        for (int i = 0; i <data.length ; i++) {
            bits.add(data[i]);
        }
        totalSize = bits.size();
    }

    private boolean update(){

//        int shift = 0;
//        for (int i = 0; i <12 ; i++) {
//            inputD[shift++] = sinch[i];
//        }
//        for (int i = 0; i <6 ; i++) {
//            byte[] in = myCoder((byte) (i+2));
//
//            for (int j = 0; j <5 ; j++) {
//
//                inputD[shift++] = (in[j]&8)>>3;
//                inputD[shift++] = (in[j]&4)>>2;
//                inputD[shift++] = (in[j]&2)>>1;
//                inputD[shift++] = (in[j]&1);
//            }
//        }


        if(data.size() == 0 )
            return false;
        int shift = 0;
        for (int i = 0; i <6 ; i++) {
            byte[] in = myCoder((byte) (0));
            if(data.size() != 0 ){
                in = myCoder((byte) data.poll());
            }
            interlivBuf[shift]= in[0];
            interlivBuf[shift+6]= in[1];
            interlivBuf[shift+12]= in[2];
            interlivBuf[shift+18]= in[3];
            interlivBuf[shift+24]= in[4];
            shift++;
        }

        shift = 0;
        for (int i = 0; i <13 ; i++) {
            inputD[shift++] = sinch[i];
        }

        for (int i = 0; i <30 ; i++) {
            //byte[] in = myCoder((byte) (i+2));
//            interlivBuf[shift]= in[0];
//            interlivBuf[shift+6]= in[1];
//            interlivBuf[shift+12]= in[2];
//            interlivBuf[shift+18]= in[3];
//            interlivBuf[shift+24]= in[4];
//            shift+=5;
            //for (int j = 0; j <5 ; j++) {

            inputD[shift++] = (interlivBuf[i]&8)>>3;
            inputD[shift++] = (interlivBuf[i]&4)>>2;
            inputD[shift++] = (interlivBuf[i]&2)>>1;
            inputD[shift++] = (interlivBuf[i]&1);
            //}
        }
        return true;
    }

    private byte[] myCoder(byte data){
//        byte []inEncode = {(byte) (Math.random()*15.9)};
        if(data==0){
            data=10;
        }
        byte []inEncode = {data};
        return ReedSolomonEncoderDecoder.doEncode(inEncode,4);
    }

    public void addDataRg(String string) {

        for (int i = 0; i < 3; i++) {
            data.add((byte) 11);//mask corespondent
            data.add((byte) 10);
            data.add((byte) 10);
            data.add((byte) Character.getNumericValue(numberCorespondent.charAt(0)));
            data.add((byte) Character.getNumericValue(numberCorespondent.charAt(1)));
            data.add((byte) Character.getNumericValue(numberCorespondent.charAt(2)));
        }
        for (int i = 0; i < string.length(); i++) {
            if (i % 5 == 0) {
                data.add((byte) 14);// mask radiogram
            }
            data.add((byte) Character.getNumericValue(string.charAt(i)));
        }

        int size = data.size();
    }
    public static void main(String[] args) {
        InfAdd infAdd = new InfAdd();
        int shift = 0;
        for (int i = 0; i <6 ; i++) {
            byte[] in = infAdd.myCoder((byte) (i+1));
            infAdd.interlivBuf[shift]= in[0];
            infAdd.interlivBuf[shift+6]= in[1];
            infAdd.interlivBuf[shift+12]= in[2];
            infAdd.interlivBuf[shift+18]= in[3];
            infAdd.interlivBuf[shift+24]= in[4];
            shift++;
        }

        shift = 0;
        for (int i = 0; i <8 ; i++) {
            infAdd.inputD[shift++] = infAdd.sinch[i];
        }
        for (int i = 0; i <30 ; i++) {
            //byte[] in = myCoder((byte) (i+2));
//            interlivBuf[shift]= in[0];
//            interlivBuf[shift+6]= in[1];
//            interlivBuf[shift+12]= in[2];
//            interlivBuf[shift+18]= in[3];
//            interlivBuf[shift+24]= in[4];
//            shift+=5;
            //for (int j = 0; j <5 ; j++) {

            infAdd.inputD[shift++] = (infAdd.interlivBuf[i]&8)>>3;
            infAdd.inputD[shift++] = (infAdd.interlivBuf[i]&4)>>2;
            infAdd.inputD[shift++] = (infAdd.interlivBuf[i]&2)>>1;
            infAdd.inputD[shift++] = (infAdd.interlivBuf[i]&1);
            //}
        }
        for (int i = 0; i <128 ; i++) {

            if(i%4==0){
                System.out.println("");
            }
            System.out.print(infAdd.inputD[i]+" ");
        }
    }
}