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
    private String sendFrequency;
    private String koef;
    private String command1;
    private String command2;
    private String command3;
    private String command4;
    private boolean readyFlag = false;
    private boolean flagData = false;
    private int[] sinch = new int[]{1,1,1,1,1,0,0,1,0,1,0,0,0};

    public String getNumberCorespondent() {
        return numberCorespondent;
    }

    public void setNumberCorespondent(String numberCorespondent) {
        this.numberCorespondent = numberCorespondent;
    }
    public void setCommand1(String command1)
    {
        this.command1 = command1;
    }
    public void setKoef(String koef)
    {
        this.koef = koef;
    }
    public void setCommand2(String command2)
    {
        this.command2 = command2;
        System.out.println(command2);
    }
    public void setCommand3(String command3)
    {
        this.command3 = command3;
        System.out.println(command3);
    }
    public void setCommand4(String command4)
    {
        this.command4 = command4;
        System.out.println(command4);
    }
    public void setSendFrequency(String frequency) {
        this.sendFrequency = frequency;
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
            int currentPercent = (100 * data.size()) / totalSize;
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
        updatePercent();
        if (index1%133==0) {
            flagData = update();
            //System.out.println("new");
        }
        if (flagData == false)
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


       if (readyFlag == false)
           return false;
        int shift = 0;
        for (int i = 0; i <6 ; i++) {
            byte[] in = myCoder((byte) (0));
            if(data.size() != 0 ){
                byte tmp = data.poll();
                System.out.print(tmp);
                in = myCoder( tmp);
            }else {
                readyFlag = false;
                return false;
            }
            interlivBuf[shift]= in[0];
            interlivBuf[shift+6]= in[1];
            interlivBuf[shift+12]= in[2];
            interlivBuf[shift+18]= in[3];
            interlivBuf[shift+24]= in[4];
            shift++;
            //totalSize--;
        }
        System.out.println("");
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

    public void addDataRg(String string, String count) {
        int countRepeat = Integer.parseInt(count);
        System.out.println("Count repeat RG "+countRepeat);
        for (int u = 0; u <countRepeat ; u++) {

            for (int i = 0; i < 3; i++) {
                data.add((byte) 11);//mask corespondent
                data.add((byte) 10);
                data.add((byte) 10);
                data.add((byte) Character.getNumericValue(numberCorespondent.charAt(0)));
                data.add((byte) Character.getNumericValue(numberCorespondent.charAt(1)));
                data.add((byte) Character.getNumericValue(numberCorespondent.charAt(2)));
            }
            int lenght = (5 - string.length()%5);

            for (int i = 0; i < string.length(); i++) {
                if (i % 5 == 0) {
                    data.add((byte) 14);// mask radiogram
                }
                data.add((byte) Character.getNumericValue(string.charAt(i)));
            }
            if(lenght !=5) {
                for (int i = 0; i < lenght; i++) {
                    data.add((byte) 10);
                }
            }
        }
        totalSize = data.size();
        readyFlag = true;
    }

    public void addDataService( String time) {
        int txByte =0;
        int repeat = 0;
        int timeCount = Integer.parseInt(time);
        int tx  = 0;
       do {
           txByte = 0;
           if (sendFrequency != null) {
               for (int i = 0; i < 1; i++) {
                   data.add((byte) 11);//mask corespondent
                   data.add((byte) 10);
                   data.add((byte) 10);
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(0)));
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(1)));
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(2)));
                   data.add((byte) 12);//mask freqq
                   data.add((byte) ((Character.getNumericValue(sendFrequency.charAt(0)) + Character.getNumericValue(koef.charAt(0))) % 10));
                   data.add((byte) ((Character.getNumericValue(sendFrequency.charAt(1)) + Character.getNumericValue(koef.charAt(1))) % 10));
                   data.add((byte) ((Character.getNumericValue(sendFrequency.charAt(2)) + Character.getNumericValue(koef.charAt(2))) % 10));
                   data.add((byte) ((Character.getNumericValue(sendFrequency.charAt(3)) + Character.getNumericValue(koef.charAt(3))) % 10));
                   data.add((byte) ((Character.getNumericValue(sendFrequency.charAt(4)) + Character.getNumericValue(koef.charAt(4))) % 10));
               }
               txByte += 12;
           }

           if (command1 != null && !command1.equals("")) {
                   data.add((byte) 11);//mask corespondent
                   data.add((byte) 10);
                   data.add((byte) 10);
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(0)));
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(1)));
                   data.add((byte) Character.getNumericValue(numberCorespondent.charAt(2)));
                   txByte += 6;

                   for (int y = 0; y < command1.length(); y += 2) {
                       data.add((byte) 13);//mask command
                       data.add((byte) Character.getNumericValue(command1.charAt(y)));
                       data.add((byte) Character.getNumericValue(command1.charAt(y + 1)));
                       data.add((byte) 13);//mask command
                       data.add((byte) Character.getNumericValue(command1.charAt(y)));
                       data.add((byte) Character.getNumericValue(command1.charAt(y + 1)));
                       txByte +=6;
                   }

           }

           txByte =((txByte * 20) +( (txByte/6) * 13));
           float timeTransm = (float) txByte/100.f;
           repeat =  (int)(((timeCount*60.f)/timeTransm)+1.f);
           System.out.println("Rep "+repeat);
           tx++;
       }while (repeat>=tx);
        sendFrequency = null;
        command1 = null;
        totalSize = data.size();
        readyFlag = true;
    }
    public void clearTxQueue(){
        data.clear();
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