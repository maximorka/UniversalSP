package comUniversal.deviceLevel;

import comUniversal.BitLevel.decoder.KylymDecoder;
import comUniversal.lowLevel.Demodulator.OptimalNonCoherent;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayDeque;
import java.util.Queue;

public class ProgramKylym {
    public OptimalNonCoherent optimalNonCoherent100 = new OptimalNonCoherent(100);;
    public OptimalNonCoherent optimalNonCoherent250 = new OptimalNonCoherent(250);;
    public KylymDecoder kylymDecoder100 = new KylymDecoder(100);
    public KylymDecoder kylymDecoder250 = new KylymDecoder(250);
   // public Digital digital;
    private Queue <Integer> dataInput = new ArrayDeque<>();
    WorkingThread workingThread = new WorkingThread();
    public void add(int data){
        dataInput.add(data);
    }
//    public ProgramKylym(){
//        //kylymDecoder100.setRunning(true);
//       // kylymDecoder250.setRunning(true);
//
//        //digital = digital -> add(digital);
//        optimalNonCoherent100.addListenerSymbol(data -> kylymDecoder100.addData(data));
////        optimalNonCoherent250.addListenerSymbol(data -> kylymDecoder250.addData(data));
//        workingThread.start();
//    }

    public void fromEthernet(Complex data) {
        Complex sempl = new Complex((float) data.getReal(),(float) data.getImaginary());
        optimalNonCoherent100.demodulate(sempl);
        optimalNonCoherent250.demodulate(sempl);
    }
    public int getData(int[] data) {
        int length = Math.min(data.length, dataInput.size());
        for (int i = 0; i <length; i++)
            data[i] = dataInput.poll();
        return length;
    }
    class WorkingThread extends Thread {
        @Override
        public void run() {

        }
    }
}
