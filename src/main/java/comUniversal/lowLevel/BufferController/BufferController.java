package comUniversal.lowLevel.BufferController;

import comUniversal.util.Complex;

import java.util.ArrayList;
import java.util.List;

public class BufferController {

    protected volatile int sampleFreq = 48_000;
    protected volatile int sampleCountPer10ms = sampleFreq / 100;
    protected volatile int sampleCountPer1ms = sampleFreq / 1000;

    private boolean finishingWork = false;

    private int percentBuffer = 0 ;
    private WorkingThread workingThread;
    private float k = 0;
    private GetIQSource getIQSource;

    // Listeners
    private List<IBufferController> controllers = new ArrayList<>();
    public void addTransferListener(IBufferController listener){controllers.add(listener);}
    public void clearTransferListener(){controllers.clear();}
    private void toListenersTransferDataBytes(Complex sample){
        if(!controllers.isEmpty())
            for(IBufferController listener: controllers)
                listener.sendData(sample);
    }


    public void setSources(GetIQSource sources) {
        this.getIQSource = sources;
    }

    public void updateSampleFrequency(int frequency) {
        this.sampleFreq = frequency;
    }

    public void updatePercent(int percent) {
        this.percentBuffer = percent;
    }

    public BufferController(int sampleFreq ){

        this.sampleFreq = sampleFreq;
        if(sampleFreq == 48000){
            k = 4;
        }else
            k = (float) 0.6;

        sampleCountPer10ms = sampleFreq / 100;
        sampleCountPer1ms = sampleFreq / 1000;

        workingThread = new WorkingThread();
        workingThread.start();
    }

    class WorkingThread extends Thread {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            long executeTime  = 1;

            while (true) {
                long delay  = 5;

                if (finishingWork) {
                    continue;
                }
                if (percentBuffer < 60) {
                    int needSendSample = sampleCountPer1ms * (int) executeTime;
                    needSendSample+= (int) ((int)(k*executeTime)*sampleCountPer1ms);
                    for (int i = 0; i < needSendSample ; i++) {
                        Complex sample = new Complex(0.0F, 0.0F);
                        if (getIQSource != null) {
                            sample = getIQSource.getIQ();
                        }
                        toListenersTransferDataBytes(sample);
                    }
                }

                executeTime = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BufferController bufferController = new BufferController(3000);

        while (true){

           // bufferController.sendIQ();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
